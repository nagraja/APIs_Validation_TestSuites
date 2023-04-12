package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class excelUtilities 
{
	public static String ProjectRelativePath_Value;
	public static XSSFWorkbook wbOBJ;
	public static XSSFSheet wsOBJ;
	public static XSSFCell cell;
	public static excelUtilities excelOBJ;
	
	
	public excelUtilities(String excelPath, String sheetName)
	{
		
		try {
				FileInputStream fis = new FileInputStream(excelPath);
				wbOBJ = new XSSFWorkbook(fis);
				wsOBJ = wbOBJ.getSheet(sheetName);
			} 
		catch (IOException e) 
			{
				e.printStackTrace();
			}		
	}
	
//	public excelUtilities ReadExcel_Fun(String FileName, String SheetName)
//	{
//		//Excel Setup Starts
//			ProjectRelativePath_Value = System.getProperty("user.dir");
//			excelOBJ = new excelUtilities(ProjectRelativePath_Value+"\\src\\test\\resources\\testdata_Files\\"+FileName, SheetName);
//		//Excel Setup Ends
//		return excelOBJ;
//	}
	
	public static void WriteData_Fun(String excelPath) throws IOException
	{
		FileOutputStream writeExcelOBJ = new FileOutputStream(new File(excelPath));
		wbOBJ.write(writeExcelOBJ);
	}
	
	public static int GetRowCount()
	{
		int RowCount_Value = 0;
		RowCount_Value = wsOBJ.getPhysicalNumberOfRows();
		//System.out.println("Row Count Is: "+RowCount_Value);
		return RowCount_Value;
		
	}
	
	public static int GetColumnCount()
	{
		int ColumnCount_Value = 0;
		ColumnCount_Value = wsOBJ.getRow(0).getLastCellNum();
		//System.out.println("Row Count Is: "+ColumnCount_Value);
		return ColumnCount_Value;
	}
	
	public static String GetCellData(int rowNum, int colNum)
	{
		//int ColumnCountValue = wsOBJ.getRow(rowNum).getPhysicalNumberOfCells();
		//System.out.println("Column Count Is: "+ColumnCountValue);
		
		String CellData_Value;
		//CellData_Value = wsOBJ.getRow(rowNum).getCell(colNum).toString();
		CellData_Value = wsOBJ.getRow(rowNum).getCell(colNum).getStringCellValue();
		System.out.println("Column Value Is: "+CellData_Value);
		return CellData_Value;
	}
	
	
}
