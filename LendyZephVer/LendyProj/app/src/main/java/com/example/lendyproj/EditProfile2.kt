package com.example.lendyproj

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.lendyproj.ui.login.Book
import com.example.lendyproj.ui.login.UserId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.imageEditBook
import kotlinx.android.synthetic.main.activity_edit.saveButton
import kotlinx.android.synthetic.main.activity_edit.titleEditText
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile2.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.usernameInput
import java.util.*

class EditProfile2 : AppCompatActivity() {

    private val database = Firebase.database
    private var filepath: Uri? = null
    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null
    private lateinit var mAuth: FirebaseAuth
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val currentUserid = intent.getStringExtra("currentUserid")
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef1 = database.getReference("uid").child(
            currentUserid.toString())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val uidRef = database.getReference("uid")

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        mAuth = FirebaseAuth.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val uniqId = Firebase.auth.uid


        uidRef.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    val map = snapshot.getValue() as Map<*, *>
                    if(map.toString() == currentUserid.toString()) {
                        buttonProfileChoose1.setOnClickListener{ v ->
                            val intent = Intent()
                            intent.setType("image/*")
                            intent.setAction(Intent.ACTION_GET_CONTENT)
                            startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), 111)
                        }

                        uidRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                val userid: UserId? = dataSnapshot.getValue(UserId::class.java)
                                if (userid != null) {
                                    if (Editable.Factory.getInstance().newEditable(userid.profileImageUri).toString() != "No image"){
                                        images(Editable.Factory.getInstance().newEditable(userid.profileImageUri).toString())
                                    }

                                    usernameEditText1.text = Editable.Factory.getInstance().newEditable(userid.username)

                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.w("TAG", "Failed to read value.", error.toException())
                            }
                        })


                        saveButton1.setOnClickListener { v ->
                            val username=usernameEditText1.text
                            if(filepath!=null) {
                                val pd = ProgressDialog(this@EditProfile2)
                                pd.setTitle("Updating...")
                                pd.show()


                                val imageRef = storageReference!!.child("profileImages/" + UUID.randomUUID().toString())
                                val uploadTask = imageRef.putFile(filepath!!)
                                    .addOnSuccessListener {
                                        pd.dismiss()
//                            val generatedFilePath = taskSnapShot.storage.downloadUrl.toString()
//                           val generatedFilePath = imageUrl.getResult().toString()

                                    }
                                    .addOnFailureListener {
                                        pd.dismiss()
                                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
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
                                        val profileImgUri = task.result
                                        val username: String = usernameEditText1.text.toString()

                                        uidRef.child("profileImageUri").setValue(profileImgUri.toString())
                                        uidRef.child("username").setValue(username)
                                        finish()
                                    } else {
                                        // Handle failures
                                        // ...
                                    }
                                }
                            } else {

                                val username: String = usernameEditText1.text.toString()

                                uidRef.child("username").setValue(username)
                                finish()
                            }

                        }

                    }


                }

                buttonProfileChoose1.setOnClickListener{ v ->
                    val intent = Intent()
                    intent.setType("image/*")
                    intent.setAction(Intent.ACTION_GET_CONTENT)
                    startActivityForResult(Intent.createChooser(intent, "SELECT PICTURE"), 111)
                }
                saveButton1.setOnClickListener { v ->
                    val username=usernameEditText1.text
                    if(filepath!=null) {
                        val pd = ProgressDialog(this@EditProfile2)
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
                                val uId = UserId(username.toString(), currentUserid.toString(), pfImageUri)
                                uidRef.child(uniqId.toString()).setValue(uId)

                                finish()
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    } else {
                        val uId = UserId(username.toString(), currentUserid.toString(), "No image")
                        uidRef.child(uniqId.toString()).setValue(uId)

                        finish()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imageEditProfile1.setImageBitmap(bitmap)

        }
    }

    fun images(url: String){
        Glide.with(applicationContext)
            .load(url)
            .into(imageEditProfile1)

//        Glide.with(this)
//            .load(url)
//            .into(backgroundImageView)
    }
}