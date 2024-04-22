package com.example.simpleyelp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // new code
        val button = findViewById<Button>(R.id.connect)
        button.setOnClickListener {
            val retrofit = RetrofitClient.getClient()
            val yelpService = retrofit.create(YelpService::class.java)

            yelpService.searchRestaurants(
                "Avocado Toast",
                "NYC"
            ).enqueue(object : Callback<YelpSearchResult> {
                override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                    Log.i(TAG, "onResponse $response")
                    Toast.makeText(this@MainActivity, "Response: $response", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(call: Call<YelpSearchResult>, throwable: Throwable) {
                    Log.i(TAG, "onFailure $throwable")
                }
            })
        }
    }
}