package mn.myownmap.myownmap.places;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStreamReader;

public class GooglePlaces {
	
	private String apikey;
	private Gson gson;
	private HttpClient client;
	
	private static final String TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";
	
	private static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	
	public GooglePlaces(String apikey) {
		this( HttpClientBuilder.create().useSystemProperties().build(), apikey);
	}

	public GooglePlaces(HttpClient client, String apikey) {
		this.apikey = apikey;
		
		GsonBuilder gb = new GsonBuilder();
		gb.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
		this.gson = gb.create();
		
		this.client = client;
	}
	
	
	private PlacesResult convertResponse(HttpResponse response) throws JsonSyntaxException, JsonIOException, UnsupportedOperationException, IOException {
		InputStreamReader inputSR = new InputStreamReader(response.getEntity().getContent());
		return gson.fromJson(inputSR, PlacesResult.class);
	}
	
	public PlacesResult searchText(String text) {
		try {
			URIBuilder url = new URIBuilder(TEXT_SEARCH_URL);
			url.addParameter("key", apikey);
			url.addParameter("query", text);
			HttpGet hg = new HttpGet(url.build());
			return convertResponse(client.execute(hg));
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public PlacesResult searchNearby( LatLng location, int radius ) {
		try {
			URIBuilder url = new URIBuilder( NEARBY_SEARCH_URL );
			url.addParameter( "key", this.apikey );
			url.addParameter( "location", location.lat + "," + location.lng );
			url.addParameter( "radius", String.valueOf(radius));
						
			HttpGet get = new HttpGet( url.build( ) );
			return this.convertResponse( this.client.execute( get ) );
			
		} catch( Exception e ) {
			System.out.println(e);
			return null;
		}
	}
}
