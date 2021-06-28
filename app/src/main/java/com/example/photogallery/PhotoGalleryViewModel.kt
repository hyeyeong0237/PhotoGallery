package com.example.photogallery

import android.app.Application
import androidx.lifecycle.*
import retrofit2.http.Query

class PhotoGalleryViewModel(private val app : Application) : AndroidViewModel(app) {

    val galleryItemLiveData : LiveData<List<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm : String
    get() = mutableSearchTerm.value ?: ""

    init {

        mutableSearchTerm.value = QueryPreferences.getStoreQuery(app)

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) {
            searchTerm ->
            if(searchTerm.isBlank()){
                flickrFetchr.fetchPhotos()
            }else{
                flickrFetchr.searchPhotos(searchTerm)
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}