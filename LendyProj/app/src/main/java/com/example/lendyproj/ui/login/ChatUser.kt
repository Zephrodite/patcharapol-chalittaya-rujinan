package com.example.lendyproj.ui.login

import com.google.firebase.database.Exclude

class ChatUser(val username: String? = null, val profileImageUri: String? = null, val userId: String? = null, @Exclude val key: String? = null) {
}