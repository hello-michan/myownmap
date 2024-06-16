package mn.myownmap.myownmap.model;

public class GeoData {
	
	private String latitude;
	private String latitudeRef;
	private String longitude;
	private String longitudeRef;
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLatitudeRef() {
		return latitudeRef;
	}
	public void setLatitudeRef(String latitudeRef) {
		this.latitudeRef = latitudeRef;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLongitudeRef() {
		return longitudeRef;
	}
	public void setLongitudeRef(String longitudeRef) {
		this.longitudeRef = longitudeRef;
	}
	@Override
	public String toString() {
		return "GeoData [latitude=" + latitude + ", latitudeRef=" + latitudeRef + ", longitude=" + longitude
				+ ", longitudeRef=" + longitudeRef + "]";
	}
	
}
