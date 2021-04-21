package com.example.lendyproj.ui.login

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lendyproj.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_book_detail.*

class BookDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("book").child(
            key.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val book:Book? = dataSnapshot.getValue(Book::class.java)
                if (book != null) {
                    titleTextView.text = book.title.toString()
                    dateTextView.text = book.date.toString()
                    descriptionTextView.text = book.description.toString()
                    images(book.downloadUri.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })


    }

    private  fun images(url: String){
        Glide.with(this)
            .load(url)
            .into(posterImgeView)

        Glide.with(this)
            .load(url)
            .into(backgroundImageView)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}