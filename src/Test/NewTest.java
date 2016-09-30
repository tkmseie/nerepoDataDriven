package Test;

import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Properties;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.FileUtils;
import org.openqa.jetty.html.Break;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import Util.TestUtil;
import Util.Xls_Reader;



public class NewTest extends ReusableMethods {
    public static Xls_Reader AAaisxls=null;
	public static Integer counter =0;
	public static Integer totalNoOfGrids=0;
	public static List<WebElement> allGrids;
	public static String titleBeforeClick;
	public static boolean stopTest = false;
	public static Integer clickCount =0;
	public static Properties obj = new Properties(); 
	public static FileInputStream objfile;

	
	/**
	 * This is the main test to be executed. 
	 * This test opens the application in firefox browser and verifies the application is loaded or not
	 * if the application is loaded,it continues with iterating through clicking all the combinations of links
	 * @throws BiffException 
	 */	@Test
		public static void firstTest() throws InterruptedException, IOException, BiffException {
		 
		 
		 objfile = new FileInputStream(System.getProperty("user.dir")+"/src/cofig.properties");
		  obj.load(objfile); 
		 //Creating html file for logging results
		//createdFirstReportPortion();
		//String FilePath = "/Users/senthil-mac/Desktop/Selinum Code/Work Space/Poc_NE_DataDriven/src/URLList.xls";
		try{
        String FilePath = obj.getProperty("ExcelFilePath");

		FileInputStream fs = new FileInputStream(FilePath);
		Workbook wb = Workbook.getWorkbook(fs);

		// TO get the access to the sheet
		Sheet sh = wb.getSheet(obj.getProperty("SheetName"));

		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();
				driver= new FirefoxDriver();


		for (int row = 1; row < totalNoOfRows; row++) {

		String URLValue= sh.getCell(0, row).getContents();
	
		//Opens firefox driver
		
		//Navigating to the application URL
		//driver.get(System.getenv("APPURL"));
		logMessage("Navigating to: "+ URLValue);
		driver.get(URLValue);
		
		//Verifying the logo is loaded or not. If the logo is notloaded the test will not be executed.
		WebElement logoImageObj = findObject(driver, "xpath", "//*[@id=\"page\"]/header/div/hgroup/h2/ul/li[1]/a/img", "Policy Link Logo");
        if(logoImageObj!=null)
        {
        	iterateMainLinks();
        }
        else{
        	logErrorMessage("Error while loading application. Cannot continue the test");
        }	
		}
		}catch(Exception ex)
		{
					//	logErrorMessage(ex.getStackTrace());
						logErrorMessage(ex.getMessage());

		}
	}
	 
	 /**
		 * The following method is used to navigate to each an every links in "Break down" panel
		 */ 
	
	public static void iterateMainLinks() throws IOException, InterruptedException{
		
		List<WebElement> allSumInsuredFields;
		try{
		//Gets all the links in the Breakdown column
		allSumInsuredFields = driver.findElements(By.className("dynamic-dsp-link"));
		System.out.println(allSumInsuredFields.size());
		//If no filters are available verifies the loaded image.
		if (allSumInsuredFields.size()==0)
		{
			titleBeforeClick = "";
			verifyImage();
			clickCombination( );
		}
		//This loop iterates through each link in the breakdown panel and clicks it
		for(WebElement element : allSumInsuredFields)     
		{
		              //Text in the link is stored
			          String linkTextValue = element.getText();
			          //If it is not first link
		              if (counter >0)
		                {
		                   // element.click();
		            	    clickObject(element);
				      		Thread.sleep(8000);
				      		clickCount++;

		                }
		              //If its first link, while page load itslef it would have been clicked. So the link will not be clicked
		              else
		              {
			      		  Thread.sleep(3000);
		              }
				        clickCombination();

		      		counter++;
		}
		}
		catch(Exception ex){
			logErrorMessage(ex.getLocalizedMessage());
		  logErrorMessage("Error while identifying links in Breakdown column. Test stopped.");	
		}
		
	}
	
	 /**
		 * The following method is used to navigate to each an every links in "Break down" panel
		 */ 
	public static void clickCombination( ) throws InterruptedException, IOException{
		
		
        //The following codes finds no of filter panels available 
		WebElement parGrid = findObject(driver, "classname", "dsp-filter-wrap", "Filter Panel");
		
		if(parGrid != null)
		{
		try{
		allGrids = parGrid.findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter"));
		 totalNoOfGrids = allGrids.size();
		 
		        //Iterates through each filter panel and clicks all the combination of filters
				for(WebElement eachGrid : allGrids)  
				{
					findCombination(allGrids.indexOf(eachGrid));
					break; 
				}
		}
		catch(Exception e)
		{
			logErrorMessage("No subfilters available.");
		}
		}
		else
		{
			logErrorMessage("Error while identifying filter panel. Test stopped.");
		}

		
	}
	
