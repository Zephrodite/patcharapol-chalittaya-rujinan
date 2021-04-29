package com.example.lendyproj

class UserProfile {
    lateinit var username: String
    lateinit var email: String

    constructor() {}
    constructor(username: String, email: String) {
        this.username = username
        this.email = email
    }
}