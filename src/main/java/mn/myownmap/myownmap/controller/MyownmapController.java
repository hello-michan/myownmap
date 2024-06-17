package mn.myownmap.myownmap.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drew.imaging.ImageProcessingException;

import mn.myownmap.myownmap.service.GeodataService;

@RestController
public class MyownmapController {

	@GetMapping("/getGeodata")
	public void getGeodata(@RequestParam(value = "files") String files) throws ImageProcessingException, IOException {
		GeodataService rtngeoData = new GeodataService();
		rtngeoData.getGeoData(files);
	}
	
	@PostMapping("/postGeodata")
	public void postGeodata(@RequestBody List<String> files) throws ImageProcessingException, IOException {
		GeodataService rtngeoData = new GeodataService();
		for(String file:files) {
			rtngeoData.getGeoData(file);
		}
	}

}
