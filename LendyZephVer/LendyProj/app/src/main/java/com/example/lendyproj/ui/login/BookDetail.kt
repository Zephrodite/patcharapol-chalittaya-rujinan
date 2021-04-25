package com.example.lendyproj.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lendyproj.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

        val bookId = intent.getStringExtra("bookId")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("book").child(
            bookId.toString())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val watchListRef = database.getReference("watchlist")

        lateinit var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val currentUid = currentUser?.uid.toString()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val book:Book? = dataSnapshot.getValue(Book::class.java)
                if (book != null) {
                    addtoWatchlistButton.setOnClickListener{
                        val watchlist = WatchList(book.title.toString(), book.date.toString(), book.description.toString(), book.downloadUri.toString(), currentUid, book.bookId.toString())
                        watchListRef.child(book.bookId.toString()).setValue(watchlist)
                        Toast.makeText(this@BookDetail, "Book added to Watchlist", Toast.LENGTH_LONG).show()
                    }

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

    fun images(url: String){
        Glide.with(applicationContext)
            .load(url)
            .into(posterImgeView)

//        Glide.with(this)
//            .load(url)
//            .into(backgroundImageView)
    }
}