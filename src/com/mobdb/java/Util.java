package com.mobdb.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.mobdb.java.org.json.JSONObject;

public class Util {
	
	//----------------------Data types-------------
	public static final String INTEGER = "integer";
	public static final String STRING  = "string";
	public static final String FLOAT   = "float";
	public static final String FILE   = "jsonObject";
	//----------------------END--------------------
	public static String getDataType( Object dataObj ){
		
		if( dataObj.getClass().equals( Integer.TYPE  ) ||  dataObj.getClass().equals( Integer.class  ) ){
			
			return INTEGER;
			
		}else if( dataObj.getClass().equals( String.class ) ){
			
			return STRING;
			
		}else if( dataObj.getClass().equals( Float.TYPE ) || dataObj.getClass().equals( Float.class ) || dataObj.getClass().equals( Double.TYPE ) || dataObj.getClass().equals( Double.class )){
			
			return FLOAT;
			
		}else if( dataObj.getClass().equals( JSONObject.class ) ){
			
			return FILE;
			
		}
		
		return null;
		
	}
	
	public static byte[] readBytes(InputStream inputStream) throws IOException {

		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		int len = 0;

		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		return byteBuffer.toByteArray();
	}
	
}
