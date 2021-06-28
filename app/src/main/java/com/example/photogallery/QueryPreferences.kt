package com.example.photogallery

import android.content.Context
import android.preference.PreferenceManager

private const val PRE_SEARCH_QUERY = "searchQuery"

object QueryPreferences {

    fun getStoreQuery(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PRE_SEARCH_QUERY, "")!!
    }

    fun setStoredQuery(context: Context, query: String){
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PRE_SEARCH_QUERY, query)
            .apply()
    }
}