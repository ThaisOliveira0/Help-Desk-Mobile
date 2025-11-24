package com.example.projeto_suporte.ui

import android.content.DialogInterface
import com.example.projeto_suporte.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityRemocaoFuncionariosBinding
import com.example.projeto_suporte.model.Usuario // Importe sua data class Usuario
import com.example.projeto_suporte.ui.adapters.RemoverFuncionarioAdapter
import com.google.firebase.firestore.FirebaseFirestore

class RemocaoFuncionariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemocaoFuncionariosBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: RemoverFuncionarioAdapter
    private val listaFuncionarios = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemocaoFuncionariosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        setupUI()
        buscarFuncionarios()
    }

    private fun setupUI() {
        binding.btnVoltar.setOnClickListener { finish() }

        // Inicializa o adapter com uma lista vazia e a lógica de exclusão
        adapter = RemoverFuncionarioAdapter(listaFuncionarios) { funcionario ->
            confirmarExclusao(funcionario)
        }

        binding.recyclerFuncionarios.layoutManager = LinearLayoutManager(this)
        binding.recyclerFuncionarios.adapter = adapter
    }

    private fun buscarFuncionarios() {
        // Mostra um indicador de progresso (opcional, mas recomendado)
        // binding.progressBar.visibility = View.VISIBLE

        db.collection("Usuarios")
            .whereEqualTo("tipoUsuario", "agente")
            .get()
            .addOnSuccessListener { documents ->
                // binding.progressBar.visibility = View.GONE
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhum agente encontrado.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                // Limpa a lista antes de adicionar novos itens
                listaFuncionarios.clear()
                for (document in documents) {
                    val usuario = document.toObject(Usuario::class.java)
                    listaFuncionarios.add(usuario)
                }
                adapter.notifyDataSetChanged() // Notifica o adapter que os dados mudaram
            }
            .addOnFailureListener { exception ->
                // binding.progressBar.visibility = View.GONE
                Log.e("BUSCA_AGENTES", "Erro ao buscar agentes", exception)
                Toast.makeText(this, "Falha ao buscar funcionários.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun confirmarExclusao(usuario: Usuario) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Exclusão")
        builder.setMessage("Você tem certeza que deseja remover o agente ${usuario.nome}?\n\nEsta ação não pode ser desfeita.")
        builder.setIcon(R.drawable.ic_delete) // Ícone para o diálogo

        builder.setPositiveButton("Sim, Excluir") { dialog, _ ->
            excluirFuncionario(usuario)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun excluirFuncionario(usuario: Usuario) {
        // O ID do documento é o email, conforme sua estrutura
        db.collection("Usuarios").document(usuario.email)
            .delete()
            .addOnSuccessListener {
                Log.d("EXCLUSAO_AGENTE", "Agente ${usuario.email} removido do Firestore.")
                Toast.makeText(this, "Agente ${usuario.nome} removido com sucesso!", Toast.LENGTH_SHORT).show()

                // Remove da lista local e atualiza o adapter
                val position = listaFuncionarios.indexOf(usuario)
                if (position != -1) {
                    adapter.removerItem(position)
                }
                AlertDialog.Builder(this)
                    .setTitle("Próximo Passo")
                    .setMessage("O funcionário foi removido da base de dados do app.\n\nLembre-se de excluir também a conta de login (${usuario.email}) no painel do Firebase Authentication para completar a remoção.")
                    .setPositiveButton("Entendi") { d, _ -> d.dismiss() }
                    .show()
            }
            .addOnFailureListener { e ->
                Log.e("EXCLUSAO_AGENTE", "Erro ao remover agente", e)
                Toast.makeText(this, "Falha ao remover agente.", Toast.LENGTH_SHORT).show()
            }
    }
}
