package Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ReusableMethods {
	
	
	public static WebDriver driver;
	public static String TestFile = "/Users/Shared/Jenkins/Home/workspace/JavaJob/src/Report.html";
	public static File FC = new File(TestFile);//Created object of java File class.
	public static BufferedWriter BW;

	
	/**
	 * This method identifies appropriate method for object identification. 
	 * For example , if you want to identify the object with "id",it will call
	 * findObjectById method to identify the object.
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  driver  (WebDriver) WebDriver object on which we need to search the element
	 * @param  attr    (String) This is the attribute value using which we will identify the object.
	 *                 Possible values are "id", "class", "name","xpath", "class_contains"
	 * @param  attrVal (String) This is the value of the attribute                 
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	public static WebElement findObject(WebDriver driver, String attr, String attrVal, String objName, String... tagName)throws IOException{
	    
		switch(attr){
		//identifies object by id value
		case "id": return findObjectById(driver, attrVal, objName); 
		
		//identifies object by xpath value
		case "xpath": return findObjectByxPath(driver, attrVal, objName); 
		
		//identifies object by link text value
		case "linkText": return findObjectBylinkText(driver, attrVal, objName);
		
		//identified object by class name
		case "classname" : return findObjectByclassName(attrVal,objName);
		
		//identified object by  name
		case "name" : return findObjectByName(driver, attrVal,objName);
		
		default: logErrorMessage("The given attribute value " + attr + " may be invalid or it is not yet programmed. Please verify common_Functions.findObject method");
			     return null;
		}
				
	}
	
	/**
	 * This method identifies the object using "id" value
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  driver  (WebDriver) WebDriver object on which we need to search the element
	 * @param  idVal    (String) This is value of the "id" attribute of the object
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	public static WebElement findObjectById(WebDriver driver, String idVal, String objName)throws IOException{
		//Identifies the element & if its available it returns it
		try{
		WebElement webObj = driver.findElement(By.id(idVal));
		logMessage("The Object " + objName + " is available.");
		return webObj;
		}catch (Exception ex)
		{
			logErrorMessage("The Object " + objName + " is not available.");
			return null;
		}
		
	
	}
	
	/**
	 * This method identifies list of elements using class name and then returns a particular element based on index value
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  parObject  Parent object inwhich the list of elements to be identified
	 * @param  indexVal    Index value of which object to be returned
	 * @param  classNameValue  Classname value to be used for object identificatoin
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	public static WebElement findObjectFromClassNameListByIndex(WebElement parObject, Integer indexVal, String classNameValue, String ObjectName ) throws IOException{
		
		try{
			List<WebElement> tempAllLinks = parObject.findElements(By.className(classNameValue));
			WebElement webObj = tempAllLinks.get(indexVal);
			logMessage("The Object " + ObjectName + " is available.");
			return webObj;
			}catch (Exception ex)
			{
				logErrorMessage("The Object " + ObjectName + " is not available.");
				return null;
			}
		
	}
	
	
	/**
	 * This method identifies the object using "xpath" value
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  driver  (WebDriver) WebDriver object on which we need to search the element
	 * @param  xpathVal    (String) This is value of the "id" attribute of the object
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	public static WebElement findObjectByxPath(WebDriver driver, String xpathVal, String objName)throws IOException{
		//Identifies the element & if its available it returns it
		try{
		WebElement webObj = driver.findElement(By.xpath(xpathVal));
		logMessage("The Object " + objName + " is available.");
		return webObj;
		}catch (Exception ex)
		{
			logErrorMessage("The Object " + objName + " is not available.");
			return null;
		}
		
	
	}
	
	/**
	 * This method identifies the object using "xpath" value
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  driver  (WebDriver) WebDriver object on which we need to search the element
	 * @param  xpathVal    (String) This is value of the "id" attribute of the object
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	public static WebElement findObjectBylinkText(WebDriver driver, String linkValue, String objName)throws IOException{
		//Identifies the element & if its available it returns it
		try{
		WebElement webObj = driver.findElement(By.linkText(linkValue));
		logMessage("The Object " + objName + " is available.");
		return webObj;
		}catch (Exception ex)
		{
			logErrorMessage("The Object " + objName + " is not available.");
			return null;
		}
		
	
	}
	
	/**
	 * This method identifies the object using "classname" value
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  Value  class name value
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	public static WebElement findObjectByclassName(String Value, String objName)throws IOException{
		//Identifies the element & if its available it returns it
		try{
		WebElement webObj = driver.findElement(By.className(Value));
		logMessage("The Object " + objName + " is available.");
		return webObj;
		}catch (Exception ex)
		{
			logErrorMessage("The Object " + objName + " is not available.");
			return null;
		}
		
	
	}
	
	
	/**
	 * This method identifies the object using "Name" value
	 * <p>
	 * This method always returns the WebElement (i.e Object) if it is available.
	 * If the object is not available it returns null.
	 * @param  Value   name value
	 * @param  objName (String) This is name of object & it will be used to log messages               
	 * @return          If the object is identified it return the object as WebElement
	 *                  else it will return null
	 * 
	 */
	 public static WebElement findObjectByName(WebDriver driver, String idVal, String objName) throws IOException{
			// Identifies the element & if its available it returns it
			try {
				WebElement webObj = driver.findElement(By.name(idVal));
				logMessage("The Object " + objName + " is available.");
				return webObj;
			} catch (Exception ex) {
				logErrorMessage("The Object " + objName + " is not available.");
				return null;
			}
	 }
	 
	 /**
		 * This method is used to log message in console output
		 * @param  messageToLog   Message to be entered into console output
		 * 
		 */
	 public static void logMessage(String messageToLog){
			try{
				
				System.out.println(messageToLog);
				
			}catch(Exception e){
				
			}
		}
	 
	 /**
		 * This method is used to log error message in console output.   
		 * This method additionally writes the message into HTML report in redcolor. Also stores screenshot in screenshot folder.
		 * @param  messageToLog   Message to be entered into console output
		 * 
		 */
	 
	 public static void logErrorMessage(String messageToLog) throws IOException
		{		
			 System.err.println(messageToLog);	
             Date now= new Date();
		     DateFormat converter = new SimpleDateFormat("DD/MM/YYYY:HH:mm:ss");		  
		     String dateTime1 = converter.format(now);
			 BW.write(" <li><font color='red'>Failed: "+messageToLog+ ".Please refer the screenshot "+ dateTime1 +".jpg <font></li>"); //Writing In To File.
	         NewTest.screenShot(dateTime1);
		}
}
