package mn.myownmap.myownmap.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {

	//read file method
		public String readApiKeyFile(String filename) {
			String apikey = null;
			try {
			      File myObj = new File("C:\\Users\\32kin\\Documents\\apikey\\"+filename);
			      Scanner myReader = new Scanner(myObj);
			      while (myReader.hasNextLine()) {
			    	  apikey = myReader.nextLine();
			      }
			      myReader.close();
			    } catch (FileNotFoundException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			return apikey;
		}
}
