package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import repository.FirebaseRepository
import sharedPreference.SharedPreference

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val firebaseRepo = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = SharedPreference(this)

        binding.btnLogin.setOnClickListener {

            val usuariInput = binding.etUsuari.text.toString().trim()
            val contrasenyaInput = binding.etContrasenya.text.toString().trim()

            if (usuariInput.isEmpty() || contrasenyaInput.isEmpty()) {
                Toast.makeText(this, "Omple els camps", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val resultat = firebaseRepo.loginUsuari(usuariInput, contrasenyaInput)
                resultat.onSuccess {
                    sharedPref.setSessioUsuari(usuariInput)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    Toast.makeText(this@LoginActivity, "Login correctament", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
                resultat.onFailure {
                    Toast.makeText(this@LoginActivity, "Error: Credencials incorrectes", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegistre.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}