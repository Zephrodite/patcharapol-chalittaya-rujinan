package com.example.lendyproj.ui.login

import com.google.firebase.database.Exclude

class ContactId(val username: String? = null, val email: String? = null, val profileImageUri: String? = null, val userId: String? = null
                , val currentUid: String? = null, val contactId: String? = null
                , val bookId: String? = null, val secondUsername: String? = null, val secondprofileImageUri: String? = null, @Exclude val key: String? = null) {
}