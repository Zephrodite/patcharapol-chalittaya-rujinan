package com.example.lendyproj.ui.login

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.lendyproj.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity : AppCompatActivity() {

    private val database = Firebase.database
    private var filepath: Uri? = null
    internal var storage:FirebaseStorage?=null
    internal var storageReference: StorageReference?=null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val myRef = database.getReference("book")

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        buttonChoose.setOnClickListener{ v ->
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "SELECT PICURE"), 111)
        }

        val title=titleEditText.text
        val date= dateEditText.text
        val description=descriptionEditText.text

        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        val currentUid = currentUser?.uid.toString()

        saveButton.setOnClickListener { v ->
            if(filepath!=null) {
                val pd = ProgressDialog(this)
                pd.setTitle("Adding")
                pd.show()


                val imageRef = storageReference!!.child("images/" + UUID.randomUUID().toString())
                val uploadTask = imageRef.putFile(filepath!!)
                        .addOnSuccessListener {
                            pd.dismiss()
//                            val generatedFilePath = taskSnapShot.storage.downloadUrl.toString()
//                           val generatedFilePath = imageUrl.getResult().toString()

                        }
                        .addOnFailureListener {
                            pd.dismiss()
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                        }
                        .addOnProgressListener { taskSnapShot ->
                            val progress = (100.0 * taskSnapShot.bytesTransferred) / taskSnapShot.totalByteCount
                            pd.setMessage("uploaded " +progress.toInt() + "%...")
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
                        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                        val bookId = (1..40).map { allowedChars.random() }.joinToString("")
                        val downloadUri = task.result
                        val book = Book(title.toString(), date.toString(), description.toString(), downloadUri.toString(), currentUid, bookId)
                        myRef.child(bookId).setValue(book)
                        finish()
                    } else {
                        // Handle failures
                        // ...
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
            imageBook.setImageBitmap(bitmap)

        }
    }
}