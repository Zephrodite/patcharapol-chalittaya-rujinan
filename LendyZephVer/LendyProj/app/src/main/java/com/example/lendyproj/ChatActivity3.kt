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
import com.example.lendyproj.RetrofitInstance
import com.example.lendyproj.ChatAdapter
import com.example.lendyproj.ui.login.Chat
import com.example.lendyproj.NotificationData
import com.example.lendyproj.PushNotification
import com.example.lendyproj.ui.login.UserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_chat.btnSendMessage
import kotlinx.android.synthetic.main.activity_chat.etMessage
import kotlinx.android.synthetic.main.activity_chat.imgProfile
import kotlinx.android.synthetic.main.activity_chat.tvUserName
import kotlinx.android.synthetic.main.activity_chat2.*
import kotlinx.android.synthetic.main.activity_chat3.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

class ChatActivity3 : AppCompatActivity() {
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
        setContentView(R.layout.activity_chat3)

        chatRecyclerView3.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var intent = intent
        var seconduserId1 = intent.getStringExtra("seconduserId1")
        var seconduserIduserName1 = intent.getStringExtra("seconduserIduserName1")
        var seconduserIdimageUri1 = intent.getStringExtra("seconduserIdimageUri1")

        val currentUserid = Firebase.auth.currentUser!!.uid
        firebaseUser = FirebaseAuth.getInstance().currentUser
        myRef = FirebaseDatabase.getInstance().getReference("uid")
        reference = FirebaseDatabase.getInstance().getReference("uid").child(seconduserId1!!)


        myRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.child(seconduserId1!!).getValue(UserId::class.java)
                tvUserName.text = user!!.username
                if (user.profileImageUri == "") {
                    imgProfile.setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity3).load(user.profileImageUri).into(imgProfile)
                }

                btnSendMessage.setOnClickListener {
                    var message: String = etMessage.text.toString()
                    mAuth = FirebaseAuth.getInstance()
                    if (message.isEmpty()) {
                        Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                        etMessage.setText("")
                    } else {
                        val senderimageTxt = snapshot.child(currentUserid).child("profileImageUri").getValue(String::class.java)
                        sendMessage(firebaseUser!!.uid, seconduserId1, message, senderimageTxt.toString(),  seconduserIdimageUri1.toString())
                        etMessage.setText("")
                        topic = "/topics/$seconduserId1"
                        PushNotification(NotificationData( seconduserIduserName1!!,message),
                            topic).also {
                            sendNotification(it)
                        }

                    }
                }

            }
        })

        readMessage(firebaseUser!!.uid, seconduserId1, chatRecyclerView3)
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

                chatRecyclerView3.adapter = chatAdapter
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