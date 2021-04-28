package com.example.lendyproj

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lendyproj.R
import com.example.lendyproj.RetrofitInstance
import com.example.lendyproj.ChatAdapter
import com.example.lendyproj.ui.login.Chat
import com.example.lendyproj.NotificationData
import com.example.lendyproj.PushNotification
import com.example.lendyproj.ui.login.Book
import com.example.lendyproj.ui.login.UserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    var firebaseUser: FirebaseUser? = null
    var myRef: DatabaseReference? = null
    var reference: DatabaseReference? = null
    var referenceSender: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""
    private lateinit var mAuth: FirebaseAuth
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var intent = intent
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")
        var imageUri = intent.getStringExtra("imageUri")


        val currentUserid = Firebase.auth.currentUser!!.uid
        firebaseUser = FirebaseAuth.getInstance().currentUser
        myRef = FirebaseDatabase.getInstance().getReference("uid")
        reference = FirebaseDatabase.getInstance().getReference("uid").child(userId!!)


        myRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.child(userId!!).getValue(UserId::class.java)
                tvUserName.text = user!!.username
                if (user.profileImageUri == "") {
                    imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImageUri).into(imgProfile)
                }

                btnSendMessage.setOnClickListener {
                    var message: String = etMessage.text.toString()
                    mAuth = FirebaseAuth.getInstance()
                    if (message.isEmpty()) {
                        Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                        etMessage.setText("")
                    } else {
                        val senderimageTxt = snapshot.child(currentUserid).child("profileImageUri").getValue(String::class.java)
                        sendMessage(firebaseUser!!.uid, userId, message, senderimageTxt.toString(),  imageUri.toString())
                        etMessage.setText("")
                        topic = "/topics/$userId"
                        PushNotification(NotificationData( userName!!,message),
                            topic).also {
                            sendNotification(it)
                        }

                    }
                }

            }
        })

        readMessage(firebaseUser!!.uid, userId, chatRecyclerView)
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String, senderimageUri: String ,  receiverimageUri: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)
        hashMap.put("imageSenderUri", senderimageUri)
        hashMap.put("imageReceiverUri", receiverimageUri)

        reference!!.child("Chat").push().setValue(hashMap)

    }


    fun readMessage(senderId: String, receiverId: String, recyclerView: RecyclerView) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Chat")



        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) || chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)) {
                        chatList.add(chat)
                    }
                }


                val chatAdapter = ChatAdapter(chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

}