	 /**
		 * The following method is used to identify and click all the combination of filters and verifies particular image for every filter is loaded or not
		 *@param currIndex
		 *      This value is the index of a filter panel.
		 */ 

	public static void findCombination(Integer currIndex ) throws InterruptedException, IOException{
		
		int i;
		int j;
		//Identifies the filter panel based on the index
		WebElement currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
		WebElement tempFilterPanel = findObject(driver, "classname", "dsp-filter-wrap", "Filter Panel");
		if(tempFilterPanel != null)
		{
			currentGrid = tempFilterPanel.findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
		//If the identified filter panel is last filter panel, it clicks all the links in that panel and verifies the image loading status
		if (totalNoOfGrids == currIndex+1)
		{
			List<WebElement> allLastlinks = currentGrid.findElements(By.className("atlas-display-exposed-filter"));
			       Integer totalLinkCount = allLastlinks.size();
				    logMessage("1");

					for(i=0; i<totalLinkCount; i++)  
					{
						currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
						
						WebElement getLinkObj = findObjectFromClassNameListByIndex(currentGrid, i, "atlas-display-exposed-filter", "Link in Filter Panel");
						if (getLinkObj != null)
						{
							WebElement titleObject = findObject(driver, "classname", "display-title", "Filter Panel");
							titleBeforeClick = titleObject.getText();
							//getLinkObj.click();
							clickObject(getLinkObj);
							clickCount++;
							//Thread.sleep(4000);
							//Thread.sleep(1000);
							currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
							WebElement spinnerObj = currentGrid.findElement(By.className("atlas-spinner"));
							//Thread.sleep(1000);
							int counterSpinner = 1;
							while(spinnerObj.isDisplayed())
							{
								Thread.sleep(1000);
								counterSpinner++;
								currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
								spinnerObj = currentGrid.findElement(By.className("atlas-spinner"));
							    verifyImage();	
								if (counterSpinner>15)
								{
									logErrorMessage("Image is not started to be loaded.");
									break;
								}
						   }
						else
						{
							logErrorMessage("Error while identifying link in the Filter panel. Test may not be executed for all combinations.");
						}
			        }
         }
		
		//If it is not last filter panel, then it clicks each and every link & again navigates to next panel by calling this function recursively
		else
		{
		    logMessage(currIndex.toString());
			currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
			//List<WebElement> allLinks = currentGrid.findElements(By.className("atlas-display-exposed-filter"));
            logMessage(currentGrid.getText());
			//List<WebElement> allLinks = currentGrid.findElements(By.xpath("//a[contains(@class, '-filter')]"));
            List<WebElement> allLinks = currentGrid.findElements(By.tagName("a"));

			
			Integer totalLinkCount = allLinks.size();

			for(j=0; j<totalLinkCount; j++)  
			{
				currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
				allLinks = currentGrid.findElements(By.tagName("a"));
				
				WebElement getLinkObj = findObjectFromTagListByIndex(currentGrid, j, "a", "Link in Filter Panel");
				if (getLinkObj != null)
				{
					
					//getLinkObj.click();
					clickObject(getLinkObj);
					clickCount++;
					Thread.sleep(8000);
					findCombination(currIndex+1);				
				}
				else
				{
					logErrorMessage("Error while identifying link in the Filter panel. Test may not be executed for all combinations.");
				}				
			}
			
		}
		}
		else
		{
			logErrorMessage("Error while identifying Filter panel. Test may not be executed for all combinations.");
		}
	}
	

	 /**
		 * The following method creates Report.html file in the given report path 
		 *And it writes initial html commands into that file
		 *This method is always executed before any @test method is executed
		 */ 
	@BeforeTest
	public static void createdFirstReportPortion() throws IOException{
		 
		   
		System.out.println("test");
		objfile = new FileInputStream(System.getProperty("user.dir")+"/src/cofig.properties");
		  obj.load(objfile); 
  		 			System.out.println("Screenshot Path: "+obj.getProperty("ScreenshotPath"));
		FileUtils.cleanDirectory(new File(obj.getProperty("ScreenshotPath")));
  
		FC.createNewFile();//Create file.
		  
		  //Writing In to file.
		  //Create Object of java FileWriter and BufferedWriter class.
		  FileWriter FW = new FileWriter(TestFile);
		   BW = new BufferedWriter(FW);
		  BW.write("<html>"); //Writing In To File.
		  BW.newLine();//To write next string on new line.
		  BW.write("<body>"); //Writing In To File.
		  BW.write("<ul>"); //Writing In To File.

	}
	
	 /**
	 * The following method writes html commands to close the Report.html file
	 *This method is always executed after any @test method is executed
	 */ 
	@AfterTest
	public static void closeHTML() throws IOException
	{
		BW.write("</ul>"); //Writing In To File.Å
		BW.write("</body>"); //Writing In To File.
		  BW.write("</html>"); //Writing In To File.
		  BW.close();	
	}
	
	 /**
	 * The following method verifies the images is loaded or not after a particular combination of filter is clicked
	 * Based on the configured load time values, it prints log in a particular color
	 */ 
	public static void verifyImage() throws InterruptedException, IOException
	{
		int timerCount ;
		//Identifies the Image object
		WebElement imageObject = driver.findElement(By.tagName("figure"));
		//Identifies the title object
		WebElement titleObject = driver.findElement(By.className("display-title"));
		//After clicking the filter links the title should be changed. If not changed, it may be a network error. So this condition ensures the title is changed
        if (!compareStrings(titleBeforeClick, titleObject.getText()))
        {
        //After clicking the filter links, the script waits for 4 seconds. Now here the timer starts to count from 5th second.
		for (timerCount=4; timerCount<15; timerCount++)
		{
			List<WebElement> divElements = imageObject.findElements(By.tagName("div"));
            if (divElements.size() !=0)
              {
    			try{
            	WebElement nodataObject = imageObject.findElement(By.className("no-data"));
            	   logErrorMessage("No data message shown for "+titleObject.getText()+ " Link.");
  			       BW.write(" <li><font color='orange'>No data message shown for "+titleObject.getText()+ " Link<font></li>");    			}catch(Exception ex){
    				
    			}
            	break;

              }
            else
              {
 
		        Thread.currentThread().sleep(1000);
             }  
		}
		//Takes screenshot for all filter combinations
		screenShot(titleObject.getText());
		if(timerCount < Integer.parseInt(obj.getProperty("BestMaximumLoadTim")) )
		{
			System.out.println("Image is Loaded for "+titleObject.getText()+ " Link.");
			  BW.write(" <li><font color='"+ obj.getProperty("BestLoadTimeColor")+"'>"+titleObject.getText()+ "Link<font></li>"); //Writing In To File.

		}
		else if(timerCount>=Integer.parseInt(obj.getProperty("BetterMiniumLoadTime")) && timerCount<Integer.parseInt(obj.getProperty("BetterMaximumLoadTime")))
		{
			System.out.println("Image is Loaded for "+titleObject.getText()+ " Link.");
			  BW.write(" <li><font color='"+obj.getProperty("BetterLoadTimeColor") +"'>"+titleObject.getText()+ " Link<font></li>"); //Writing In To File.

		}
		else if(timerCount>=Integer.parseInt(obj.getProperty("GoodMiniumLoadTime")) && timerCount<Integer.parseInt(obj.getProperty("GoodMaximumLoadTime")))
		{
			System.out.println("Image is Loaded for "+titleObject.getText()+ " Link.");
			  BW.write(" <li><font color='"+ obj.getProperty("GoodLoadTimeColor")+"'>"+titleObject.getText()+ " Link<font></li>"); //Writing In To File.

		}
		else
		{
			logErrorMessage("Failed: Image is Loaded Not  for "+titleObject.getText()+ " Link.");
			  BW.write(" <li><font color='"+obj.getProperty("ErrorColor") +"'>Failed: "+titleObject.getText()+ " Link<font></li>"); //Writing In To File.

		}
        }else{
        	logErrorMessage("Failed: Unable to communicate to the server. Plesae check network connectivity");
			BW.write(" <li><font color='"+obj.getProperty("ErrorColor") +"'>Failed: Unable to communicate to the server. Plesae check network connectivity<font></li>"); //Writing In To File.
         //   Thread.currentThread().stop();
        }
		
	}
	
	 /**
		 * The following method compares two strings and returns the result
		 * @return If the strings are identical returns true, else returns false.
		 */ 
	public static boolean compareStrings(String a, String b)
	{
       //If both the strings are identical returns true
		if(a.equals(b) & clickCount >1)
		{
			return true;
		}
		//else returns false
		else
		{
			return false;
		}
	}
	 /**
		 * The following method is used to take screenshot of the current page in the webdriver and stores the file in the given path
		 * This stroes file in jpg format
		 */ 
	 public static void screenShot(String fileNameValue) throws IOException{
		    fileNameValue = fileNameValue.replace("/", "_");
	        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		    FileUtils.copyFile(scrFile, new File(obj.getProperty("ScreenshotPath")+fileNameValue+".jpg"));		 
	 }
	 
	 public static void clickObject (WebElement objectToClick) throws IOException{
		 try{
			 logMessage(objectToClick.getText() +" is clickked.");
			 objectToClick.click();
		 }
		 catch(Exception ex){
			 logErrorMessage("Error while clicking the Link");
		 }
	 }
	 
	 @DataProvider
		public Object[][] getTestData() {
	        AAaisxls = new Xls_Reader("/Users/senthil-mac/Desktop/Selinum Code/Work Space/Poc_NE_DataDriven/src/URLList.xlsx");
			return TestUtil.getData(AAaisxls, "Sheet1");
		}
}
