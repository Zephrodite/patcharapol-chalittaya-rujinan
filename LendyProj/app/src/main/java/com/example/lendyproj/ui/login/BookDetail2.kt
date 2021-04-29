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
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val uidRef = database.getReference("uid")
        val userIdRef = database.getReference("uid").child(userId.toString())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val contactRef = database.getReference("contactId")
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val contactRefuid = database.getReference("contactId").child(currentUid).child("userId")
        val currentEmail = currentUser?.email.toString()



        var i = true

        userIdRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userid:UserId? = dataSnapshot.getValue(UserId::class.java)
                if (userid != null) {

                    val userIdCompare = userid.userId.toString()

                    contactRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(datasnapshot: DataSnapshot) {
                            addtoContactButton2.setOnClickListener{

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
                                                            watchListId, secUsername.toString(), secImageUri.toString())
                                                    val contactid2 =
                                                        ContactId(secUsername.toString(),
                                                            currentEmail,
                                                            secImageUri.toString(),
                                                            currentUid,
                                                            userid.userId.toString(),
                                                            contactId2,
                                                            watchListId, userid.username.toString(), userid.profileImageUri.toString())

                                                    val chatuser =
                                                        ChatUser(userid.username.toString(),
                                                            userid.profileImageUri.toString(),
                                                            userid.userId.toString())

                                                    val chatuser2 =
                                                        ChatUser(secUsername.toString(),
                                                            secImageUri.toString(),
                                                            currentUid)


                                                    if (dataSnapshot.hasChild(currentUid)) {

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

                                            val intent = Intent(this@BookDetail2, ChatActivity::class.java)
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
        myRef1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val watchlist: WatchList? = dataSnapshot.getValue(WatchList::class.java)
                if (watchlist != null) {

                    titleTextView2.text = watchlist.title.toString()
                    authorTextView2.text = watchlist.author.toString()
                    descriptionTextView2.text = watchlist.description.toString()
                    typeTextView5.text = "Type : " + watchlist.type.toString()
                    priceTextView5.text = "Price : " + watchlist.price.toString() + " ฿"
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