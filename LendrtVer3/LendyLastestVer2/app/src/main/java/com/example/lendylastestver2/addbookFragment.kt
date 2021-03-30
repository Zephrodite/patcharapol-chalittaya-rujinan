package com.example.lendylastestver2

import android.content.Context
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.io.FileNotFoundException
import java.io.IOException


class addbookFragment(val content: Context) : Fragment() {

    val REQUEST_GALLERY = 1
    var bitmap: Bitmap? = null
    var imageView1: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageView1 = view?.findViewById(com.example.lendylastestver2.R.id.imageView2) as ImageView
        val buttonIntent: Button = view?.findViewById(com.example.lendylastestver2.R.id.addpic) as Button
        buttonIntent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(
                        intent, "Select Picture"
                    ), REQUEST_GALLERY
                )
            }
        })
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(content.getContentResolver(), uri)
                imageView1!!.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(com.example.lendylastestver2.R.layout.fragment_addbook, container, false)
    }

}