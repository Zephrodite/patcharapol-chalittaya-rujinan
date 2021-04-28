package com.example.lendyproj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        resetpassButton.setOnClickListener {
            val email: String = emailForReset.text.toString().trim() { it <= ' '}
            if(TextUtils.isEmpty(emailForReset.text.toString())) {
                emailForReset.setError("Please enter email address ")
                return@setOnClickListener
            } else if(!(email.matches(emailPattern.toRegex()))) {
                emailForReset.setError("Invalid email address")
                return@setOnClickListener
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this@ForgotPassword,
                                "Email sent successfullytt o reset your password!",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@ForgotPassword,
                                task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }
}