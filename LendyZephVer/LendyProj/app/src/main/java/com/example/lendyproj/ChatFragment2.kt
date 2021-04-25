package com.example.lendyproj

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ChatFragment2 : Fragment() {

    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var chatFragment: ChatFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_chat2, container, false)

        return view



    }

}