package com.example.lendylastestver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class YourBookActivity : AppCompatActivity() {
    private lateinit var result: TextView
    private lateinit var mainViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_book)

        result = findViewById(R.id.result)

        mainViewModel = ViewModelProvider.AndroidViewModelFactory(application)
                .create(HomeViewModel::class.java)
        mainViewModel.books.observe(this, Observer {
            booksRetrieved(it)
        })
    }

    fun show(view: View) {

        mainViewModel.retrieveBooks()
    }

    private fun booksRetrieved(books: List<Book>) {
        result.text = books.joinToString("\n")
    }
}