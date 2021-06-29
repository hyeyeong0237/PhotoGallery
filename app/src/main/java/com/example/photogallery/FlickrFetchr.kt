package com.example.photogallery

import android.app.DownloadManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.api.FlickerResponse
import com.example.photogallery.api.FlickrApi
import com.example.photogallery.api.PhotoInterceptor
import com.example.photogallery.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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

        val client = OkHttpClient.Builder()
            .addInterceptor(PhotoInterceptor())
            .build()
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    fun fetchPhotosRequest() : Call<FlickerResponse> {
        return flickrApi.fetchPhotos()
    }

    fun fetchPhotos(): LiveData<List<GalleryItem>> {
        return fetchPhotosMetadata(fetchPhotosRequest())
    }
    fun searchPhotosRequest(query: String): Call<FlickerResponse> {
        return flickrApi.searchPhotos(query)
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>>{
        return fetchPhotosMetadata(searchPhotosRequest(query))
    }

    fun fetchPhotosMetadata(flickRequest: Call<FlickerResponse>): LiveData<List<GalleryItem>>{
        val responseLiveData : MutableLiveData<List<GalleryItem>> = MutableLiveData()


        flickRequest.enqueue(object : Callback<FlickerResponse> {

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

    @WorkerThread
    fun fetchPhoto(url: String): Bitmap? {
        val response : Response<ResponseBody> = flickrApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap = $bitmap from response = $response")
        return bitmap
    }
}