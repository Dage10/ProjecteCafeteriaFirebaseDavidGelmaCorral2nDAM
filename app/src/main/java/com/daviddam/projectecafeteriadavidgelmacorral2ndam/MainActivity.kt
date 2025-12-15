package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import sharedPreference.SharedPreference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        val sharedPref = SharedPreference(this)
        if (!sharedPref.estaLogat()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

    }
}