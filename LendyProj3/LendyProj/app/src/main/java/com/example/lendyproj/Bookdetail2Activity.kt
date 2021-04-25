package com.example.lendyproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_book_detail2.*

class Bookdetail2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail2)

        var lendButton = findViewById<Button>(R.id.contact_button)
        lendButton.setOnClickListener{ v ->
            val intent = Intent(this@Bookdetail2Activity, LendbookActivity::class.java)
            startActivity(intent)
        }
    }
}