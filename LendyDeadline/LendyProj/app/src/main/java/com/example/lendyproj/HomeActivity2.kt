package com.example.lendyproj

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController


class HomeActivity2 : AppCompatActivity() {
    lateinit var drawerLayout2: DrawerLayout
    lateinit var homeFragment: HomeFragment
    lateinit var bookShelfFragment: BookshelfFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var lendFragment: LendFragment
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
