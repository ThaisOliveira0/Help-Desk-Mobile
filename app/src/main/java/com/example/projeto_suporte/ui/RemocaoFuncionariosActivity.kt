package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityRemocaoFuncionariosBinding
import com.example.projeto_suporte.models.Funcionario
import com.example.projeto_suporte.ui.adapters.RemoverFuncionarioAdapter

class RemocaoFuncionariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemocaoFuncionariosBinding
    private lateinit var adapter: RemoverFuncionarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRemocaoFuncionariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener { finish() }

        val listaFuncionarios = mutableListOf(
            Funcionario(1, "Maria"),
            Funcionario(2, "João"),
            Funcionario(3, "Ana"),
        )

        adapter = RemoverFuncionarioAdapter(listaFuncionarios) { funcionario ->
            println("Remover ${funcionario.nome}")
        }


        // Configurar RecyclerView
        binding.recyclerFuncionarios.layoutManager = LinearLayoutManager(this)
        binding.recyclerFuncionarios.adapter = adapter
    }
}
