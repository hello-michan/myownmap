package mn.myownmap.myownmap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageProcessingException;
import com.google.maps.errors.ApiException;
import mn.myownmap.myownmap.places.GooglePlaces;
import mn.myownmap.myownmap.places.Place;
import mn.myownmap.myownmap.places.PlacesResult;
import mn.myownmap.myownmap.service.GeodataService;
import mn.myownmap.myownmap.service.ReadFile;

@RestController
public class MyownmapController {

	// get address
	@GetMapping("/getGeodata")
	public String getGeodata(@RequestParam(value = "file") String file)
			throws ImageProcessingException, IOException, ApiException, InterruptedException {
		ReadFile rf = new ReadFile();
		GeodataService rtngeoData = new GeodataService();
		return rtngeoData.getAddress(rtngeoData.getGeoData(rf.attributeFile(file)));
	}

	/*
	 * get business using address
	 * 
	 * @param address
	 * 
	 * @return
	 */
	@GetMapping("/getPlace")
	public ArrayList<String> getPlace(@RequestBody List<String> addresses) {
		ReadFile rf = new ReadFile();
		GooglePlaces gp = new GooglePlaces(rf.readApiKeyFile("googleApiKey.txt"));
		ArrayList<String> rtn = new ArrayList<String>();
		for (String address : addresses) {
			PlacesResult pr = gp.searchText(address);
			for (Place place : pr.asList()) {
				rtn.add(place.getName());
				place.getName();
			}
		}
		;
		return rtn;
	}

	/*
	 * get business using address
	 * 
	 * @param file image
	 * 
	 * @return business
	 */
	@PostMapping("/getPlaceName")
	public String getPlaceFromFile(@RequestParam("image") MultipartFile file)
			throws ImageProcessingException, ApiException, InterruptedException, IOException {
		ReadFile rf = new ReadFile();
		GeodataService rtngeoData = new GeodataService();
		GooglePlaces gp = new GooglePlaces(rf.readApiKeyFile("googleApiKey.txt"));
		PlacesResult pr = gp.searchText(rtngeoData.getAddress(rtngeoData.getGeoData(rf.attributeMultFile(file))));
		for (Place place : pr.asList()) {
			return place.getName();
		}
		return null;

	}

	@PostMapping("/nearby")
	public String nearby(@RequestParam("image") MultipartFile file, @RequestParam(value = "radius") int radius)
			throws ImageProcessingException, ApiException, InterruptedException, IOException {
		ReadFile rf = new ReadFile();
		GeodataService rtngeoData = new GeodataService();
		GooglePlaces gp = new GooglePlaces(rf.readApiKeyFile("googleApiKey.txt"));
		PlacesResult pr = gp.searchNearby(rtngeoData.getGeoData(rf.attributeMultFile(file)), radius);
		for (Place place : pr.asList()) {
			if (place.getTypes().contains("food"))
				System.out.println(place.getName());
		}
		return "NO";
	}

//	@PostMapping("/getPlaceName")
//	public String getPlaceFromFile(@RequestBody List<String> files) throws ImageProcessingException, ApiException, InterruptedException, IOException {
//		ReadFile rf = new ReadFile();
//		GooglePlaces gp = new GooglePlaces(rf.readApiKeyFile("googleApiKey.txt"));
//		ArrayList<String> addresses = new ArrayList<String>();
//		GeodataService rtngeoData = new GeodataService();
//		//get address from picture
//		for(String file:files) {
//			addresses.add(rtngeoData.getAddress(rtngeoData.getGeoData(file)));
//		}
//		//get business from address 
//		for(String address:addresses) {
//			PlacesResult pr = gp.searchText(address);
//			for(Place place: pr.asList()) {
//				System.out.println(place.getName());
//			}
//		};
//		return null;
//	}

//	@GetMapping("/getObjects")
//	public void getObjects(@RequestParam(value = "bucketName") String bucketName) {
//		GeodataService rtngeoData = new GeodataService();
//		Region region = Region.US_EAST_1;
//		S3Client s3 = S3Client.builder().region(region).build();
//		rtngeoData.listObjects(s3, bucketName);
//		s3.close();
//	}
}
