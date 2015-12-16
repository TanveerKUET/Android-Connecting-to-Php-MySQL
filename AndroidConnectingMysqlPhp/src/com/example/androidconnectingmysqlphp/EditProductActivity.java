package com.example.androidconnectingmysqlphp;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProductActivity extends Activity{
	EditText txtName;
	EditText txtPrice;
	EditText txtDesc;
	EditText txtCreatedAt;
	Button btnSave;
	Button btnDelete;

	String pid;
	String productName;
	String productPrice;
	String productDescription;

	
	
	TextView txtView;
	
	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();
	
	// single product url
	private static final String url_product_detials = "http://10.0.2.2/json_practise/get_product_details.php";

	// url to update product
	private static final String url_update_product = "http://10.0.2.2/json_practise/update_product.php";

	// url to delete product
	private static final String url_delete_product = "http://10.0.2.2/json_practise/delete_product.php";

	
	// JSON Node names
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_PRODUCT = "product";
		private static final String TAG_PID = "pid";
		private static final String TAG_NAME = "name";
		private static final String TAG_PRICE = "price";
		private static final String TAG_DESCRIPTION = "description";
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.edit_product);
			
			// save button
			btnSave = (Button) findViewById(R.id.btnSave);
			
			//delete button
			btnDelete = (Button) findViewById(R.id.btnDelete);
			
			
			txtView = (TextView)findViewById(R.id.textView1);
			// Edit Text
			txtName = (EditText) findViewById(R.id.inputName);
			txtPrice = (EditText) findViewById(R.id.inputPrice);
			txtDesc = (EditText) findViewById(R.id.inputDesc);
			
			// getting product details from intent
			Intent i = getIntent();
			
			// getting product id (pid) from intent
			pid = i.getStringExtra(TAG_PID);
	        Log.d("Before entering into GetProductDetails() ", pid);
			// Getting complete product details in background thread
			new GetProductDetails().execute();
			
			btnSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// starting background task to update product
					new SaveProductDetails().execute();
					
				}
			});
			
			btnDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				new DeleteProduct().execute();	
					
				}
			});
			
		}
		
		/**
		 * Background Async Task to Get complete product details
		 * */
		class GetProductDetails extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(EditProductActivity.this);
				pDialog.setMessage("Loading product details. Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				//Toast.makeText(EditProductActivity.this, "pid: "+pid, Toast.LENGTH_SHORT).show();
				pDialog.show();
			}

			/**
			 * Getting product details in background thread
			 * */
			
			protected String doInBackground(String... args) {
				
				
				
						int success;
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("pid", pid));
						
						
						// getting product details by making HTTP request
						// Note that product details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(url_product_detials, "GET", params);

						// check your log for json response
					/*	Log.d("Single Product Details", json.toString()); */
						try {
							
							
							// json success tag
						success = json.getInt(TAG_SUCCESS); 
						Log.e("json success", ""+success);								
							if (success == 1) {
								// successfully received product details
								JSONArray productObj = json
										.getJSONArray(TAG_PRODUCT); // JSON Array
								
								// get first product object from JSON Array
								JSONObject product = productObj.getJSONObject(0);
								Log.e("TAG_NAME ", product.getString(TAG_NAME));
								Log.e("TAG_PRICE ", product.getString(TAG_PRICE));
								Log.e("TAG_DESCRIPTION ", product.getString(TAG_DESCRIPTION));
								//txtName = (EditText) findViewById(R.id.inputName);
								
								 productName = product.getString(TAG_NAME);
								 productPrice = product.getString(TAG_PRICE);
								 productDescription = product.getString(TAG_DESCRIPTION);
								
								//txtName.setText(product.getString(TAG_NAME));
								
								/*
								// product with this pid found
								// Edit Text
								txtName = (EditText) findViewById(R.id.inputName);
								txtPrice = (EditText) findViewById(R.id.inputPrice);
								txtDesc = (EditText) findViewById(R.id.inputDesc);

								// display product data in EditText
								txtName.setText(product.getString(TAG_NAME));
								txtPrice.setText(product.getString(TAG_PRICE));
								txtDesc.setText(product.getString(TAG_DESCRIPTION));*/

							}else{
								// product with pid not found
							}
						
						} catch (JSONException e) {
							//e.printStackTrace();
							txtView.setText("success :"+0);
						}
					
				return null;
			}
			/**
			 * After completing background task Dismiss the progress dialog
			 * When Using any GUI work then do that in onPostExecuteMethod
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once product updated
				pDialog.dismiss();
				txtName.setText(productName);
				txtPrice.setText(productPrice);
				txtDesc.setText(productDescription);
			}
		}
		
		/**
		 * Background Async Task to  Save product Details
		 * */
		class SaveProductDetails extends AsyncTask<String, String, String>{
			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
            protected void onPreExecute() {
	        // TODO Auto-generated method stub
	        super.onPreExecute();
	        pDialog = new ProgressDialog(EditProductActivity.this);
	        pDialog.setCancelable(true);
	        pDialog.setMessage("Please wait while updating data.......");
	        pDialog.setIndeterminate(false);
	        pDialog.show();
	        
            }
			
			/**
			 * Saving product
			 * */
			
			@Override
			protected String doInBackground(String... args) {
				// getting updated data from EditTexts
				String name = txtName.getText().toString();
				String price = txtPrice.getText().toString();
				String description = txtDesc.getText().toString();
				
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair(TAG_PID, pid));
				params.add(new BasicNameValuePair(TAG_NAME, name));
				params.add(new BasicNameValuePair(TAG_PRICE, price));
				params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
				
				
				// sending modified data through http request
				// Notice that update product url accepts POST method
				JSONObject json = jsonParser.makeHttpRequest(url_update_product,"POST", params);
				
				// check json success tag
				try{
					int success = json.getInt(TAG_SUCCESS);
					Log.e("TAG_SUCCESS", TAG_SUCCESS);
					if(success==1){
						// successfully updated
						Intent i = getIntent();
						// send result code 100 to notify about product update
						setResult(100, i);
						finish();
					}
					else{
						Log.e("Update Error", "I am sorry to update........");
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				
				return null;
			}
			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pDialog.dismiss();
			}
			
		}
		
		/*****************************************************************
		 * Background Async Task to Delete Product
		 * */
		class DeleteProduct extends AsyncTask<String, String, String>{
           
		   @Override
           protected void onPreExecute() {
			   super.onPreExecute();
				pDialog = new ProgressDialog(EditProductActivity.this);
				pDialog.setMessage("Deleting Product...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
	       
            }
		   
		   /**
			 * Deleting product
			 * */
		   
			
		   @Override
			protected String doInBackground(String... args) {
			// Check for success tag
				int success;
				try{
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("pid", pid));
					
					// getting product details by making HTTP request
					JSONObject json = jsonParser.makeHttpRequest(url_delete_product, "POST", params);
					// check your log for json response
					Log.e("Delete Product", json.toString());
					
					// json success tag
					
					success = json.getInt(TAG_SUCCESS);
					if(success==1){
						// product successfully deleted
						// notify previous activity by sending code 100
						Intent i = getIntent();
						// send result code 100 to notify about product deletion
						setResult(100, i);
						//Intent myIntent = new Intent(EditProductActivity.this, MainActivity.class);
						//startActivity(myIntent);
						finish();
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
		   /**
			 * After completing background task Dismiss the progress dialog
			 * **/
		   @Override
			protected void onPostExecute(String result) {
			// dismiss the dialog once product deleted
				pDialog.dismiss();
			}
			
		}
		
		
}
