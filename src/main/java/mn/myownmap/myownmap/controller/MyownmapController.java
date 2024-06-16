package mn.myownmap.myownmap.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drew.imaging.ImageProcessingException;

import mn.myownmap.myownmap.service.GeodataService;

@RestController
public class MyownmapController {

	@GetMapping("/getGeodata")
	public void getGeodata() throws ImageProcessingException, IOException {
		GeodataService rtngeoData = new GeodataService();
		rtngeoData.getGeoData();
	}

}
