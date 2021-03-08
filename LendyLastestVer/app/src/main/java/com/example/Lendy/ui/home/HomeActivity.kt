package com.example.Lendy

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
//import com.example.Lendy.databinding.ActivityHomeBinding
import com.example.Lendy.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.nav_view)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_home)
        drawerLayout = binding.drawerLayout
        val navController = Navigation.findNavController(this, R.id.nav_host)
        NavigationUI.setupActionBarWithNavController(this,navController)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host)
        return navController.navigateUp()
    }


}
