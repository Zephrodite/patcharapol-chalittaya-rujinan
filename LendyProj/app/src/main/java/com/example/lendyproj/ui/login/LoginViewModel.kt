package com.example.lendyproj.ui.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import com.example.lendyproj.data.LoginRepository
import com.example.lendyproj.data.Result

import com.example.lendyproj.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(email, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (!isemailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder email validation check
    private fun isemailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    val user = MutableLiveData<FirebaseUser>()

    fun login(activity: Activity, email:String, password: String){
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Test Task;","ok")
                    task.result?.let{
                        user.value = task.result?.user
                    }
                }else{
                    Log.w("Test task login","singInWithEmail:Failure", task.exception)
                    Toast.makeText(activity,"Authemtication failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    fun register (activity: Activity, email: String, password: String){
        Firebase.auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(activity){task ->
                if(task.isSuccessful){
                    task.result?.let {
                        Log.w("Test Register Success",it.user?.email ?: "")
                    }
                }else{
                    Log.w("Test Register Error:","signUpWithEmail:failure", task.exception)
                }
            }
    }

    fun firebaseAuthWithGoogle(activity: Activity, IDToken: String){
        val credential = GoogleAuthProvider.getCredential(IDToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Test google auth", "signInWithCredential:succes")
                    task.result?.let{
                        user.value = it.user
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Test google auth", "signInWithCredential:failure",task.exception)
                }
            }
    }
    fun firebaseSignOut(googleSignInClient: GoogleSignInClient){
        Firebase.auth.signOut()
        googleSignInClient.signOut()
    }

}