package com.example.projeto_suporte.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityLoginBinding
import com.example.projeto_suporte.ui.chamados.ChamadosActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener {
            val intent = Intent(this, ChamadosActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}
