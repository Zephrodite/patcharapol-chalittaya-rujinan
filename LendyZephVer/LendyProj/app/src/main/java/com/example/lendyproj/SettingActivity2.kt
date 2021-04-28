package com.example.lendyproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_setting2.*

class SettingActivity2 : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting2)

        firebaseAuth = FirebaseAuth.getInstance()

        logout2.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, PreLoginActivity::class.java))
        }


    }
}