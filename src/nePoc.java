import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.jetty.html.Break;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;


public class nePoc {
	
	public static WebDriver driver;
	public static Integer counter =0;
	public static Integer totalNoOfGrids=0;
	public static List<WebElement> allGrids;
	
	//public static void main(String[] args) throws InterruptedException, IOException {
	@Test
		public static void firstTest() throws InterruptedException, IOException {


		driver= new FirefoxDriver();
		driver.get("http://nationalequityatlas.org/indicators/Wages%3A_Median/By_ancestry%3A40211/United_States/false/Race_ethnicity%3ABlack/Nativity%3AAll_people/Year%3A2012");
		
		List<WebElement> allSumInsuredFields = driver.findElements(By.className("dynamic-dsp-link"));
		for(WebElement element : allSumInsuredFields)     
		{
		              String linkTextValue = element.getText();
		              if (counter >0)
		                {
		                    element.click();
				      		Thread.sleep(8000);
					        clickCombination();

		                }
		              else
		              {
			      		  Thread.sleep(3000);
		              }
		      		counter++;

		}

	}
	
	public static void clickCombination( ) throws InterruptedException, IOException{
		
		//Find no of grids
		//dsp-filter-wrap
		//System.out.println("Came click combination");

		WebElement parGrid = driver.findElement(By.className("dsp-filter-wrap"));
		allGrids = parGrid.findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter"));
		//System.out.println(allGrids.size()) ;
		 totalNoOfGrids = allGrids.size();
				for(WebElement eachGrid : allGrids)  
				{
					//System.out.println(allGrids.indexOf(eachGrid));
					findCombination(allGrids.indexOf(eachGrid));
					//System.out.println("******");
					break; 
				}

		
	}

	public static void findCombination(Integer currIndex ) throws InterruptedException, IOException{
		
		int i;
		int j;
		WebElement currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
		//System.out.println(currIndex + " is current index");
		if (totalNoOfGrids == currIndex+1)
		{
		  //atlas-display-exposed-filter 
			List<WebElement> allLastlinks = currentGrid.findElements(By.className("atlas-display-exposed-filter"));
			       Integer totalLinkCount = allLastlinks.size();
					for(i=0; i<totalLinkCount; i++)  
					{
						currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
						allLastlinks = currentGrid.findElements(By.className("atlas-display-exposed-filter"));					
						
						allLastlinks.get(i).click();
						Thread.sleep(8000);
						verifyImage();
						
			        }
         }
		else
		{
			currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
			List<WebElement> allLinks = currentGrid.findElements(By.className("atlas-display-exposed-filter"));
			Integer totalLinkCount = allLinks.size();
			for(j=0; j<totalLinkCount; j++)  
			{
				currentGrid = driver.findElement(By.className("dsp-filter-wrap")).findElements(By.cssSelector(".atlas-grey-wrapper-small.atlas-display-filter")).get(currIndex);
				allLinks = currentGrid.findElements(By.className("atlas-display-exposed-filter"));
				//System.out.println(allLinks.get(j).getText() + "***");
				allLinks.get(j).click();
				Thread.sleep(8000);
				findCombination(currIndex+1);
			}
			
		}
	}
	
	public static void verifyImage() throws InterruptedException, IOException
	{
		int timerCount ;
		WebElement imageObject = driver.findElement(By.tagName("figure"));
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//System.out.println(allLastlinks.get(i).getText());
		WebElement titleObject = driver.findElement(By.className("display-title"));
	    FileUtils.copyFile(scrFile, new File("/Users/senthil-mac/SeleniumScreenshot/"+titleObject.getText()+".png"));

		for (timerCount=0; timerCount<15; timerCount++)
		{
			List<WebElement> divElements = imageObject.findElements(By.tagName("div"));
			System.out.println("test");
            if (divElements.size() !=0)
              {
            	 break;
          	     //System.out.println("Image is Loaded for "+titleObject.getText()+ " Link.");
              }
            else
              {
          	   //System.out.println("Image is Not Loaded for "+titleObject.getText()+ " Link.");
            	Thread.sleep(1000);
                

             }  
		}
		
		if(timerCount<5)
		{
			System.out.println("\u001B[31mImage is Loaded for "+titleObject.getText()+ " Link.\u001B[31m");
		}
		else if(timerCount>=5 && timerCount<8)
		{
			System.out.println("<Yellow>Image is Loaded for "+titleObject.getText()+ " Link.</Yellow>");
		}
		else if(timerCount>=8 && timerCount<15)
		{
			System.out.println("<Red>Image is Loaded for "+titleObject.getText()+ " Link.</Red>");
		}
		else
		{
			System.out.println("Failed: Image is Loaded Not  for "+titleObject.getText()+ " Link.");
		}
		
	}
}
