package com.mobdb.java;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This class send file request and retrieves file bytes array 
 * @since 1.4 JDK
 */
public class MobDBRequest implements Runnable  {

	
	private MobDBResponseListener listener;
	private String urlParameters;
	private boolean secure;
	private String params;

	public MobDBRequest( boolean secure, MobDBResponseListener listner) {
		// TODO Auto-generated constructor stub
		this.listener = listner;
		this.secure = secure;
	}
	
	public void executeRequest(){
		execute(params);
	}
	
	public void setParams(String jsonParams){
		this.params = jsonParams;
	}
	
	public void execute(String jsonStr){
		this.urlParameters = jsonStr;
		Thread reqThread = new Thread(this);
		reqThread.start();
	}

	private void onPostExecute(String contentType, byte[] result) {
	
		if( result != null ){

			if( contentType.indexOf("json") == -1 ){

				this.listener.mobDBFileResponse( contentType.substring(contentType.indexOf(";") + 1 ), result );

			}else{

				String jsonStr =   new String( result ) ;
				this.listener.mobDBResponse(jsonStr);	
				MobDBJSONHandler jsonParser = new MobDBJSONHandler(this.listener);
				jsonParser.parse(jsonStr);

			}

		}
		MobDB.requestCompleted(this);
	}

	public void run() {
		
		if(secure){
			secureConnection();
		}else{
			normalConnection();
		}
		
	}
	
	private void normalConnection(){


		URL url;
		HttpURLConnection connection = null;  

		try {

			//Create connection
			url = new URL(SDKConstants.URL_HTTP);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
					SDKConstants.JSON_CONTENT);

    				
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			//Send request
			DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());
			wr.writeBytes (urlParameters);System.out.println(urlParameters);
			wr.flush ();
			wr.close ();
			
			String contentType = connection.getContentType();
			
			//Get Response	
			InputStream is = connection.getInputStream();
			
			byte[] b = Util.readBytes(is);
			if(contentType != null && b != null){
				onPostExecute(contentType,b);
			}

		} catch (Exception e) {

			e.printStackTrace();


		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	
	}
	
	private void secureConnection(){
		
	     TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
           public java.security.cert.X509Certificate[] getAcceptedIssuers() {
               return null;
           }
 
           public void checkClientTrusted(
                   java.security.cert.X509Certificate[] certs, String authType) {
           }
 
           public void checkServerTrusted(
                   java.security.cert.X509Certificate[] certs, String authType) {}
       } };
 
       try {
       	
           SSLContext sc = SSLContext.getInstance("SSL");
           sc.init(null, trustAllCerts, new java.security.SecureRandom());
           HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
           
       } catch (Exception e) {
           e.printStackTrace();
           System.exit(1);
       }
 
       URL url = null;
       
       try {

       	url = new URL(SDKConstants.URL_HTTPS);
       	
       } catch (MalformedURLException mue) {
           mue.printStackTrace();
           System.exit(1);
       }
 
       try {
       	
       	HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
           
           connection.setRequestMethod("POST");
			
			connection.setRequestProperty("Content-Type", 
					SDKConstants.JSON_CONTENT);

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
           
           DataOutputStream wr = new DataOutputStream (
					connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush ();
			wr.close ();
			
			String contentType = connection.getContentType();
			
			//Get Response	
			InputStream is = connection.getInputStream();
			
			byte[] b = Util.readBytes(is);
			onPostExecute(contentType,b);
			
       } catch (IOException ioe) {
           ioe.printStackTrace();
           System.exit(1);
       }
   
	}
	

}
