package com.example.projeto_suporte.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityRelatoriosBinding
import com.example.projeto_suporte.model.Usuario // Verifique o caminho para seu modelo Usuario
import com.google.firebase.firestore.FirebaseFirestore

class RelatoriosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRelatoriosBinding
    private lateinit var db: FirebaseFirestore
    private var listaDeAgentes: List<Usuario> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRelatoriosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        binding.progressBar.visibility = View.VISIBLE
        binding.contentView.visibility = View.INVISIBLE

        buscarAgentes()

        binding.spinnerAgentes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position >= 0 && position < listaDeAgentes.size) {
                    val agenteSelecionado = listaDeAgentes[position]
                    buscarEstatisticasDoAgente(agenteSelecionado)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.groupStats.visibility = View.GONE
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun buscarAgentes() {
        db.collection("Usuarios")
            .whereEqualTo("tipoUsuario", "agente")
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE

                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhum agente encontrado.", Toast.LENGTH_SHORT).show()
                    binding.contentView.visibility = View.VISIBLE
                    return@addOnSuccessListener
                }

                listaDeAgentes = documents.toObjects(Usuario::class.java)

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaDeAgentes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerAgentes.adapter = adapter

                binding.contentView.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Erro ao carregar agentes: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun buscarEstatisticasDoAgente(agente: Usuario) {
        binding.progressBar.visibility = View.VISIBLE
        binding.groupStats.visibility = View.GONE

        // Define o e-mail
        binding.txtEmailAgente.text = agente.email

        // Zera os contadores
        binding.txtResolvidos.text = "0"
        binding.txtConversasAbertas.text = "0"

        // --- INÍCIO DAS CORREÇÕES ---

        // 1. Busca chamados FECHADOS na coleção "chamados"
        db.collection("Chamados")
            // O ID do agente no seu chamado é "idAgente" e não "agenteId"
            .whereEqualTo("idAgente", agente.id)
            // O status é "Fechado" e não "Resolvido"
            .whereEqualTo("status", "Fechado")
            .get()
            .addOnSuccessListener { resolvidosDocs ->
                binding.txtResolvidos.text = resolvidosDocs.size().toString()

                // 2. Após buscar os chamados, busca os chats ATIVOS em outra coleção
                //    Assumindo que a coleção se chama "chats"
                db.collection("Chats") //
                    .whereEqualTo("agenteId", agente.id)
                    .whereEqualTo("statusChat", "ativo") // <--- CORREÇÃO: Campo e valor corretos
                    .get()
                    .addOnSuccessListener { abertosDocs ->
                        binding.txtConversasAbertas.text = abertosDocs.size().toString()

                        // Esconde o progresso e exibe as estatísticas
                        binding.progressBar.visibility = View.GONE
                        binding.groupStats.visibility = View.VISIBLE
                    }
                    .addOnFailureListener { e ->
                        // Se a busca por chats falhar, ainda mostra o que já carregou
                        binding.progressBar.visibility = View.GONE
                        binding.groupStats.visibility = View.VISIBLE
                        Toast.makeText(this, "Erro ao buscar chats abertos: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Erro ao buscar chamados fechados: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
