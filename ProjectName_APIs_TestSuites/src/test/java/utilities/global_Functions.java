package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;
import org.apache.maven.shared.utils.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;


public class global_Functions 
{
	public static String BrowserType_Value;
	public static WebDriver DriverOBJ;
	static Properties pfOBJ = new Properties();
	static String ProjectPath = System.getProperty("user.dir");
	
	public static String BT_Name = null;
	public static String TestEnv_Value = null;
	public static String TestEnv1 = "Test1";
	public static String Selected_BT;
	public static String Selected_TT;
	public static String ProjectRelativePath_Value;
	public static excelUtilities excelOBJ;
	public static ExtentTest parentReportOBJ;
	public static ExtentTest childReportOBJ;
	public static ExtentTest ExtentReportOBJ;
	

	public static excelUtilities writeToExcelOBJ;
	public static Logger loggerOBJ;
	
	public static ExtentHtmlReporter ReporterOBJ;
	public static ExtentReports ReportOBJ;
	
	public static String EnvURL = null;
	public static String TestType = null;
	
	//Global Functions Object Creation
		static global_Functions gfOBJ = new global_Functions();	
			
	@Parameters({"EnvURL", "TestType"})
	@BeforeTest
	public static void setup_Fun(ITestContext ContextOBJ, String EnvURL, String TestType)
	{
		 //Calling This function to Get BrowserType Value from Properties File		
	        if (EnvURL.isEmpty() || TestType.isEmpty()) 
				{
		        	Selected_TT = gfOBJ.TestTypeRequest_Fun();
		        	System.out.println("Selected Test Type Is: "+Selected_TT);
	        		global_Functions.setProperties("TestType_Value", Selected_TT, Selected_TT);
	        	
	        		String Env_Value = JOptionPane.showInputDialog(null,"Enter URL"); 
	        		System.out.println("Script Using Environment Variable Value from Config Properties File "+Env_Value);
	        		global_Functions.setProperties("API_Env_URL", Env_Value, Selected_TT);
	        	}
			else
				{
					System.out.println("Entered Test Type Is: "+TestType);
					Selected_TT = TestType;
					//String Env_Value = EnvURL;
					global_Functions.setProperties("API_Env_URL", EnvURL, Selected_TT);
					global_Functions.setProperties("TestType_Value", TestType, Selected_TT);
				}
	        
	        
//Reporting Setup Starts
			
			//Getting Suite Name Starts
				String TestSuiteName = ContextOBJ.getSuite().getName();
				System.out.println("Test Suite Name Is: "+TestSuiteName);
				global_Functions.setProperties("testSuiteName", TestSuiteName, Selected_TT);
			//Getting Suite Name Ends
			
			//Test Result Folder Creation Starts	
				global_Functions.CreateTestResultFolder_Fun();	
			//Test Result Folder Creation Starts
			
			String dateTimeStamp_Value = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
			
			
			//ReporterOBJ = new ExtentHtmlReporter("./testExecution_Reports/"+gfOBJ.getProperties("RunResultFolderName")+"/TestRunReport_"+dateTimeStamp_Value+".html");
		   	
			if (TestSuiteName.contains("FT"))
			{
				ReporterOBJ = new ExtentHtmlReporter("./FunctionalTestsExecution_Results_And_Reports/"+global_Functions.getProperties("RunResultFolderName", Selected_TT)+"/TestRunReport_"+dateTimeStamp_Value+".html");
			}
			else
			{	
				ReporterOBJ = new ExtentHtmlReporter("./SmokeTestsExecution_Results_And_Reports/"+global_Functions.getProperties("RunResultFolderName", Selected_TT)+"/TestRunReport_"+dateTimeStamp_Value+".html");
			}
			
	        // create ExtentReports and attach reporter(s)
	        ReportOBJ = new ExtentReports();
	        ReportOBJ.attachReporter(ReporterOBJ);
	    //Reporting Setup Ends
		
		}

	public static void CreateTestResultFolder_Fun()
	{
		String timeStamp_Value = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		String RunResultFolderName_Value = global_Functions.getProperties("testSuiteName", Selected_TT)+"_Result_"+timeStamp_Value;
		if (global_Functions.getProperties("testSuiteName", Selected_TT).contains("FT"))
		{	
			File RunResultFolderPath = new File("./FunctionalTestsExecution_Results_And_Reports/"+RunResultFolderName_Value);
			RunResultFolderPath.mkdir();
		}
		else
		{
			File RunResultFolderPath = new File("./SmokeTestsExecution_Results_And_Reports/"+RunResultFolderName_Value);
			RunResultFolderPath.mkdir();
		}	
		global_Functions.setProperties("RunResultFolderName", RunResultFolderName_Value, Selected_TT);
		System.out.println("Run Result Folder Name Is: "+global_Functions.getProperties("RunResultFolderName", Selected_TT));
	}
	
