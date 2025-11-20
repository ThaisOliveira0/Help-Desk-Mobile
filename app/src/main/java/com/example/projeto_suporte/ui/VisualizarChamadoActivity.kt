package com.example.projeto_suporte.ui

import com.example.projeto_suporte.databinding.ActivityVisualizarChamadoBinding
import android.os.Bundle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


class VisualizarChamadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVisualizarChamadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVisualizarChamadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVoltar.setOnClickListener {
            finish()
        }
        binding.btnContato.setOnClickListener {
            val intent = Intent(this, ResponderChamadoActivity::class.java)
            startActivity(intent)
        }



        val numero = intent.getStringExtra("numero")
        val cliente = intent.getStringExtra("cliente")
        val data = intent.getStringExtra("data")
        val descricao = intent.getStringExtra("descricao")


        binding.txtNumeroChamado.text = "Chamado #$numero"
        binding.txtNomeCliente.text = "Cliente: $cliente"
        binding.txtData.text = "Data: $data"
        binding.txtDescricao.text = descricao

    }
}
