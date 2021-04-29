package com.example.lendyproj.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lendyproj.ChatActivity
import com.example.lendyproj.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_book_detail.addtoWatchlistButton
import kotlinx.android.synthetic.main.activity_book_detail.authorTextView
import kotlinx.android.synthetic.main.activity_book_detail.descriptionTextView
import kotlinx.android.synthetic.main.activity_book_detail.posterImgeView
import kotlinx.android.synthetic.main.activity_book_detail.titleTextView
import kotlinx.android.synthetic.main.activity_book_detail3.*

class BookDetail3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail3)

        val userId = intent.getStringExtra("userId")
        val bookId = intent.getStringExtra("bookId")
        val database = Firebase.database

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("book").child(
            bookId.toString())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val watchListRef = database.getReference("watchlist")

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val uidRef = database.getReference("uid")
        val userIdRef = database.getReference("uid").child(userId.toString())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val contactRef = database.getReference("contactId")

        lateinit var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val currentUid = currentUser?.uid.toString()
        val currentEmail = currentUser?.email.toString()
        var j = true
        var i = true


        userIdRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userid:UserId? = dataSnapshot.getValue(UserId::class.java)
                if (userid != null) {

                    val userIdCompare = userid.userId.toString()

                    contactRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(datasnapshoT: DataSnapshot) {

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.w("TAG", "Failed to read value.", error.toException())
                        }
                    })


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })




        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val book:Book? = dataSnapshot.getValue(Book::class.java)
                if (book != null) {

                    val bookIdCompare = book.bookId.toString()

//                    shareButton.setOnClickListener {
//                        val myIntent = Intent(Intent.ACTION_SEND)
//                        myIntent.setType("type/plain").putExtra(Intent.EXTRA_TEXT, book.title + " by " + book.date + "\n description: " + book.description)
//                        startActivity(myIntent)
//                    }

                    watchListRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            addtoWatchlistButton.setOnClickListener{

                                datasnapshot.children.forEach { child ->
                                    if(child.child("bookId").getValue(String::class.java) == bookIdCompare && child.child("currentUid").getValue(String::class.java) == currentUid) {
                                        j = false
                                        Toast.makeText(this@BookDetail3,
                                            "This book are in your watchlist",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }

                                if ( j == true ) {
                                    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                                    val watchListId =
                                        (1..40).map { allowedChars.random() }.joinToString("")

                                    val watchlist = WatchList(book.title.toString(), book.author.toString(), book.description.toString(), book.downloadUri.toString()
                                        , currentUid, watchListId, book.type.toString(), book.price.toString() , book.bookId.toString())
                                    watchListRef.child(watchListId).setValue(watchlist)
                                    Toast.makeText(this@BookDetail3,
                                        "Book added to Watchlist",
                                        Toast.LENGTH_SHORT).show()

                                }


                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.w("TAG", "Failed to read value.", error.toException())
                        }
                    })

                    titleTextView.text = book.title.toString()
                    authorTextView.text = book.author.toString()
                    descriptionTextView.text = book.description.toString()
                    typeTextView3.text = book.type.toString()
                    priceTextView3.text = book.price.toString()
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