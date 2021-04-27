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
import kotlinx.android.synthetic.main.activity_book_detail2.*

class BookDetail2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail2)

        val watchListId = intent.getStringExtra("watchListId")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef1 = database.getReference("watchlist").child(
            watchListId.toString())

        myRef1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val watchlist: WatchList? = dataSnapshot.getValue(WatchList::class.java)
                if (watchlist != null) {

                    titleTextView1.text = watchlist.title.toString()
                    dateTextView1.text = watchlist.date.toString()
                    descriptionTextView1.text = watchlist.description.toString()
                    images(watchlist.downloadUri.toString())
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
            .into(posterImgeView1)

//        Glide.with(this)
//            .load(url)
//            .into(backgroundImageView)
    }
}