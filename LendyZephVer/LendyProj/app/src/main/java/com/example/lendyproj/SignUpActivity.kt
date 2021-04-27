package com.example.lendyproj

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.lendyproj.ui.login.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

class SignUpActivity : AppCompatActivity() {

    var databaseReference :  DatabaseReference? = null
    private val database = Firebase.database
    private var filepath: Uri? = null
    val user = MutableLiveData<FirebaseUser>()
    internal var storage:FirebaseStorage?=null
    internal var storageReference: StorageReference?=null
    private lateinit var viewModel: LoginViewModel2
    private lateinit var mAuth: FirebaseAuth
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

//        viewModel = ViewModelProvider(this).get(LoginViewModel2::class.java)
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        val myRefOfid = database.getReference("uid")

        val username=usernameInput.text
        val email= emailInput.text
        val password= passwordInput.text
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val usernamePattern = "[a-zA-Z0-9._]+(?<![_.])\$"

        profilebuttonChoose.setOnClickListener{ v ->
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "SELECT PICURE"), 111)
        }

        registerButton.setOnClickListener {
            if(TextUtils.isEmpty(usernameInput.text.toString())) {
                usernameInput.setError("Please enter username ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(emailInput.text.toString())) {
                emailInput.setError("Please enter email ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(passwordInput.text.toString())) {
                passwordInput.setError("Please enter password ")
                return@setOnClickListener
            } else if(!(usernameInput.text.toString().matches(usernamePattern.toRegex()))) {
                usernameInput.setError("Invalid username")
                return@setOnClickListener
            } else if(usernameInput.text.length < 6 ) {
                usernameInput.setError("Username must contain at least 6 characters.")
                return@setOnClickListener
            } else if(!(emailInput.text.toString().matches(emailPattern.toRegex()))) {
                emailInput.setError("Invalid email address")
                return@setOnClickListener
            } else if(passwordInput.text.length < 8 ) {
                passwordInput.setError("Password must contain at least 8 characters.")
                return@setOnClickListener
            } else {
                Firebase.auth.createUserWithEmailAndPassword(emailInput.text.toString(), passwordInput.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            if(filepath!=null) {
                                val pd = ProgressDialog(this)
                                pd.setTitle("Creating")
                                pd.show()


                                val imageRef =
                                    storageReference!!.child("profileImages/" + UUID.randomUUID()
                                        .toString())
                                val uploadTask = imageRef.putFile(filepath!!)
                                    .addOnSuccessListener {
                                        pd.dismiss()
//                            val generatedFilePath = taskSnapShot.storage.downloadUrl.toString()
//                           val generatedFilePath = imageUrl.getResult().toString()

                                    }
                                    .addOnFailureListener {
                                        pd.dismiss()
                                        Toast.makeText(applicationContext,
                                            "Failed",
                                            Toast.LENGTH_LONG).show()
                                    }
                                    .addOnProgressListener { taskSnapShot ->
                                        val progress =
                                            (100.0 * taskSnapShot.bytesTransferred) / taskSnapShot.totalByteCount
                                        pd.setMessage("" + progress.toInt() + "%...")
                                    }

                                val urlTask = uploadTask.continueWithTask { task ->
                                    if (!task.isSuccessful) {
                                        task.exception?.let {
                                            throw it
                                        }
                                    }
                                    imageRef.downloadUrl
                                }.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val ImageUri = task.result
                                        val pfImageUri = ImageUri.toString()
//                                        val keyId = myRefOfid.push().key.toString()
                                        val uniqId = Firebase.auth.uid
                                        val uId = UserId(username.toString(),
                                            email.toString(),
                                            pfImageUri)
                                        myRefOfid.child(uniqId.toString()).setValue(uId)

                                        finish()
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            } else {
                                val uniqId = Firebase.auth.uid
                                val uId = UserId(username.toString(), email.toString(), "No image")
                                myRefOfid.child(uniqId.toString()).setValue(uId)

                                finish()
                            }


                        } else {
                            Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            profileImage.setImageBitmap(bitmap)

        }
    }
}