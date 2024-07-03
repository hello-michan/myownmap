package mn.myownmap.myownmap.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import mn.myownmap.myownmap.model.DecimalGeoData;
import mn.myownmap.myownmap.model.GeoData;

public class GeodataService {

	public LatLng getGeoData(String file) throws ImageProcessingException, IOException {
		LatLng location = new LatLng();
		DecimalGeoData decimalGeoDataObj = new DecimalGeoData();
		decimalGeoDataObj.setFileName(file);
		String getFile = Objects.requireNonNull(GeodataService.class.getClassLoader().getResource(file)).getFile();
		File picFile = new File(getFile);
		Metadata metadata = ImageMetadataReader.readMetadata(picFile);
		GeoData geoData = new GeoData();
		for (Directory directory : metadata.getDirectories()) {
			if (!directory.getName().equalsIgnoreCase("gps")) {
				continue;
			}
			for (Tag tag : directory.getTags()) {
				if (tag.getTagName() == "GPS Latitude") {
					geoData.setLatitude(tag.getDescription());
				}
				if (tag.getTagName() == "GPS Latitude Ref") {
					geoData.setLatitudeRef(tag.getDescription());
				}
				if (tag.getTagName() == "GPS Longitude") {
					geoData.setLongitude(tag.getDescription());
				}
				if (tag.getTagName() == "GPS Longitude Ref") {
					geoData.setLongitudeRef(tag.getDescription());
				}
			}
		}
		decimalGeoDataObj.setLatitude(getNormal(geoData.getLatitude(), geoData.getLatitudeRef()));
		decimalGeoDataObj.setLongitude(getNormal(geoData.getLongitude(), geoData.getLongitudeRef()));
		location.lat = getNormal(geoData.getLatitude(), geoData.getLatitudeRef());
		location.lng = getNormal(geoData.getLongitude(), geoData.getLongitudeRef());
		System.out.println(decimalGeoDataObj);
		return location;
	}

	// convert degrees minutes,seconds to decimal
	public Double getNormal(String value, String ref) {
		Double degree = Double.parseDouble(StringUtils.substringBefore(value, "°"));
		Double minute = Double.parseDouble(StringUtils.substringBefore(StringUtils.substringAfter(value, "°"), "'"));
		Double second = Double
				.parseDouble(StringUtils.substringBefore(StringUtils.substringAfter(value, "'"), "\"").trim());
		double dd = Math.signum(degree) * (Math.abs(degree) + (minute / 60.0) + (second / 3600.0));
		if (ref == "S" || ref == "W") {
			dd = dd * -1;
		}
		return dd;
	}

	// get address from latitude and longitude
	public String getAddress(LatLng location) throws ApiException, InterruptedException, IOException {
		String rtn = "";
		GeoApiContext context = new GeoApiContext.Builder().apiKey(readApiKeyFile()).build();
		GeocodingResult[] geocodingapirtn =  GeocodingApi.reverseGeocode(context, location).await();
		rtn = geocodingapirtn[0].formattedAddress;
		context.shutdown();
		return rtn;
	}
	
	private String readApiKeyFile() {
		String apikey = null;
		try {
		      File myObj = new File("C:\\Users\\32kin\\OneDrive\\Documents\\googleApiKey.txt");
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
