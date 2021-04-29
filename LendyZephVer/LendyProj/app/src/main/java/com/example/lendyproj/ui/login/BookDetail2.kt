package com.example.lendyproj.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lendyproj.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_book_detail2.*
import kotlinx.android.synthetic.main.activity_book_detail2.authorTextView2
import kotlinx.android.synthetic.main.activity_book_detail2.titleTextView2
import kotlinx.android.synthetic.main.watchlist_content.*


class BookDetail2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail2)

        val bookId = intent.getStringExtra("bookId")
        val userId = intent.getStringExtra("userId")
        val watchListId = intent.getStringExtra("watchListId")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef1 = database.getReference("watchlist").child(
            watchListId.toString())
        lateinit var mAuth: FirebaseAuth
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val currentUid = currentUser?.uid.toString()

        val userIdRef = database.getReference("uid").child(userId.toString())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val contactRef = database.getReference("contactId")
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val contactRefuid = database.getReference("contactId").child(currentUid).child("userId")



        var i = true

        userIdRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userid:UserId? = dataSnapshot.getValue(UserId::class.java)
                if (userid != null) {

                    val userIdCompare = userid.userId.toString()

                    contactRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            addtoContactButton.setOnClickListener{

                                if ( i == true ) {
                                    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                                    val contactId = (1..40).map { allowedChars.random() }.joinToString("")
                                    val contactid = ContactId(userid.username.toString(), userid.email.toString(), userid.profileImageUri.toString(), userid.userId.toString(), currentUid, contactId, bookId)
                                    contactRef.child(contactId).setValue(contactid)

                                }


                            }
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
        myRef1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val watchlist: WatchList? = dataSnapshot.getValue(WatchList::class.java)
                if (watchlist != null) {

                    titleTextView2.text = watchlist.title.toString()
                    authorTextView2.text = watchlist.author.toString()
                    descriptionTextView2.text = watchlist.description.toString()
                    typeTextView5.text = watchlist.type.toString()
                    priceTextView5.text = watchlist.price.toString()
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
            .into(posterImgeView)

//        Glide.with(this)
//            .load(url)
//            .into(backgroundImageView)
    }
}