package com.example.lendyproj.ui.login
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class WatchList(val title: String? = null, val author: String? = null, val description: String? = null
                     , val downloadUri: String? = null, val currentUid: String? = null, val watchListId: String? = null
                     , val type: String? = null, val price: String? = null,
                     val bookId: String? = null, @Exclude val key: String? = null) {
}

//data class WatchList(val title: String? = null, val author: String? = null, val description: String? = null
//                     , val downloadUri: String? = null, val currentUid: String? = null, val watchListId: String? = null
//                     , val type: String? = null, val price: String? = null, val status: String? = null,
//                     val bookId: String? = null, @Exclude val key: String? = null) {
//}