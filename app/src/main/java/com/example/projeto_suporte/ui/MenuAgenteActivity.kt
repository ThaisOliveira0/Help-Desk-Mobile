package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityMenuAgenteBinding
import com.example.projeto_suporte.model.Chamado

class MenuAgenteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuAgenteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAgenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listaChamados = listOf(
            Chamado(
                numeroChamado = "2548781651",
                descricao = "Problema com a impressora",
                dataAbertura = "21/03/2024"
            ),
            Chamado(
                numeroChamado = "2548781652",
                descricao = "Erro no sistema",
                dataAbertura = "22/03/2024"
            ),
            Chamado(
                numeroChamado = "2548781653",
                descricao = "Solicitação de acesso",
                dataAbertura = "25/03/2024"
            )
        )

        binding.recyclerChamados.layoutManager = LinearLayoutManager(this)
        binding.recyclerChamados.adapter = ChamadoAdapter(listaChamados) { chamado ->
            val intent = Intent(this, VisualizarChamadoActivity::class.java)
            intent.putExtra("chamado", chamado)
            startActivity(intent)
        }

        binding.btnGuia.setOnClickListener {
            startActivity(Intent(this, GuiaActivity::class.java))
        }

        binding.btnChat.setOnClickListener {
            startActivity(Intent(this, ChatsAgenteActivity::class.java))
        }
    }
}
