package com.example.lendyproj

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.lendyproj.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var lendFragment: LendFragment
    var t = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this,
            R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        drawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.fragment)

//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.bookshelfFragment, R.id.lendFragment, R.id.profileFragment))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.navView, navController)


    }



}
