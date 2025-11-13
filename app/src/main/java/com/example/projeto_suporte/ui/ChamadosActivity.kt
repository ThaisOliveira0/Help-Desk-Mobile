package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityChamadosBinding
import com.example.projeto_suporte.model.Chamado

class ChamadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChamadosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChamadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listaChamados = listOf(
            Chamado("2548781651", "Problema com a impressora", "21/03/2024"),
            Chamado("2548781652", "Erro no sistema", "22/03/2024"),
            Chamado("2548781653", "Solicitação de acesso", "25/03/2024")
        )

        binding.recyclerChamados.layoutManager = LinearLayoutManager(this)
        binding.recyclerChamados.adapter = ChamadoAdapter(listaChamados)


    }
}