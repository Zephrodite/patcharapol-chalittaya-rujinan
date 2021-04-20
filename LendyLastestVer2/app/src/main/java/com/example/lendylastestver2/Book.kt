package com.example.lendylastestver2

class Book constructor(
        private val bookName: String,
        private val authorName: String
){
    override fun toString(): String {
        return "$bookName : $authorName"
    }
}