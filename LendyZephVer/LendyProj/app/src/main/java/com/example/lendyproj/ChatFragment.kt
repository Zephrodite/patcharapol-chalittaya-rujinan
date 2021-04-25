package com.example.lendyproj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_chat.view.*

class ChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var chatFragment: ChatFragment
    private val database1 = Firebase.database
    val myRef1 = database1.getReference("book")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val firebaseQuery = myRef1.orderByChild("title").startAt("A").endAt("A" + "\uf8ff")
//        view.testTV.text = firebaseQuery.toString()
        return view



    }
}