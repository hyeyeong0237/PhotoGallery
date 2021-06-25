package com.example.photogallery

import com.google.gson.annotations.SerializedName

data class GalleryItem (
    var title: String = "",
    var id: String = "",
    @SerializedName("url_s") val url : String = ""
    )