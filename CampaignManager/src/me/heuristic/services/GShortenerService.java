package me.heuristic.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import me.heuristic.references.GoogleApis;
import me.heuristic.references.Messages;

public class GShortenerService {

	public static String shorten(String longUrl) {
	    String shortUrl = "";
	
	    try {
	        URLConnection conn = new URL(GoogleApis.ShortenerUrl).openConnection();
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Content-Type", "application/json");
	        OutputStreamWriter wr =
	                     new OutputStreamWriter(conn.getOutputStream());
	        wr.write("{\"longUrl\":\"" + longUrl + "\"}");
	        wr.flush();
	
	        // Get the response
	        BufferedReader rd =
	                     new BufferedReader(
	                     new InputStreamReader(conn.getInputStream()));
	        String line;
	
	        while ((line = rd.readLine()) != null) {
	            if (line.indexOf("id") > -1) {
	                // I'm sure there's a more elegant way of parsing
	                // the JSON response, but this is quick/dirty =)
	                shortUrl = line.substring(8, line.length() - 2);
	                break;
	            }
	        }
	
	        wr.close();
	        rd.close();
	    } catch (MalformedURLException ex) {
	    	return Messages.MALFORMATTED_URL;
	    } catch (IOException ex) {
	    	return Messages.UNKNOWN_ERROR;
	    }
	
	    if ("".equals(shortUrl))
	    	return Messages.UNKNOWN_ERROR;
	    else
	    	return shortUrl;
	}
}
