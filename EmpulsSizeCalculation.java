package sizeCalculation;

import java.io.FileInputStream;
import java.io.FileWriter;

import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.opencsv.CSVWriter;

public class EmpulsSizeCalculation {
	WebDriver driver;
	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "/Users/kailash.k/Downloads/chromedriver_mac64 (4)/chromedriver");
		
		driver = new ChromeDriver();
		Date d = new Date();
		System.out.println("Test Execution Date : "+ d.toString());
	}
	@Test
	public void empulsWebsiteImageSizeCal() {
		try {
			
			
			//Reading Page links from local sheet
			
			FileInputStream fs = new FileInputStream("/Users/kailash.k/Documents/kailash_compass_link.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(fs);
			XSSFSheet sheet = wb.getSheetAt(0); 
			int total_Rows = sheet.getLastRowNum();
			int first_row = sheet.getFirstRowNum();
			System.out.println("Total Rows in the sheet = " + total_Rows);
			
			//Writing data into a file
			
			FileWriter output = new FileWriter("/users/kailash.k/Documents/CompassWebsiteImagesCountAndSizes.csv");
			CSVWriter writer = new CSVWriter(output);
			String[] header = {"PageLink","Image Count","Gross size"};
			writer.writeNext(header);
			
			for(int i=first_row;i<=total_Rows;i++) {
				int total_size=0;
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				Row row = sheet.getRow(i);
				Cell cell = row.getCell(0);
				String pageLink = cell.getStringCellValue();
				driver.get(pageLink);
				List<WebElement> imagesList = driver.findElements(By.tagName("img"));
				int total_images = imagesList.size();
				String image_count = Integer.toString(total_images);
				
				
					for (WebElement image : imagesList)
					{
						
						URLConnection urlconnection = new URL(image.getAttribute("src")).openConnection();
						total_size = total_size + urlconnection.getContentLength();
					}
						
			String size_count = Integer.toString(total_size);
			String[] data1 = {pageLink,image_count,size_count};
			writer.writeNext(data1);
			System.out.println("Page link : "+ pageLink);
			System.out.println("Image count : " + total_images );
			System.out.println("Gross size : " + total_size);
			}
			
			wb.close();
			writer.close(); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	@AfterTest
	public void close() {
		driver.quit();
	}

}
