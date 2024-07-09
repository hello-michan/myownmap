package mn.myownmap.myownmap.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import mn.myownmap.myownmap.model.DecimalGeoData;
import mn.myownmap.myownmap.model.GeoData;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

public class GeodataService {

	//get latitude and longitude from photos 
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
		return location;
	}
	
	//get Objects list
		public void listObjects(S3Client s3, String bucketName) {
			ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();
			ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
            	if(myValue.size()>0)
            		System.out.println(myValue);
            }
	        
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
		ReadFile rd = new ReadFile();
		GeoApiContext context = new GeoApiContext.Builder().apiKey(rd.readApiKeyFile("googleApiKey.txt")).build();
		GeocodingResult[] geocodingapirtn =  GeocodingApi.reverseGeocode(context, location).await();
		rtn = geocodingapirtn[0].formattedAddress;
		context.shutdown();
		return rtn;
	}
	
	

}
