package com.example.lendyproj

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.lendyproj.ui.login.LoginActivity
import com.example.lendyproj.ui.login.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import android.app.Activity

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lendyproj.ui.login.LoginViewModelFactory
import com.example.lendyproj.HomeActivity
import com.example.lendyproj.R
import com.example.lendyproj.SignUpActivity
import com.example.lendyproj.ui.login.UserId
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_pre_login.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class PreLoginActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 202
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel

    lateinit var viewModel: LoginViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    val user = MutableLiveData<FirebaseUser>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //Firebase Auth instance
        mAuth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            signIn()
        }

        login_with_email_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("Test Google Auth", "firebaseAuthWithGoogle:" + account.id)
                account.idToken?.let {
                    firebaseAuthWithGoogle(this, it)

                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Test Google Auth", "Google sign in failed", e)
            }
        }
    }
    fun firebaseAuthWithGoogle(activity: Activity, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Test Google Auth", "signInWithCredential:success")
                    task.result?.let {
                        user.value = it.user
                    }

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Test Google Auth", "signInWithCredential:failure", task.exception)

                }
            }
    }
}