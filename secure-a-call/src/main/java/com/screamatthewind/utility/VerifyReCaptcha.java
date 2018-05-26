package com.screamatthewind.utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONObject;
import javax.net.ssl.HttpsURLConnection;

public class VerifyReCaptcha {

	public static final String url = "https://www.google.com/recaptcha/api/siteverify";
	public static final String secret = "secret";
	private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean verify(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		
		try{
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	
			String postParams = "secret=" + secret + "&response=" + gRecaptchaResponse;
	
			// Send post request
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
	
			int responseCode = con.getResponseCode();
	
			// System.out.println("\nSending 'POST' request to URL : " + url);
			// System.out.println("Post parameters : " + postParams);
			// System.out.println("Response Code : " + responseCode);
	
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
			String inputLine;
			StringBuffer response = new StringBuffer();
	
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
	
			// print result
			// System.out.println(response.toString());
			
			//parse JSON response and return 'success' value
			JSONObject jsonObject = new JSONObject(response.toString());
			
			return jsonObject.getBoolean("success");
		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}