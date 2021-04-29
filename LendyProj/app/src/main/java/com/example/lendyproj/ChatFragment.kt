package com.example.lendyproj

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lendyproj.ui.login.ChatUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var chatFragment: ChatFragment
    private val database1 = Firebase.database
    val myRef1 = database1.getReference("book")
    private val userList:MutableList<ChatUser> = ArrayList()
    val currentUserid = Firebase.auth.currentUser!!.uid
    val contactReference = database1.getReference("contactId").child(currentUserid).child("userId")
    private lateinit var messagesListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        FirebaseService.sharedPref = activity?.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
        }

        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        view.userRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayout.VERTICAL, false)
        userList.clear()

        setupRecyclerView(view.userRecyclerView)

        return view
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        var i = true

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                dataSnapshot.children.forEach { child ->
                    val chatuser: ChatUser? =
                        ChatUser(child.child("username").getValue<String>(),
                            child.child("profileImageUri").getValue<String>(),
                            child.child("userId").getValue<String>(),
                            child.key)
                    chatuser?.let { userList.add(it) }
                }

                recyclerView.adapter = UserAdapter(userList)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }


        contactReference.addValueEventListener(messagesListener)
    }


    class UserAdapter( private val userList: List<ChatUser>) :
        RecyclerView.Adapter<UserAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = userList[position]
            holder.txtUserName.text = user.username
            holder.imgUser?.let {
                Glide.with(holder.itemView.context)
                    .load(user.profileImageUri)
                    .into(it)
            }

            holder.layoutUser.setOnClickListener { v ->
                val intent = Intent(v.context, ChatActivity::class.java)
                intent.putExtra("userId", user.userId)
                intent.putExtra("userName", user.username)
                intent.putExtra("imageUri", user.profileImageUri.toString())
                v.context.startActivity(intent)
            }
        }

        override fun getItemCount() = userList.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtUserName: TextView = view.findViewById(R.id.userName)
            val txtTemp: TextView = view.findViewById(R.id.temp)
            val imgUser: CircleImageView = view.findViewById(R.id.userImage)
            val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser)
        }

    }
}