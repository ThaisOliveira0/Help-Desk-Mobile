package com.example.projeto_suporte.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityAbrirTicketBinding
import com.example.projeto_suporte.model.Chamado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.format

class AbrirTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbrirTicketBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbrirTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario == null) {
            finish()
            return
        } else {
            binding.txtSaudacao.text = "Olá, ${usuario.displayName}"
        }

        setupSpinner()

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnEnviar.setOnClickListener {
            abrirNovoChamado()
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.opcoes_categorias,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.categoriaSpinner.adapter = adapter
        }
    }

    private fun abrirNovoChamado() {
        val categoria = binding.categoriaSpinner.selectedItem.toString()
        val descricao = binding.txtDescricao.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val chamadosRef = db.collection("Chamados")

        val novoChamadoId = chamadosRef.document().id

        val numeroChamadoLegivel = SimpleDateFormat("yyyyMMddHHmmss",
            Locale.getDefault()).format(Date())

        val novoChamado = Chamado(
            id = novoChamadoId,
            numeroChamado = numeroChamadoLegivel,
            categoria = categoria,
            descricao = descricao,
            dataAbertura = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                Date()
            ),
            userId = userId
        )

        chamadosRef.document(novoChamadoId).set(novoChamado)
            .addOnSuccessListener {
                Toast.makeText(this, "Chamado #${numeroChamadoLegivel} aberto com sucesso!", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao abrir chamado.", Toast.LENGTH_SHORT).show()
            }
    }
}