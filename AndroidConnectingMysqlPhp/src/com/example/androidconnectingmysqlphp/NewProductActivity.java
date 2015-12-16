package com.example.androidconnectingmysqlphp;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

public class NewProductActivity extends Activity{
	//progress dialogue
	private ProgressDialog pDialog;
	
	
	JSONParser jsonParser = new JSONParser();
	
	EditText inputName;
	EditText inputPrice;
	EditText inputDesc;
	
	// url to create new product
	private static String url_create_product="http://10.0.2.2/json_practise/create_product.php";
	
	//JSON nodes Name
	private static final String TAG_SUCCESS = "success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_product);
		
		
		        // Edit Text
				inputName = (EditText) findViewById(R.id.inputName);
				inputPrice = (EditText) findViewById(R.id.inputPrice);
				inputDesc = (EditText) findViewById(R.id.inputDesc);
			
				// Create button
				Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);
				
				btnCreateProduct.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// creating new product in background thread
						new CreateNewProduct().execute();
					}
				});
	}
	
	/**
	 * Background Async Task to Create new product
	 * */
	
	class CreateNewProduct extends AsyncTask<String, String, String>{

		
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(NewProductActivity.this);
			pDialog.setMessage("Creating Product....Please wait..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			
		}
		
		/**
		 * Creating product
		 * */
		
		@Override
		protected String doInBackground(String... args) {
			String name = inputName.getText().toString();
			String price = inputPrice.getText().toString();
			String description = inputDesc.getText().toString();
			
			//Building parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("price",price));
			params.add(new BasicNameValuePair("description", description));
			
			
			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);
			
			// check log cat for response
			Log.i("Create Response", json.toString());
			
			// check for success tag
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success==1){
					// successfully created product
					Intent i = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(i);
					
					// closing this screen
					finish();
					
				}else{
					// failed to create product
				}
			}catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return null;
		}
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		
		@Override
		protected void onPostExecute(String result) {
			// dismiss the dialog once done
			pDialog.dismiss();
			
		}
		
	}
	
}
