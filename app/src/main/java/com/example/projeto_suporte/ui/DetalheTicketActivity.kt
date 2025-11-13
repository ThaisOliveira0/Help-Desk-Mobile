package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityDetalheTicketBinding

class DetalheTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalheTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalheTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }
}