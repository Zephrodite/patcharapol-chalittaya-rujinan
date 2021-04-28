package com.example.lendyproj.ui.login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel2 : ViewModel() {


    // TODO: Implement the ViewModel

    val user = MutableLiveData<FirebaseUser>()

    fun login(activity: Activity, email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Test: Task", "ok")
                    task.result?.let {
                        user.value = it.user
                    }
                } else {
                    Log.w("Test Task login", "singInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }

            }
    }

    fun register(activity: Activity, email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    task.result?.let {
                        Log.d("Test Register Success!", it.user?.email ?: "")
                    }
                } else {
                    Log.w("Test Register Error!", "singUpWithEmail:failure", task.exception)
                }
            }

    }

}