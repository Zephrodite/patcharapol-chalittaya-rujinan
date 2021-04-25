package com.example.lendyproj.chat

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lendyproj.ProfileFragment
import com.example.lendyproj.R
import com.example.lendyproj.ui.login.*
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<ViewHolder>()

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser

        recyclerview_chat_log.adapter = adapter

        val database = Firebase.database
        val bookOwner = intent.getStringExtra("username")
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val myRef = database.getReference("uid").child(
            bookOwner.toString())


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val userId:UserId? = dataSnapshot.getValue(UserId::class.java)
                if (userId != null) {
                    supportActionBar?.title = userId.username.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        setupDummyData()

        listenForMessages()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")
//            performSendMessage()
        }
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

//    private fun performSendMessage() {
//
//        val text = edittext_chat_log.text.toString()
//        val fromId = FirebaseAuth.getInstance().uid
//        val user = intent.getParcelableExtra<User>(BookDetail.USER_KEY)
//        val toId = user.uid
//
//        if (fromId == null) return
//
//        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
//
//        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
//        reference.setValue(chatMessage)
//            .addOnSuccessListener {
//                Log.d(TAG, "Saved our chat message: ${reference.key}")
//            }
//    }

    private fun setupDummyData() {

        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(ChatFromItem("I LOVE YOU"))
        adapter.add(ChatToItem("I LOVE YOU TOO"))

        recyclerview_chat_log.adapter = adapter
    }
}



class ChatFromItem(val text: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}