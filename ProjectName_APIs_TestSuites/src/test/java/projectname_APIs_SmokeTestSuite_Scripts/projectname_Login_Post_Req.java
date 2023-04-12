package projectname_APIs_SmokeTestSuite_Scripts;

import com.aventstack.extentreports.ExtentTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.global_Functions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;



public class projectname_Login_Post_Req extends global_Functions
{
	//Global Functions Object Creation
			static global_Functions gfOBJ = new global_Functions();
			
			public XSSFWorkbook w_bOBJ;
			public XSSFSheet w_sOBJ;
			public CellStyle StyleOBJ;
			
			public static ExtentTest ParentReportOBJ;
			
			String CalssName_V = this.getClass().getSimpleName();
			
			public static ExtentTest childReportOBJ;
			public static ExtentTest childChildReportOBJ;
		
	@Test
	public void FetchApplicant() throws IOException
	{
		ProjectRelativePath_Value = System.getProperty("user.dir");
		
		//File Input Stream Setup Starts
			File I_filePathOBJ = new File(ProjectRelativePath_Value+"/smoke_testscripts_DataFiles/"+CalssName_V+"_DF.xlsx");
			FileInputStream fisOBJ = new FileInputStream(I_filePathOBJ);
			w_bOBJ = new XSSFWorkbook(fisOBJ);
			// Input File Stream Opened or Not Check Starts
				global_Functions.InputFileStreamCheck_Fun(I_filePathOBJ);
			// Input File Stream Opened or Not Check Ends
			w_sOBJ = w_bOBJ.getSheet("Global");
		//File Input Stream Setup Ends
		
		// Report Name Assignment Starts
			ExtentTest parentReportOBJ = global_Functions.ExtentReportForTest_Fun(CalssName_V);
		// Report Name Assignment Ends
		
		// Child Report Name Assignment Starts
			childReportOBJ = parentReportOBJ.createNode(CalssName_V);
		// Child Report Name Assignment Ends
			
		String timeStamp_Value = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		
		
		//This section is to get the Row Count From DataFile Starts
			int RowCount_Value = w_sOBJ.getPhysicalNumberOfRows();
			System.out.println("Row Count: "+RowCount_Value);
		//This section is to get the Row Count From DataFile Ends
	
	    //This section is to get the Column Count From DataFile Starts
			int ColumnCount_Value = w_sOBJ.getRow(0).getLastCellNum();
			System.out.println("Column Count: "+ColumnCount_Value);
		//This section is to get the Column Count From DataFile Ends
			
					
		//Hash Map Object Creation Starts to get the column name and corresponding values
	    	HashMap<String, String> cn_OBJ = new HashMap<String, String>(); //cnOBJ=columnnameOBJ
	    //Hash Map Object Creation Ends	
	    	
	    //Hash Map Object Creation Starts to get the column name and corresponding values
	    	HashMap<String, Integer> columnNameIndex_OBJ = new HashMap<String, Integer>(); //cnOBJ=columnnameOBJ
	    //Hash Map Object Creation Ends	
	    	
	    cn_OBJ.clear();
	    columnNameIndex_OBJ.clear();
	
	    
		for (int rli=1; rli<=(RowCount_Value-1); rli++)
	    {
			//This Loop is to Get the Column Name and corresponding Value Start
			 	for (int cni=0; cni<ColumnCount_Value; cni++)
				    {
				    	String Column_Name = w_sOBJ.getRow(0).getCell(cni).getStringCellValue();
			    		String Column_Value = w_sOBJ.getRow(rli).getCell(cni).getStringCellValue(); 
			    		//System.out.println("Column Name Is: "+Column_Name+" And Corresponding Value Is: "+Column_Value);
			    		cn_OBJ.put(Column_Name, Column_Value);
			    		columnNameIndex_OBJ.put(Column_Name, cni);
			       	}		    	
	    	//This Loop is to Get the Column Name and corresponding Value Ends 
						 	
			// Child Child Report Section Declaration Starts
	    		childChildReportOBJ = childReportOBJ.createNode("Test Case: "+cn_OBJ.get("TC_Number")+" - "+cn_OBJ.get("TC_Name").toString());
		    // Child Child Report Section Declaration Ends 
			 	
	    		//Test Cases Execution Starts
	    	if (cn_OBJ.get("TC_ExecutionFlg_V").equals("yes"))
		    	{
		    		
	    			XSSFSheet TC_SheetOBJ = w_bOBJ.createSheet("TC_"+rli);
	    			
	    			Integer EV_CNV = 0;
		    		Row row0 = TC_SheetOBJ.createRow(0);
		    		Row row1 = TC_SheetOBJ.createRow(1);
		    		for (int cni=0; cni<ColumnCount_Value; cni++)
				    {
				    	String Column_Name = w_sOBJ.getRow(0).getCell(cni).getStringCellValue();
			    		String Column_Value = w_sOBJ.getRow(rli).getCell(cni).getStringCellValue(); 
			    		//System.out.println("Column Name Is: "+Column_Name+" And Corresponding Value Is: "+Column_Value);
			    		row0.createCell(EV_CNV).setCellValue(Column_Name);
			    		row1.createCell(EV_CNV).setCellValue(Column_Value);
			    		EV_CNV = EV_CNV+1;
			       	}
		    		
		    	//******************API Request Section ***********************************************************************	Starts 		    		
	    			
	    			//Assigning UserName and Password Values to Variables in Config.Properties File, the Values from Input Excel File For Further use in While Submitting Request Starts
		    			global_Functions.setProperties("UserName_Value", cn_OBJ.get("UserName_V"), Selected_TT);
		    			global_Functions.setProperties("Password_Value", cn_OBJ.get("Password_V"), Selected_TT);
		    		//Assigning UserName and Password Values to Variables in Config.Properties File, the Values from Input Excel File For Further use in While Submitting Request Starts
	    			
	    			String Env_URL_Value = global_Functions.getProperties("API_Env_URL", Selected_TT);
	    			//System.out.println("Environment URL From Properties File Is: "+Env_URL_Value);
	    			RestAssured.baseURI = Env_URL_Value;
	    			RestAssured.basePath = "/api/login";
		    		
	    			//Sending Request Starts
			    		Response resOBJ = (Response) 
			    		RestAssured
			    		.given()
				    		.header("Content-Type", "application/json")
			    			.header("Accept", "*/*")
			    			.body(ReqBody())
			    				    		   			
						.when()
				    		.post()
							
			    		.then() 
			    			.statusCode(200)
			    			.log().all()
			    			.extract()
			    			.response();
		    		//Sending Request Ends
			    //******************API Request Section ***********************************************************************	Ends			    		
	    				
		    		//System.out.println("Response is"+resOBJ);
		    		
		    		//Getting Status Code for Validation Starts
			    		Integer StatusCode_ActualValue = resOBJ.getStatusCode();
			    		System.out.println("Response Status Code Value is: "+StatusCode_ActualValue);			    		
		    		//Getting Status Code for Validation Ends
		    		
		    		//Writing Status Code Actual Value in Excel Starts
			    		w_sOBJ.getRow(rli).getCell(columnNameIndex_OBJ.get("StatusCode_AV")).setCellValue(StatusCode_ActualValue);
			    	//Writing Status Code Actual Value in Excel Starts
			    		
			    	// Status Code Validation and Writing the result to Excel and Report Starts	
				    	if (StatusCode_ActualValue.toString().equals(cn_OBJ.get("StatusCode_EV").toString()))
				    		{
			    				w_sOBJ.getRow(rli).getCell(columnNameIndex_OBJ.get("StatusCode_SV")).setCellValue("Expected and Actual Status Codes are Matched");
			    				w_sOBJ.getRow(rli).getCell(columnNameIndex_OBJ.get("TC_SV")).setCellValue("PASSED");
				    			childChildReportOBJ.info("Test Case: "+cn_OBJ.get("TC_Number")+" Executed Successfully");
				    			childChildReportOBJ.info("Expected Response Code Is: " +cn_OBJ.get("StatusCode_EV").toString()+" And Actual Response Code Is: "+StatusCode_ActualValue);
				    			childChildReportOBJ.info("Request End Point Sent: " +RestAssured.baseURI+RestAssured.basePath);
				    			childChildReportOBJ.info("Request Body Sent: " +ReqBody());
				    			childChildReportOBJ.info("Response Received: " +resOBJ.getBody().asString());
				    			childChildReportOBJ.pass("Test Case: "+cn_OBJ.get("TC_Number")+" Passed");
				    		}
			    		else
				    		{
			    				w_sOBJ.getRow(rli).getCell(columnNameIndex_OBJ.get("StatusCode_SV")).setCellValue("Expected and Actual Status Codes are NOT Matched");
			    				w_sOBJ.getRow(rli).getCell(columnNameIndex_OBJ.get("TC_SV")).setCellValue("FAILED");
				    			childChildReportOBJ.info("Test Case: "+cn_OBJ.get("TC_Number")+" Executed Successfully But Getting Error");
				    			childChildReportOBJ.info("Expected Response Code Is:" +cn_OBJ.get("StatusCode_EV").toString()+" And Actual Response Code Is: "+StatusCode_ActualValue);
				    			childChildReportOBJ.info("Request End Point Sent: " +RestAssured.baseURI+RestAssured.basePath);
				    			childChildReportOBJ.info("Request Body Sent: " +ReqBody());
				    			childChildReportOBJ.info("Response Received: " +resOBJ.getBody().asString());
				    			Assert.fail("Test Case: "+cn_OBJ.get("TC_Number")+" Failed");
				    		}
				    // Status Code Validation and Writing the result to Excel and Report Ends

			    
				    //Writing or Saving Data in an Output File For Functional Tests Starts
			    		String RunResultFolderName_V = global_Functions.getProperties("RunResultFolderName", Selected_TT);
			    		if (gfOBJ.getProperties("testSuiteName", Selected_TT).contains("FT"))
			    		{
			    			File O_filePathOBJ = new File("./FunctionalTestsExecution_Results_And_Reports/"+RunResultFolderName_V+"/"+CalssName_V+"_Output_DF.xlsx");
			    			FileOutputStream fosOBJ = new FileOutputStream(O_filePathOBJ);			    					    			    		
							w_bOBJ.write(fosOBJ);
							
							//Closing output File Starts
								fosOBJ.close();
							//Closing output File Ends
						}
		    		//Writing or Saving Data in an Output File For Functional Tests Ends
			    	//Writing or Saving Data in an Output File For Smoke Tests Starts
				    	else			    		
			    		{
			    			File O_filePathOBJ = new File("./SmokeTestsExecution_Results_And_Reports/"+RunResultFolderName_V+"/"+CalssName_V+"_Output_DF.xlsx");
			    			FileOutputStream fosOBJ = new FileOutputStream(O_filePathOBJ);			    					    			    		
							w_bOBJ.write(fosOBJ);
							
							//Closing output File Starts
								fosOBJ.close();
							//Closing output File Ends
						}
			    	//Writing or Saving Data in an Output File For Smoke Tests Ends
				}
	    	else
		    	{
			    	childChildReportOBJ.info("Test Case: "+cn_OBJ.get("TC_Number")+" Is Skipped");
		    	}
	    	//Test Cases Execution Ends
	    }
		
		w_bOBJ.close();
		fisOBJ.close();
						
	}
	
	//This Method is for REST API Pay load/Request Starts
		public static String ReqBody()
		{
					
			return "{\r\n"
					+ "    \"email\": \""+global_Functions.getProperties("UserName_Value", Selected_TT)+"\",\r\n"
					+ "    \"password\": \""+global_Functions.getProperties("Password_Value", Selected_TT)+"\"\r\n"
					+ "}";
		}
	//This Method is for REST API Pay load/Request Ends
}


