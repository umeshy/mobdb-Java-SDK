package com.mobdb.java;


/**
 * This Class generate INSERT SQL query
 * Version 1.0
 */

import java.security.InvalidParameterException;
import java.util.Vector;

import com.mobdb.java.org.json.JSONException;
import com.mobdb.java.org.json.JSONObject;


public class InsertRowData {

	private String query = "INSERT INTO ";
	private Vector fields = null;
	private Vector fieldValue = null;

	private boolean isFilePresent = false;

	public InsertRowData(String tableName) {	

		query = query + tableName;

	}

	/**
	 * Set field name in which data needs to be inserted  
	 * @param field table filed name
	 * @param value String value to insert
	 */
	public void setValue(String field, String value){

		if( fields == null ){

			fields = new Vector();

		}
		if( fieldValue == null ){

			fieldValue = new Vector();

		}

		fields.add( field  );
		fieldValue.add("'"+value+"'");

	}

	/**
	 * Set field name in which data needs to be inserted  
	 * @param field table filed name
	 * @param value int value to insert
	 */
	public void setValue(String field, int value){

		if( fields == null ){

			fields = new Vector();

		}
		if( fieldValue == null ){

			fieldValue = new Vector();

		}

		fields.add( field );
		fieldValue.add(Integer.valueOf(value) );

	}
	/**
	 * Set field name in which data needs to be inserted  
	 * @param field table filed name
	 * @param value Integer value to insert
	 */
	public void setValue(String field, Integer value){

		if( fields == null ){

			fields = new Vector();

		}
		if( fieldValue == null ){

			fieldValue = new Vector();

		}

		fields.add( field );
		fieldValue.add(value);

	}
	
	/**
	 * Set field name in which data needs to be inserted  
	 * @param field table filed name
	 * @param value Double value to insert
	 */

	public void setValue(String field, Double value){

		if( fields == null ){

			fields = new Vector();

		}
		if( fieldValue == null ){

			fieldValue = new Vector();

		}

		fields.add( field );
		fieldValue.add(value);

	}

	/**
	 * Set file bytes array, which needs to inserted in table
	 * @param field table field name
	 * @param fileNameWithFileExtension String value file name with file extension, example: 'image.png'
	 * @param fileBytes file bytes array
	 */
	public void setValue(String field, String fileNameWithFileExtension, byte[] fileBytes) throws InvalidParameterException{

		if(fileNameWithFileExtension == null || fileNameWithFileExtension.trim().length() <= 0){

			throw new InvalidParameterException("File name required");

		}

		if( fields == null ){

			fields = new Vector();

		}

		if( fieldValue == null ){

			fieldValue = new Vector();

		}


		JSONObject file = new JSONObject();

		try {

			file.put( SDKConstants.FILE_NAME, fileNameWithFileExtension );

			file.put( SDKConstants.FILE_DATA, Base64.encodeBytes(fileBytes) );

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fields.add( field );
		fieldValue.add(file);
		isFilePresent = true;

	}

	/**
	 * With conditions contracts SQL query 
	 * @return SQL query string
	 */
	public String getQueryString() throws InvalidParameterException{

		StringBuffer fieldsBuff = new StringBuffer();

		StringBuffer fieldValueBuff = new StringBuffer();

		if( this.fields.size() > 0 ){

			fieldsBuff.append(" (").append( (String)this.fields.elementAt(0) );

		}else{
			throw new InvalidParameterException("Field value needed.");
		}

		if( isFilePresent ){

			fieldValueBuff.append(" VALUES(?");

			for ( int i = 1; i < fields.size(); i++ ) {

				fieldsBuff.append(",").append( (String)this.fields.elementAt(i) );

				fieldValueBuff.append(",?");

			}

		}else{
			fieldValueBuff.append(" VALUES(").append( (this.fieldValue.elementAt(0) ) );
			//fieldValueBuff.append(" VALUES(").append( String.valueOf(this.fieldValue.elementAt(0) ) );

			for ( int i = 1; i < fields.size(); i++ ) {

				fieldsBuff.append(",").append( (String)this.fields.elementAt(i) );

				fieldValueBuff.append(",").append( ( this.fieldValue.elementAt(i) ) );

			}

		}


		fieldsBuff.append(")");

		fieldValueBuff.append(")");

		query = query + fieldsBuff.toString() + fieldValueBuff.toString();

		return query;

	}

	/**
	 * Returns array of SQL parameter objects
	 * @return Object array
	 */
	public Object[] getParameters(){

		if(!isFilePresent){

			return null;

		}

		int size = fieldValue.size() ;

		Object[] o = new Object[size];

		for ( int i = 0; i < size; i++ ) {

			o[i] = fieldValue.elementAt(i);

		}

		return o;

	}

//	private String formatValue( Object o ){
//
//		if( Util.getDataType(o) == SDKConstants.STRING ){
//
//			return "'" + (String)o + "'";
//
//		}else if( Util.getDataType(o) == SDKConstants.INTEGER || Util.getDataType(o) == SDKConstants.FLOAT ){
//
//			return String.valueOf(o);
//
//		}
//
//		return null;
//	}


}
