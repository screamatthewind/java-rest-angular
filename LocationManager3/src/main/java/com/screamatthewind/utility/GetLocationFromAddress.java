package com.screamatthewind.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.screamatthewind.core.Address;

public class GetLocationFromAddress {

	public static List<Address> findAddress(String address) {
	
		String json = "";
		
	    address = address.replaceAll(" ", "%20");
	    List<Address> addresses = null;
	
	    try {
	
	        URL url = new URL("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
	
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
	
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	
			// System.out.println("Output from Server .... \n");
	
			String output;
			while ((output = br.readLine()) != null) {
				json += output;
				// System.out.println(output);
			}
	
			conn.disconnect();
	
			JSONObject obj = new JSONObject(json);
			addresses = getCurrentLocationViaJSON(obj, 10);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
	    return addresses;
	}	

	public static List<Address> getCurrentLocationViaJSON(JSONObject jsonObj, int maxResults) {
	    List<Address> outResult = new ArrayList<>();
	
	    try {
	        // No results status
	        if ("ZERO_RESULTS".equalsIgnoreCase(jsonObj.getString("status"))) {
	            return Collections.emptyList();
	        }
	
	        // Other non-OK responses status
	        if (!"OK".equalsIgnoreCase(jsonObj.getString("status"))) {
	            throw new RuntimeException("Wrong API response");
	        }
	
	        // Process results
	        JSONArray results = jsonObj.getJSONArray("results");
	        for (int i = 0; i < results.length() && i < maxResults; i++) {
	            Address address = new Address(Locale.getDefault());
	            String addressLineString = "";
	            JSONObject sourceResult = results.getJSONObject(i);
	            JSONArray addressComponents = sourceResult.getJSONArray("address_components");
	            address.setPlaceId(sourceResult.getString("place_id"));
	            address.setFormattedAddress(sourceResult.getString("formatted_address"));
	
	            // Assemble address by various components
	            for (int ac = 0; ac < addressComponents.length(); ac++) {
	                String longNameVal = addressComponents.getJSONObject(ac).getString("long_name");
	                String shortNameVal = addressComponents.getJSONObject(ac).getString("short_name");
	                JSONArray acTypes = addressComponents.getJSONObject(ac).getJSONArray("types");
	                String acType = acTypes.getString(0);
	
	                if (!(longNameVal.length() == 0)) {
	                    if (acType.equalsIgnoreCase("street_number")) {
	                    	address.setStreetNumber(longNameVal);
	                    	
	                        if (addressLineString.length() == 0) {
	                            addressLineString = longNameVal;
	                        } else {
	                            addressLineString += " " + longNameVal;
	                        }
	
	                    } else if (acType.equalsIgnoreCase("route")) {
	                    	address.setStreetName(longNameVal);
	                    	
	                        if (addressLineString.length() == 0) {
	                            addressLineString = longNameVal;
	                        } else {
	                            addressLineString = longNameVal + " " + addressLineString;
	                        }
	
	                    } else if (acType.equalsIgnoreCase("sublocality")) {
	                        address.setSubLocality(longNameVal);
	
	                    } else if (acType.equalsIgnoreCase("locality")) {
	                        address.setLocality(longNameVal);
	
	                    } else if (acType.equalsIgnoreCase("administrative_area_level_2")) {
	                        address.setSubAdminArea(longNameVal);
	
	                    } else if (acType.equalsIgnoreCase("administrative_area_level_1")) {
	                        address.setAdminArea(longNameVal);
	
	                    } else if (acType.equalsIgnoreCase("country")) {
	                        address.setCountryName(longNameVal);
	                        address.setCountryCode(shortNameVal);
	
	                    } else if (acType.equalsIgnoreCase("postal_code")) {
	                        address.setPostalCode(longNameVal);
	                    }
	                }
	            }
	
	            JSONObject geometry = sourceResult.getJSONObject("geometry");
	            JSONObject location = geometry.getJSONObject("location");
	            Double lat = location.getDouble("lat");
	            Double lng = location.getDouble("lng");
	
	            address.setLatitude(lat);
	            address.setLongitude(lng);
	
	//            JSONArray geometry = sourceResult.getJSONArray("geometry");
	
	            // Try to get the already formatted address
	            String formattedAddress = sourceResult.getString("formatted_address");
	            if (!(formattedAddress.length() == 0)) {
	                String[] formattedAddressLines = formattedAddress.split(",");
	
	                for (int ia = 0; ia < formattedAddressLines.length; ia++) {
	                    address.setAddressLine(ia, formattedAddressLines[ia].trim());
	                }
	            } else if (!(addressLineString.length() == 0)) {
	                // If that fails use our manually assembled formatted address
	                address.setAddressLine(0, addressLineString);
	            }
	
	            // Finally add address to resulting set
	            outResult.add(address);
	        }
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	
	    return Collections.unmodifiableList(outResult);
	}

	
}
