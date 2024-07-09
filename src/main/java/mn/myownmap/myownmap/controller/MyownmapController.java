package mn.myownmap.myownmap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.drew.imaging.ImageProcessingException;
import com.google.maps.errors.ApiException;


import mn.myownmap.myownmap.places.GooglePlaces;
import mn.myownmap.myownmap.places.Place;
import mn.myownmap.myownmap.places.PlacesResult;
import mn.myownmap.myownmap.service.GeodataService;
import mn.myownmap.myownmap.service.ReadFile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@RestController
public class MyownmapController {

	@GetMapping("/getGeodata")
	public String getGeodata(@RequestParam(value = "files") String files) throws ImageProcessingException, IOException, ApiException, InterruptedException {
		GeodataService rtngeoData = new GeodataService();
		return rtngeoData.getAddress(rtngeoData.getGeoData(files));
	}
	
	@GetMapping("/getObjects")
	public void getObjects(@RequestParam(value = "bucketName") String bucketName)  {
		GeodataService rtngeoData = new GeodataService();
		Region region = Region.US_EAST_1;
	    S3Client s3 = S3Client.builder().region(region).build();
	    rtngeoData.listObjects(s3,bucketName);  
	    s3.close();
	}
	
	@PostMapping("/postGeodata")
	public ArrayList<String> postGeodata(@RequestBody List<String> files) throws ImageProcessingException, IOException, ApiException, InterruptedException {
		ArrayList<String> result = new ArrayList<String>();
		GeodataService rtngeoData = new GeodataService();
		for(String file:files) {
			result.add(rtngeoData.getAddress(rtngeoData.getGeoData(file)));
		}
		return result;		
	}
	
	@GetMapping("/getPlace")
	public String getPlace(@RequestBody List<String> addresses) {
		ReadFile rf = new ReadFile();
		GooglePlaces gp = new GooglePlaces(rf.readApiKeyFile("googleApiKey.txt"));
		
		for(String address:addresses) {
			PlacesResult pr = gp.searchText(address);
			for(Place place: pr.asList()) {
				System.out.println(place.getName());
			}
		};
		return null;
	}
	
	@GetMapping("/getPlaceName")
	public String getPlaceFromFile(@RequestBody List<String> files) throws ImageProcessingException, ApiException, InterruptedException, IOException {
		ReadFile rf = new ReadFile();
		GooglePlaces gp = new GooglePlaces(rf.readApiKeyFile("googleApiKey.txt"));
		ArrayList<String> addresses = new ArrayList<String>();
		GeodataService rtngeoData = new GeodataService();
		//get address from picture
		for(String file:files) {
			addresses.add(rtngeoData.getAddress(rtngeoData.getGeoData(file)));
		}
		//get business from address 
		for(String address:addresses) {
			PlacesResult pr = gp.searchText(address);
			for(Place place: pr.asList()) {
				System.out.println(place.getName());
			}
		};
		return null;
	}

}
