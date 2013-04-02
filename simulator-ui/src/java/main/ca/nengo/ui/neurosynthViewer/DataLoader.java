package ca.nengo.ui.neurosynthViewer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

enum DataType {
	REVERSE_INFERENCE,
	FORWARD_INFERENCE,
	POSTERIOR_PROBABILITY;
	
	@Override
	public String toString() {
		switch (this) {
		case REVERSE_INFERENCE: 	return "Reverse Inference";
		case FORWARD_INFERENCE:		return "Forward Inference";
		case POSTERIOR_PROBABILITY: return "Posterior Probability";
		default: 					return "";
		}
	}
}

class DataLoader {
	private static String SCHEME = "http";
	private static String AUTHORITY = null;
	private static String PATH = "//www.neurosynth.org/terms/ajax_image";
	private static String FRAGMENT = null;
	
	private JSONParser parser = new JSONParser();

	// returns the activation data
	// returns null on failure to fetch data
	// possible reasons for failure:
	// 		- no Internet connection
	//		- term does not exist in the database
	// 		- Neurosynth web site changed their encoding format
	Data load(String term, DataType type) {
		URL url = createURL(term, type);
		if (url == null) {
			throw new IllegalArgumentException("Unable to create URL from parameters.");
		}
		
		JSONObject json = fetchJSON(url);
		if (json == null) {
			return null;
		}
		
		Data data = createData(json);
		if (data == null) {
			throw new IllegalArgumentException("Unable to create data object from JSON.");
		}
		
		return data;
	}
	
	private URL createURL(String term, DataType type) {
		String typeArg;
		switch (type) {
		case REVERSE_INFERENCE: typeArg = "ri"; break;
		case FORWARD_INFERENCE: typeArg = "fi"; break;
		case POSTERIOR_PROBABILITY: typeArg = "pp"; break;
		default:
			System.err.println("Unsupported Data Type: " + type);
			return null;
		}
		
		String query = "type=" + typeArg + "&term=" + term;
		try {
			URI uri = new URI(SCHEME, AUTHORITY, PATH, query, FRAGMENT);
			return uri.toURL();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private JSONObject fetchJSON(URL url) {
		InputStream stream;
		try {
			stream = url.openStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			GZIPInputStream unzipper = new GZIPInputStream(stream);
			InputStreamReader input = new InputStreamReader(unzipper);
			JSONObject object = (JSONObject) parser.parse(input);
			input.close();
			return object;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private Data createData(JSONObject json) {
		int[] ranges = Data.getRanges();
		int yRange = ranges[1];
		int zRange = ranges[2];
		int yzRange = yRange * zRange;

		try {
			Number min = (Number) json.get("min");
			Number max = (Number) json.get("max");
			Data data = new Data(min.floatValue(), max.floatValue());
			
			JSONArray indicesArr = (JSONArray) json.get("inds");
			JSONArray values = (JSONArray) json.get("vals");
			
			@SuppressWarnings("unchecked")
			Iterator<Number> vite = values.iterator();
			@SuppressWarnings("unchecked")
			Iterator<JSONArray> aite = indicesArr.iterator();
			
			while (vite.hasNext() && aite.hasNext()) {
				Number numValue = vite.next();
				float value = numValue.floatValue();
				
				JSONArray indices = aite.next();
				@SuppressWarnings("unchecked")
				Iterator<Number> iite = indices.iterator();
				while (iite.hasNext()) {
					Number numIndex = iite.next();
					
					// the logic here is copied from examining the script on
					// NeuroSynth.com, so I can't really explain some things
					
					// why subtract 1?
					int index = numIndex.intValue() - 1;
					
					// x and z are swapped when compared to the coordinate
					// system used in the rest of the code
					int x = (index / yzRange);
					int y = (index - (x * yzRange)) / zRange;
					int z = index - (x * yzRange) - (y*zRange);

					// TODO: change to data coordinates to x y z
					data.setValue(x, y, z, value);
				}
			}
			return data;
			
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}
}
