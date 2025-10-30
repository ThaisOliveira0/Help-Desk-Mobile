package com.example.projeto_suporte.ui.chamados

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityAbrirTicketBinding
import com.example.projeto_suporte.ui.chamados.AbrirTicketActivity

class AbrirTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbrirTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbrirTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
