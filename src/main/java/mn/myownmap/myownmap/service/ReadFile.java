package mn.myownmap.myownmap.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

import mn.myownmap.myownmap.model.DecimalGeoData;

public class ReadFile {

	// read file method
	public String readApiKeyFile(String filename) {
		String apikey = null;
		try {
			File myObj = new File("C:\\Users\\32kin\\Documents\\apikey\\" + filename);
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

	// Attribute metadata
	public Metadata attributeFile(String file) throws ImageProcessingException, IOException {
		DecimalGeoData decimalGeoDataObj = new DecimalGeoData();
		decimalGeoDataObj.setFileName(file);
		String getFile = Objects.requireNonNull(GeodataService.class.getClassLoader().getResource(file)).getFile();
		File picFile = new File(getFile);
		Metadata metadata = ImageMetadataReader.readMetadata(picFile);
		return metadata;
	}
	
	public Metadata attributeMultFile(MultipartFile file) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
		return metadata;
	}
}
