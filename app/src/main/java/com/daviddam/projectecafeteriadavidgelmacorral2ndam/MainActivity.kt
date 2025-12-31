package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.ActivityMainBinding
import sharedPreference.SharedPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.mainContainer.id) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        setSupportActionBar(binding.toolbar)

        binding.btnLogout.setOnClickListener {
            sharedPref.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val drawerLayout = binding.main
        val navigationView = binding.navigationView
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar, R.string.drawer_obrir, R.string.drawer_tancar
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setupWithNavController(navController)

    }
}