package com.example.lendyproj.ui.login

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
import com.example.lendyproj.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.buttonChoose
import kotlinx.android.synthetic.main.activity_edit.dateEditText
import kotlinx.android.synthetic.main.activity_edit.descriptionEditText
import kotlinx.android.synthetic.main.activity_edit.saveButton
import kotlinx.android.synthetic.main.activity_edit.titleEditText
import java.util.*


class EditActivity : AppCompatActivity() {
    private val database = Firebase.database
    private var filepath: Uri? = null
    internal var storage: FirebaseStorage?=null
    internal var storageReference: StorageReference?=null
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val bookId = intent.getStringExtra("bookId")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("book").child(
            bookId.toString())

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        buttonChoose.setOnClickListener{ v ->
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "SELECT PICURE"), 111)
        }

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val book:Book? = dataSnapshot.getValue(Book::class.java)
                if (book != null) {
                    titleEditText.text = Editable.Factory.getInstance().newEditable(book.title)
                    dateEditText.text = Editable.Factory.getInstance().newEditable(book.author)
                    descriptionEditText.text = Editable.Factory.getInstance().newEditable(book.description)
                    images(Editable.Factory.getInstance().newEditable(book.downloadUri).toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })


        saveButton.setOnClickListener { v ->

            if(filepath!=null) {
                val pd = ProgressDialog(this)
                pd.setTitle("Updating")
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
                        val changedownloadUri = task.result
                        val title: String = titleEditText.text.toString()
                        val date: String = dateEditText.text.toString()
                        val description: String = descriptionEditText.text.toString()
                        val downloadUri: String = changedownloadUri.toString()

                        myRef.child("title").setValue(title)
                        myRef.child("date").setValue(date)
                        myRef.child("description").setValue(description)
                        myRef.child("downloadUri").setValue(downloadUri)
                        finish()
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            } else {

                val title : String = titleEditText.text.toString()
                val date : String = dateEditText.text.toString()
                val description: String = descriptionEditText.text.toString()

                myRef.child("title").setValue(title)
                myRef.child("date").setValue(date)
                myRef.child("description").setValue(description)
                finish()
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==111 && resultCode == Activity.RESULT_OK && data != null) {
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,filepath)
            imageEditBook.setImageBitmap(bitmap)

        }
    }

    fun images(url: String){
        Glide.with(applicationContext)
            .load(url)
            .into(imageEditBook)

//        Glide.with(this)
//            .load(url)
//            .into(backgroundImageView)
    }

}