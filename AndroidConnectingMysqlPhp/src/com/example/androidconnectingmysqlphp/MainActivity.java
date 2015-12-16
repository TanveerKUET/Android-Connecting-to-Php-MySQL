package com.example.androidconnectingmysqlphp;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
     
	Button btnViewProducts;
	Button btnNewProduct;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//button
		btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
		btnNewProduct = (Button)  findViewById(R.id.btnCreateProduct);
		
		
		btnNewProduct.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getApplicationContext(), NewProductActivity.class);
				startActivity(myIntent);
				
			}
		});
		
		btnViewProducts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Launching All products Activity
				Intent myIntent = new Intent(getApplicationContext(), AllProductsActivity.class);
				startActivity(myIntent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
