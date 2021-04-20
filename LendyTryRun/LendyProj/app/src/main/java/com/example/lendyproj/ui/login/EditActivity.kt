package com.example.lendyproj.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.lendyproj.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.dateEditText
import kotlinx.android.synthetic.main.activity_edit.descriptionEditText
import kotlinx.android.synthetic.main.activity_edit.saveButton
import kotlinx.android.synthetic.main.activity_edit.titleEditText

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef = database.getReference("book").child(
            key.toString())

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val book:Book? = dataSnapshot.getValue(Book::class.java)
                if (book != null) {
                    titleEditText.text = Editable.Factory.getInstance().newEditable(book.title)
                    dateEditText.text = Editable.Factory.getInstance().newEditable(book.date)
                    descriptionEditText.text = Editable.Factory.getInstance().newEditable(book.description)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        saveButton.setOnClickListener { v ->

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