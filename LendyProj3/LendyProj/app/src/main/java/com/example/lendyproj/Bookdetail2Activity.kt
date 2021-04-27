package com.example.lendyproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

import com.example.lendyproj.chat.ChatLogActivity
import com.firebase.ui.auth.data.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_book_detail2.*

class Bookdetail2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail2)

        var chatButton = findViewById<Button>(R.id.contact_button)
        chatButton.setOnClickListener{ v ->
            val intent = Intent(this@Bookdetail2Activity, ChatLogActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}