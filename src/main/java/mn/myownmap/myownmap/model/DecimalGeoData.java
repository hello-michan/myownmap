package mn.myownmap.myownmap.model;

public class DecimalGeoData {
	
	private String fileName;
	private double latitude;
	private double longitude;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "DecimalGeoData [fileName=" + fileName + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