	public void Open_And_LaunchApp()
	{
		//Launch Application Starts
			gfOBJ.LaunchApplication_Fun();
			String URL_Value = DriverOBJ.getCurrentUrl();
			System.out.println("Browser Launched: "+URL_Value);
		//Launch Application Ends
	}
	
	public static ExtentTest ExtentReportForTest_Fun(String TestNameForExtentReport)
	{
		//Creating Reporter Object to report statuses in this class.
			ExtentReportOBJ = ReportOBJ.createTest(TestNameForExtentReport, "Test: "+TestNameForExtentReport+" Executed In***"+global_Functions.getProperties("TestEnv", Selected_TT)+"***Environment");
		//Creating Reporter Object to report statuses in this class.
		
		return ExtentReportOBJ;
	}
	
	
	public String BrowserTypeRequest_Fun()
	{
		String[] BT_Choices = {"IE", "Chrome", "Firefox", "Edge"}; //BrowserType_Choices
		String Selected_BT = (String) JOptionPane.showInputDialog(null, "Choose Browser Type", 
		"Select Browser Type Against You Want To Open Application", 
		JOptionPane.QUESTION_MESSAGE, null, 
		BT_Choices, BT_Choices[0]);
		System.out.println("Selected Browser Type Is: "+Selected_BT);
		return Selected_BT;
	}
	
	public String TestTypeRequest_Fun()
	{
		String[] TT_Choices = {"ST", "FT"}; //TestType_Choices
		String Selected_TT_Value = (String) JOptionPane.showInputDialog(null, "Choose Test Type", 
		"Select Test Type that you would like to Execute", 
		JOptionPane.QUESTION_MESSAGE, null, 
		TT_Choices, TT_Choices[0]);
		System.out.println("Selected Test Type Is: "+Selected_TT_Value);
		return Selected_TT_Value;
	}
	
	
	
	@AfterTest
	public void TearDown() throws Exception
	{
		ReportOBJ.flush();
	}
	
	public static excelUtilities TestDataFile_Fun(String FileName, String SheetName)
	{
		//Excel Setup Starts
			ProjectRelativePath_Value = System.getProperty("user.dir");
			excelOBJ = new excelUtilities(ProjectRelativePath_Value+FileName, SheetName);
		//Excel Setup Ends
		return excelOBJ;
	}
	
	public static void InputFileStreamCheck_Fun(File i_filePathOBJ) //IFP=InputFilePath
	{
		if (i_filePathOBJ.isFile() && i_filePathOBJ.exists()) 
			{
	            System.out.println("Excel File Opened Successfully");
	        }
	    else 
	        { 
	        	System.out.println("Excel File not exist or can't open");
	        }
	}
	
	
	public WebDriver LaunchBrowser_Fun(String Selected_BT)
	{
		
		if (Selected_BT.equals("Chrome"))
    	{
			System.setProperty("webdriver.chrome.driver", "./browsers_Drivers/chromedriver");
			DriverOBJ = new ChromeDriver();
			DriverOBJ.manage().window().maximize();
			System.out.println("Expected Browser*** "+Selected_BT+" *** Launched");
    	}
		
		if (Selected_BT.equals("Firefox"))
    	{
			System.setProperty("webdriver.gecko.driver", "./browsers_Drivers/geckodriver.exe");
			DriverOBJ = new FirefoxDriver();
			DriverOBJ.manage().window().maximize();
			System.out.println("Expected Browser*** "+Selected_BT+" *** Launched");
    	}
		
		if (Selected_BT.equals("Edge"))
    	{
			System.setProperty("webdriver.edge.driver", "./browsers_Drivers/msedgedriver.exe");
			DriverOBJ = new EdgeDriver();
			DriverOBJ.manage().window().maximize();
			System.out.println("Expected Browser*** "+Selected_BT+" *** Launched");
    	}
		
		// start reporters
				
		return DriverOBJ;
	}
	
