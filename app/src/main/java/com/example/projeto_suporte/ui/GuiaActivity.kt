package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityGuiaBinding

class GuiaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuiaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltarGuia.setOnClickListener {
            finish()
        }

        val listaCategorias = listOf(
            GuiaItem("Conectividade", R.drawable.conectividade, "Descrição..."),
            GuiaItem("Hardware", R.drawable.ic_suporte, "Descrição..."),
            GuiaItem("Software/Sistemas", R.drawable.software, "Descrição..."),
            GuiaItem("Contas e Acessos", R.drawable.contas_acessos, "Descrição..."),
            GuiaItem("Periféricos", R.drawable.perifericos, "Descrição..."),
            GuiaItem("Solicitações/Requisições", R.drawable.solicitacoes, "Descrição..."),
            GuiaItem("Email e Comunicação", R.drawable.ic_email, "Descrição..."),
            GuiaItem("Outros", R.drawable.outros, "Descrição...")
        )

        val adapterCategorias = GuiaAdapter(listaCategorias) { abrirModal(it) }
        binding.rvCategorias.layoutManager = LinearLayoutManager(this)
        binding.rvCategorias.adapter = adapterCategorias

        val listaStatus = listOf(
            GuiaItem("Concluído", R.drawable.ic_concluido, "Chamado finalizado"),
            GuiaItem("Em andamento", R.drawable.ic_andamento, "Agente trabalhando"),
            GuiaItem("Atrasado", R.drawable.ic_atrasado, "Prazo excedido"),
            GuiaItem("Não iniciado", R.drawable.ic_nao_iniciado, "Aguardando início")
        )

        val adapterStatus = GuiaAdapter(listaStatus) { abrirModal(it) }
        binding.rvStatus.layoutManager = LinearLayoutManager(this)
        binding.rvStatus.adapter = adapterStatus
    }

    private fun abrirModal(item: GuiaItem) {

    }
}
