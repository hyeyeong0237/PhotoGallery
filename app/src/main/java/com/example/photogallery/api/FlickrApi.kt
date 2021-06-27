package com.example.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=85b74130421c6647e7cd1f6509f6e2b7" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhotos(): Call<FlickerResponse>


    @GET
    fun fetchUrlBytes(@Url url: String): Call<ResponseBody>
}