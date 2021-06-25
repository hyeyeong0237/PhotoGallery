package com.example.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.api.FlickerResponse
import com.example.photogallery.api.FlickrApi
import com.example.photogallery.api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "FlickrFetchr"

class FlickrFetchr {

    private val flickrApi : FlickrApi

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>>{
        val responseLiveData : MutableLiveData<List<GalleryItem>> = MutableLiveData()


        val flickHomePageRequest : Call<FlickerResponse> = flickrApi.fetchPhotos()

        flickHomePageRequest.enqueue(object : Callback<FlickerResponse> {

            override fun onFailure(call: Call<FlickerResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch photos", t)
            }

            override fun onResponse(call: Call<FlickerResponse>, response: Response<FlickerResponse>) {
                Log.d(TAG, "Response received")
                val flickerResponse: FlickerResponse? = response.body()
                val photoResponse : PhotoResponse? = flickerResponse?.photos
                var galleryItems : List<GalleryItem> = photoResponse?.galleryItem
                    ?: mutableListOf()
                galleryItems = galleryItems.filterNot {
                    it.url.isBlank()
                }

                responseLiveData.value = galleryItems



            }
        })

        return responseLiveData
    }
}