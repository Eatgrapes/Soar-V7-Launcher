package me.eldodebug.soarlauncher.utils.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import me.eldodebug.soarlauncher.logger.SoarLogger;

public class HttpUtils {

    private static String ACCEPTED_RESPONSE = "application/json";
    private static Gson gson = new Gson();
    
    public static JsonObject readJson(HttpURLConnection connection) {
        return gson.fromJson(readResponse(connection), JsonObject.class);
    }
	
    public static JsonObject postJson(String url, Object request) {
    	
        HttpURLConnection connection = setupConnection(url, UserAgents.MOZILLA, 5000, false);
        connection.setDoOutput(true);
        connection.addRequestProperty("Content-Type", ACCEPTED_RESPONSE);
        connection.addRequestProperty("Accept", ACCEPTED_RESPONSE);

        try {
            connection.setRequestMethod("POST");
            connection.getOutputStream().write(gson.toJson(request).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
        	SoarLogger.error("Failed to post json", e);
        }
        
        return readJson(connection);
    }

    public static String readResponse(HttpURLConnection connection) {
    	
        String redirection = connection.getHeaderField("Location");
        
        if (redirection != null) {
        	return readResponse(setupConnection(redirection, UserAgents.MOZILLA, 5000, false));
        }

        StringBuilder response = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getResponseCode() >= 400 ? connection.getErrorStream() : connection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line).append('\n');
            }
        } catch (IOException e) {
        	SoarLogger.error("Failed to read response", e);
        }

        return response.toString();
    }
    
	public static JsonObject readJson(String url, Map<String, String> headers, String userAgents) {
        try {
            HttpURLConnection connection = setupConnection(url, userAgents, 5000, false);

            if (headers != null) {
                for (String header : headers.keySet()) {
                    connection.addRequestProperty(header, headers.get(header));
                }
            }

            InputStream is = connection.getResponseCode() != 200 ? connection.getErrorStream() : connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            
            return gson.fromJson(readResponse(rd), JsonObject.class);
        } catch (IOException e) {
        	SoarLogger.error("Failed to read json", e);
        }
        
        return null;
	}
	
	public static JsonObject readJson(String url, Map<String, String> headers) {
		return readJson(url, headers, UserAgents.MOZILLA);
	}
	
    private static String readResponse(BufferedReader br) {
    	
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            
            while ((line = br.readLine()) != null) {
            	sb.append(line);
            }
            
            return sb.toString();
        } catch (IOException e) {
        	SoarLogger.error("Failed to read response", e);
        }
        
        return null;
    }
	
	public static boolean downloadFile(String url, File outputFile, String userAgent, int timeout, boolean useCaches) {
		
        url = url.replace(" ", "%20");
        
        try (FileOutputStream fileOut = new FileOutputStream(outputFile); BufferedInputStream in = new BufferedInputStream(setupConnection(url, userAgent, timeout, useCaches).getInputStream())) {
        	org.apache.commons.io.IOUtils.copy(in, fileOut);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
	}
	
	public static boolean downloadFile(String url, File outputFile, String userAgents) {
		return downloadFile(url, outputFile, userAgents, 5000, false);
	}
	
	public static boolean downloadFile(String url, File outputFile) {
		return downloadFile(url, outputFile, UserAgents.MOZILLA, 5000, false);
	}
	
	public static HttpURLConnection setupConnection(String url, String userAgent, int timeout, boolean useCaches) {
		
		try {
			HttpURLConnection connection = ((HttpURLConnection) new URL(url).openConnection());
			
	        connection.setRequestMethod("GET");
	        connection.setUseCaches(useCaches);
	        connection.addRequestProperty("User-Agent", userAgent);
	        connection.setRequestProperty("Accept-Language", "en-US");
	        connection.setRequestProperty("Accept-Charset","UTF-8");
	        connection.setReadTimeout(timeout);
	        connection.setConnectTimeout(timeout);
	        connection.setDoOutput(true);
	        
	        return connection;
		} catch (Exception e) {
			SoarLogger.error("Failed to setup connection");
		}
        
		return null;
	}
	
	public static String encode(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static String decode(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
