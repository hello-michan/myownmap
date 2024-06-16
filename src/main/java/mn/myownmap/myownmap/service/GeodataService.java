package mn.myownmap.myownmap.service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;


import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import mn.myownmap.myownmap.model.GeoData;

public class GeodataService {

	public void getGeoData() throws ImageProcessingException, IOException {
		String file = Objects.requireNonNull(GeodataService.class.getClassLoader().getResource("IMG_4243.HEIC"))
				.getFile();
		File jpegFile = new File(file);
		Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);
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
		System.out.println(geoData);
		System.out.println(getNormal(geoData.getLatitude(), geoData.getLatitudeRef()));
		System.out.println(getNormal(geoData.getLongitude(), geoData.getLongitudeRef()));
	}

	public Double getNormal(String value, String ref){
	    Double degree = Double.parseDouble(StringUtils.substringBefore(value,"°"));
	    System.out.println(degree);
	    Double minute = Double.parseDouble(StringUtils.substringBefore(StringUtils.substringAfter(value,"°"),"'"));
	    System.out.println(minute);
	    Double second = Double.parseDouble(StringUtils.substringBefore(StringUtils.substringAfter(value,"'"),"\"").trim());
	    System.out.println(second);
	    //	    Double second = Double.parseDouble((StringUtils.substringAfter(value,"'")).trim());
	    double dd = Math.signum(degree) * (Math.abs(degree) + (minute / 60.0) + (second / 3600.0));
	    if(ref == "S" || ref == "W") {
	    	dd = dd*-1;
	    }
	    return dd;
	    	}
}
