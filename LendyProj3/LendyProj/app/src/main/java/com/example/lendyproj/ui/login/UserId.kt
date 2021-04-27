package com.example.lendyproj.ui.login
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserId(val username: String? = null, val email: String? = null, val profileImageUri: String? = null, @Exclude val key: String? = null) {
}