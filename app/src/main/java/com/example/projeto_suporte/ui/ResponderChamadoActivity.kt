package com.example.projeto_suporte.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityResponderChamadoBinding

class ResponderChamadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResponderChamadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityResponderChamadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvIdChamado.text = "ID: 124"
        binding.tvNomeUsuario.text = "Usuário: Maria Silva"
        binding.tvCategoria.text = "Categoria: Tecnologia"
        binding.tvDescricaoChamado.text =
            "Meu sistema está travando ao tentar abrir a página X."
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnEnviarResposta.setOnClickListener {
            val resposta = binding.edtResposta.text.toString()

            if (resposta.isBlank()) {
                Toast.makeText(this, "Digite uma resposta!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Resposta enviada!", Toast.LENGTH_SHORT).show()
        }
    }
}
