package com.example.lendylastestver2

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.lendylastestver2.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this,
                R.layout.activity_home)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        drawerLayout = binding.drawerLayout
        val navController = Navigation.findNavController(this, R.id.frame_layout)
        NavigationUI.setupWithNavController(binding.navView, navController)

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
                    bookShelfFragment = BookshelfFragment()
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