	public void LaunchApplication_Fun()
	{
		String AppURL_Value = "http://identity.bancadigitaldev.banesco.com.do/auth/realms/banesco-retail/protocol/openid-connect/auth?client_id=banesco-retail&redirect_uri=http%3A%2F%2Fcx.bancadigitaldev.banesco.com.do%2Fbanesco-retail%2Flogin%3Fredirect%3Dhttp%3A%2F%2Fcx.bancadigitaldev.banesco.com.do%2Fbanesco-retail%2Findex&state=87119606-9829-4d3f-8fb4-57a060dda76c&response_mode=fragment&response_type=code%20id_token%20token&scope=openid&nonce=61265972-f289-4dda-8fb3-4ddc5ae89172&ui_locales=es-DO%20en-US&kc_locale=es-DO";
		DriverOBJ.get(AppURL_Value);
		
	}
	
	public void CloseBrowserAndApplication()
	{
		DriverOBJ.quit();
	}
	
	public static String getProperties(String ParameterName, String TT)
	{
		String Parameter_Value = null;
		if (TT.equals("ST"))
			{
				try 
					{
						InputStream inputOBJ = new FileInputStream(ProjectPath+"/src/test/java/configPackage/ST_config.properties");
						try 
						{
							pfOBJ.load(inputOBJ);
							Parameter_Value = pfOBJ.getProperty(ParameterName);
							//System.out.println(Parameter_Value);
							
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} 
				catch (FileNotFoundException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		else
		{
			try 
				{
					InputStream inputOBJ = new FileInputStream(ProjectPath+"/src/test/java/configPackage/FT_config.properties");
					try 
					{
						pfOBJ.load(inputOBJ);
						Parameter_Value = pfOBJ.getProperty(ParameterName);
						//System.out.println(Parameter_Value);
						
					} 
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return Parameter_Value;
	}
	
	public static void setProperties(String PN, String PV, String TT)
	{
		if (TT.equals("ST"))
			{
				try 
					{
						
						OutputStream outputOBJ = new  FileOutputStream(ProjectPath+"/src/test/java/configPackage/ST_config.properties");
						pfOBJ.setProperty(PN, PV);
						
						try 
							{
								pfOBJ.store(outputOBJ, "This is Writing BrowserType Value In The Properties File");
							} 
						catch (IOException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					} 
				catch (FileNotFoundException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		if (TT.equals("FT"))
			{
				try 
				{
					
					OutputStream outputOBJ = new  FileOutputStream(ProjectPath+"/src/test/java/configPackage/FT_config.properties");
					pfOBJ.setProperty(PN, PV);
					
					try 
						{
							pfOBJ.store(outputOBJ, "This is Writing BrowserType Value In The Properties File");
						} 
					catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				} 
			catch (FileNotFoundException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	public static String getScreenShot_Fun(WebDriver DriverOBJ, String screenshotName) throws Exception 
	{
		TakesScreenshot takeScreenshotOBJ = (TakesScreenshot) DriverOBJ;
		
		File screenshotsSource = takeScreenshotOBJ.getScreenshotAs(OutputType.FILE);
		
		//This section of code is to create Screen Shots Folder in the Current Run Test Result Folder Start
			String RunResultFolderName_V = global_Functions.getProperties("RunResultFolderName", Selected_TT);
			String SS_FloderName_Value = RunResultFolderName_V+"/ScreenShots";
			if (global_Functions.getProperties("testSuiteName", Selected_TT).contains("FT"))
			{
				File SS_Folder_Path = new File("./FunctionalTestsExecution_Results_And_Reports/"+SS_FloderName_Value);
				SS_Folder_Path.mkdir();
			}
			else
			{
				File SS_Folder_Path = new File("./SmokeTestsExecution_Results_And_Reports/"+SS_FloderName_Value);
				SS_Folder_Path.mkdir();
			}
		//This section of code is to create Screen Shots Folder in the Current Run Test Result Folder Start
		
		if (global_Functions.getProperties("testSuiteName", Selected_TT).contains("FT"))
		{
			String screenshotsDestination = System.getProperty("user.dir")+"/FunctionalTestsExecution_Results_And_Reports/"+SS_FloderName_Value+"/"+screenshotName+System.currentTimeMillis()+".png";
			File fileDestination = new File(screenshotsDestination);
			FileUtils.copyFile(screenshotsSource, fileDestination);
			return screenshotsDestination;
		}
		else
		{	
			String screenshotsDestination = System.getProperty("user.dir")+"/SmokeTestsExecution_Results_And_Reports/"+SS_FloderName_Value+"/"+screenshotName+System.currentTimeMillis()+".png";	
			File fileDestination = new File(screenshotsDestination);
			FileUtils.copyFile(screenshotsSource, fileDestination);
			return screenshotsDestination;
		}	
			
	}
	
	
}
