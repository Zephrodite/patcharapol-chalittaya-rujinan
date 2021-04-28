package com.example.lendyproj

import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lendyproj.R
import com.example.lendyproj.ChatActivity
import com.example.lendyproj.ui.login.Chat
import com.example.lendyproj.ui.login.UserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val chatList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount() = chatList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtUserName.text = chat.message

        holder.imgUser1.let {
            Glide.with(holder.itemView.context)
                .load(chat.imageSenderUri)
                .into(it)
        }

        holder.imgUser.let {
            Glide.with(holder.itemView.context)
                .load(chat.imageSenderUri)
                .into(it)
        }


    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName: TextView = view.findViewById(R.id.tvMessage)
        val imgUser: CircleImageView = view.findViewById(R.id.userImage)
        val imgUser1: CircleImageView = view.findViewById(R.id.userImage1)

    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }
}