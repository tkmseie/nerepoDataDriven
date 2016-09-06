package Util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class TestUtil {

	// finds if the test suite is runnable 
	public static boolean isSuiteRunnable(Xls_Reader xls , String suiteName){
		boolean isExecutable=false;
		//System.out.println(xls.getRowCount("App Name"));
		for(int i=2; i <= xls.getRowCount("App Name") ;i++ ){
			String suite = xls.getCellData("App Name", "Application Name", i);
			String runmode = xls.getCellData("App Name", "Run Mode", i);
		
			if(suite.equalsIgnoreCase(suiteName)){
			
				if(runmode.equalsIgnoreCase("Y")){
					isExecutable=true;
				}else{
					isExecutable=false;
				}
			}

		}
		xls=null; // release memory
		return isExecutable;
		
	}

	
	// returns true if runmode of the test is equal to Y
		public static boolean isTestCaseRunnable(Xls_Reader xls, String testCaseName){
			boolean isExecutable=false;
					//System.out.println(xls.getRowCount("TestNames"));
			for(int i=2; i<= xls.getRowCount("TestNames") ; i++){
				//String tcid=xls.getCellData("Test Cases", "TCID", i);
				//String runmode=xls.getCellData("Test Cases", "Runmode", i);
				//System.out.println(tcid +" -- "+ runmode);
			
				if(xls.getCellData("TestNames", "Test Name", i).equalsIgnoreCase(testCaseName)){
					if(xls.getCellData("TestNames", "Run Mode", i).equalsIgnoreCase("Y")){
						isExecutable= true;
					}else{
						isExecutable= false;
					}
				}
			}
			
			//xls = null;
			return isExecutable;
			
		}
		
		// return the test data from a test in a 2 dim array
		public static Object[][] getData(Xls_Reader xls , String testCaseName){
			// if the sheet is not present
			if(! xls.isSheetExist(testCaseName)){
				xls=null;
				return new Object[1][0];
			}
			
			
			int rows=xls.getRowCount(testCaseName);
			int cols=xls.getColumnCount(testCaseName);
			//System.out.println("Rows are -- "+ rows);
			//System.out.println("Cols are -- "+ cols);
			
		    Object[][] data =new Object[rows-1][cols];
			for(int rowNum=2;rowNum<=rows;rowNum++){
				for(int colNum=0;colNum<cols;colNum++){
					//System.out.print(xls.getCellData(testCaseName, colNum, rowNum) + " -- ");
					data[rowNum-2][colNum] = xls.getCellData(testCaseName, colNum, rowNum);
				}
				//System.out.println();
			}
			return data;
			
		}
		
		// update results for a particular data set	
		public static void reportDataSetResult(Xls_Reader xls, String testCaseName, int rowNum,String result, String conf,String Comments){	
		  
			xls.setCellData(testCaseName, "Results", rowNum, result);
			xls.setCellData(testCaseName, "Confirmation Number", rowNum, conf);
			xls.setCellData(testCaseName, "Comments", rowNum, Comments);
		}
		
		
		
         //Get date from today
		 public static  String getDatefromToday(int  days) {
			 
			   DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			   	 
			   //get current date time with Calendar()
			   Calendar cal = Calendar.getInstance();
			   cal.add(Calendar.DATE, days);
			   //System.out.println(dateFormat.format(cal.getTime()));
			  
			   
			   return dateFormat.format(cal.getTime());
		 
		  }
	
		// returns true if runmode of the test is equal to Y
			public static int getRowNum(Xls_Reader xls, String sheetName, String id){
				
				for(int i=2; i<= xls.getRowCount(sheetName) ; i++){
					String tcid=xls.getCellData(sheetName, "Test Name", i);
					if (tcid.equals(id)){
						xls = null;
						return i;
					}
				}
				return -1;
			}
			
			public static void selectBypartText(Select listObj, String partText){
				
				 List<WebElement> list = listObj.getOptions();
				 for (WebElement option : list) {
				 String fullText = option.getText();
				 if (fullText.contains(partText)) {
					 listObj.selectByVisibleText(fullText);
				 }
				 }
			}
			
			 public static void clickVisbleobject(String xPathval, WebDriver driver){
				 //String xPathval = "//a[contains(@class,'_seat_" + SeatstrArr[i] + "')]";
				 List<WebElement> allSeats= driver.findElements(By.xpath(xPathval));
				 for(WebElement allseatObj : allSeats)
		         {
		               if(allseatObj.isDisplayed())
		                {
		            	   allseatObj.click();
		                 }
		   
		           }
				 
				 
				 
			 }
			 
			 public static void takeScreenshot(WebDriver driver) throws IOException{
					
				    
				    File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				    FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//src//com//AA//xls//screenshot.png"));
				}


		
}
