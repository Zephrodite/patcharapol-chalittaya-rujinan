package com.example.lendyproj.ui.login

import com.google.firebase.database.Exclude

data class Chat(var senderId:String = "", var receiverId:String = "", var message:String = "", var imageSenderUri:String = "", var imageReceiverUri:String = "", @Exclude val key: String? = null)