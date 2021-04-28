package com.example.lendyproj.ui.login
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class WatchList(val title: String? = null, val date: String? = null, val description: String? = null
                     , val downloadUri: String? = null, val currentUid: String? = null, val watchListId: String? = null
                     , val bookId: String? = null, @Exclude val key: String? = null) {
}