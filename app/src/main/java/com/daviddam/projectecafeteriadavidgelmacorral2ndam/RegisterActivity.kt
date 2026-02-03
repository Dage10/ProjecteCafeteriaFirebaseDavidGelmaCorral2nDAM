package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import repository.FirebaseRepository

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val firebaseRepo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistre.setOnClickListener {
            val usuari = binding.etUsuari.text.toString().trim()
            val contrasenya = binding.etContrasenya.text.toString().trim()

            if (usuari.isEmpty() || contrasenya.isEmpty()) {
                android.widget.Toast.makeText(this, "Omple usuari i contrasenya", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val resultat = firebaseRepo.registrarUsuari(usuari, usuari, contrasenya)
                resultat.onSuccess {
                    android.widget.Toast.makeText(this@RegisterActivity, "Usuari registrat", android.widget.Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                resultat.onFailure {
                    android.widget.Toast.makeText(this@RegisterActivity, "Error: aquest usuari ja existeix", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}