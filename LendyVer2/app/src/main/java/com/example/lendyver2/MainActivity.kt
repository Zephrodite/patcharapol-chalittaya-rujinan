package com.example.lendyver2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home.view.*


class MainActivity : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookShelfFragment
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.nav_view)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_bookshelf -> {
                    bookShelfFragment = BookShelfFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, bookShelfFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.navigation_profile -> {
                    profileFragment = ProfileFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            }
            true
        }

    }
}