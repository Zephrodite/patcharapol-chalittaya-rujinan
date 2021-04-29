package com.example.lendyproj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lendyproj.databinding.ActivityHomeBinding as ActivityHomeBinding1


class HomeActivity2 : AppCompatActivity() {
    lateinit var drawerLayout2: DrawerLayout
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var chatFragment: ChatFragment
    var t = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding = DataBindingUtil.setContentView<ActivityHomeBinding1>(this,
//            R.layout.activity_home2)
        setContentView(R.layout.activity_home2)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation2)
//        drawerLayout2 = binding.drawerLayout
        val navController = findNavController(R.id.fragment)

//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.bookshelfFragment, R.id.lendFragment, R.id.profileFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
//        NavigationUI.setupWithNavController(binding.navView, navController)



    }



}
