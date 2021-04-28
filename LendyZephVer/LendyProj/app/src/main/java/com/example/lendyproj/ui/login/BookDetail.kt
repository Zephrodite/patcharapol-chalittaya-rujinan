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

class BookDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

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
                            addtoContactButton.setOnClickListener{
//                                datasnapshoT.children.forEach { child ->
//                                    if(child.child("userId").getValue(String::class.java) == userIdCompare
//                                        && child.child("currentUid").getValue(String::class.java) == currentUid){
//                                        i = false
//                                        Toast.makeText(this@BookDetail,
//                                            "This person are in your chat",
//                                            Toast.LENGTH_SHORT).show()
//                                    }
//                                }

                                if ( i == true ) {
                                    val currentUserid1 = Firebase.auth.currentUser!!.uid
                                    uidRef.addValueEventListener(object : ValueEventListener {
                                        @Suppress("NAME_SHADOWING")
                                        override fun onDataChange(datasnapshot: DataSnapshot) {
                                            datasnapshot.children.forEach { child ->
                                                if (child.child("userId")
                                                        .getValue(String::class.java) == currentUserid1
                                                ) {
                                                    val allowedChars =
                                                        ('A'..'Z') + ('a'..'z') + ('0'..'9')
                                                    val contactId =
                                                        (1..40).map { allowedChars.random() }
                                                            .joinToString("")
                                                    val contactId2 =
                                                        (1..40).map { allowedChars.random() }
                                                            .joinToString("")
                                                    val secUsername = child.child("username").getValue(String::class.java)
                                                    val secImageUri = child.child("profileImageUri").getValue(String::class.java)
                                                    val contactid =
                                                        ContactId(userid.username.toString(),
                                                            userid.email.toString(),
                                                            userid.profileImageUri.toString(),
                                                            userid.userId.toString(),
                                                            currentUid,
                                                            contactId,
                                                            bookId, secUsername.toString(), secImageUri.toString())
                                                    val contactid2 =
                                                        ContactId(secUsername.toString(),
                                                            currentEmail,
                                                            secImageUri.toString(),
                                                            currentUid,
                                                            userid.userId.toString(),
                                                            contactId2,
                                                            bookId, userid.username.toString(), userid.profileImageUri.toString())
                                                    val chatuser =
                                                        ChatUser(userid.username.toString(),
                                                            userid.profileImageUri.toString(),
                                                            userid.userId.toString())
                                                    val chatuser2 =
                                                        ChatUser(secUsername.toString(),
                                                            secImageUri.toString(),
                                                            currentUid)
                                                    if (datasnapshoT.hasChild(currentUid)) {

                                                        contactRef.child(currentUid)
                                                            .child("userId").child(userid.userId.toString())
                                                            .setValue(chatuser)
                                                        contactRef.child(userid.userId.toString())
                                                            .child("userId").child(currentUid)
                                                            .setValue(chatuser2)


                                                    } else {
                                                        contactRef.child(currentUid).setValue(contactid)
                                                        contactRef.child(userid.userId.toString()).setValue(contactid2)
                                                        contactRef.child(currentUid)
                                                            .child("userId").child(userid.userId.toString())
                                                            .setValue(chatuser)
                                                        contactRef.child(userid.userId.toString())
                                                            .child("userId").child(currentUid)
                                                            .setValue(chatuser2)
                                                    }
                                                }
                                            }
                                            val intent = Intent(this@BookDetail, ChatActivity::class.java)
                                            intent.putExtra("userId", userid.userId.toString())
                                            intent.putExtra("userName", userid.username.toString())
                                            intent.putExtra("imageUri", userid.profileImageUri.toString())
                                            startActivity(intent)
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Log.w("TAG", "Failed to read value.", error.toException())
                                        }
                                    })
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
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val book:Book? = dataSnapshot.getValue(Book::class.java)
                if (book != null) {
                    val bookIdCompare = book.bookId.toString()
                    shareButton.setOnClickListener {
                        val myIntent = Intent(Intent.ACTION_SEND)
                        myIntent.setType("type/plain").putExtra(Intent.EXTRA_TEXT, book.title + " by " + book.date + "\n description: " + book.description)
                        startActivity(myIntent)
                    }
                    watchListRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            addtoWatchlistButton.setOnClickListener{

                                datasnapshot.children.forEach { child ->
                                    if(child.child("bookId").getValue(String::class.java) == bookIdCompare && child.child("currentUid").getValue(String::class.java) == currentUid) {
                                        j = false
                                        Toast.makeText(this@BookDetail,
                                            "This book are in your watchlist",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                                if ( j == true ) {
                                    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                                    val watchListId =
                                        (1..40).map { allowedChars.random() }.joinToString("")
                                    val watchlist = WatchList(book.title.toString(), book.date.toString(), book.description.toString(), book.downloadUri.toString(), currentUid, watchListId, book.bookId.toString())
                                    watchListRef.child(watchListId).setValue(watchlist)
                                    Toast.makeText(this@BookDetail,
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