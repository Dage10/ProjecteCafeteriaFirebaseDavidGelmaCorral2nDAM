package com.daviddam.projectecafeteriadavidgelmacorral2ndam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.daviddam.projectecafeteriadavidgelmacorral2ndam.databinding.ActivityLoginBinding
import sharedPreference.SharedPreference

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

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

            if (sharedPref.validarCredencials(usuariInput, contrasenyaInput)) {
                sharedPref.setSessioUsuari(usuariInput)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER", usuariInput)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuari o contrasenya incorrectes", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegistre.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}