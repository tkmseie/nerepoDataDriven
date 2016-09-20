package Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.james.mime4j.field.datetime.DateTime;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Testscripts {
	
	 public static void main(String[] args) throws IOException {
		 
		 int i;

		 while (true)
		 {
             Date now= new Date(); 
			 DateFormat converter = new SimpleDateFormat("DD/MM/YYYY:HH:mm:ss");		  
			     String dateTime1 = converter.format(now);
		     System.out.println(dateTime1);
		     try{
		        Thread.currentThread().sleep(1000);
		     }catch(Exception ex){ }
		  }
	 }


}
