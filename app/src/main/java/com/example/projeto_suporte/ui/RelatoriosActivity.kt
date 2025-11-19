package com.example.projeto_suporte.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityRelatoriosBinding

class RelatoriosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRelatoriosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRelatoriosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val agentes = listOf("João Silva", "Maria Santos", "Pedro Almeida")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, agentes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAgentes.adapter = adapter

        binding.btnVoltar.setOnClickListener {
            finish()
        }


    }
}
