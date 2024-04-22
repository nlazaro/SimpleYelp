package com.example.simpleyelp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val restaurants = mutableListOf<YelpRestaurant>()
    private lateinit var rvRestaurants: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // ---
        rvRestaurants = findViewById(R.id.rvRestaurants)
        val restaurantAdapter = RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter = restaurantAdapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)
        val retrofit = RetrofitClient.getClient()
        val yelpService = retrofit.create(YelpService::class.java)

        yelpService.searchRestaurants(
            "Avocado Toast",
            "NYC"
        ).enqueue(object : Callback<YelpSearchResult> {
            override fun onResponse(call: Call<YelpSearchResult>, response: Response<YelpSearchResult>) {
                Log.i(TAG, "onResponse $response")
                val body = response.body()
                if (body == null){
                    Log.w(TAG, "Did not receive valid response body")
                    return
                }
                val curSize = restaurantAdapter.getItemCount()
                val newItems = body.restaurants
                restaurants.addAll(newItems)
                restaurantAdapter.notifyItemRangeInserted(curSize, newItems.size)
            }

            override fun onFailure(call: Call<YelpSearchResult>, throwable: Throwable) {
                Log.i(TAG, "onFailure $throwable")
            }
        })
    }
}