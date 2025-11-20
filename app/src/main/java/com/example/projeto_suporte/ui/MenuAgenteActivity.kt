package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityMenuAgenteBinding
import com.example.projeto_suporte.model.Chamado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MenuAgenteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuAgenteBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var chamadoAdapter: ChamadoAdapter
    private var isInitialSpinnerSelection = true // flag para controlar o primeiro disparo do spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAgenteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        setupSpinner()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        // carrega os dados sempre que a tela fica visível
        isInitialSpinnerSelection = true // reseta a flag do spinner
        carregarChamadosAbertos(null) // carrega todos os chamados por padrão
        carregarDadosDoAgente()
    }

    private fun setupListeners() {
        // listener para o botão de limpar filtro
        binding.btnLimparFiltroCategoria.setOnClickListener {
            // carrega os dados sem filtro.
            carregarChamadosAbertos(null)

            isInitialSpinnerSelection = true
            binding.spinnerCategoria.setSelection(0, false)

            Toast.makeText(this, "Filtro de categoria limpo", Toast.LENGTH_SHORT).show()
        }

        binding.btnGuia.setOnClickListener {
            startActivity(Intent(this, GuiaActivity::class.java))
        }

        binding.btnChat.setOnClickListener {
            startActivity(Intent(this, ChatsAgenteActivity::class.java))
        }
    }

    private fun setupSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.opcoes_categorias,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategoria.adapter = adapter
        }

        binding.spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Evita que o filtro seja aplicado na primeira vez que a tela abre
                if (isInitialSpinnerSelection) {
                    isInitialSpinnerSelection = false
                    return
                }

                val categoriaSelecionada = parent?.getItemAtPosition(position).toString()
                carregarChamadosAbertos(categoriaSelecionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun carregarDadosDoAgente() {
        val agenteId = auth.currentUser?.uid

        if (agenteId == null) {
            binding.txtSaudacao.text = "Olá, Agente!"
            binding.txtChamadosFinalizados.text = "Chamados finalizados: 0"
            Log.w("MenuAgente", "Agente não autenticado.")
            return
        }

        buscarNomeDoAgente(agenteId)
        contarChamadosDoAgente(agenteId)
    }

    private fun buscarNomeDoAgente(agenteId: String) {
        db.collection("Usuarios")
            .whereEqualTo("id", agenteId)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val nomeAgente = documents.documents[0].getString("nome") ?: "Agente"
                    binding.txtSaudacao.text = "Olá, $nomeAgente!"
                } else {
                    binding.txtSaudacao.text = "Olá, Agente!"
                    Log.w("MenuAgente", "Agente com id $agenteId não encontrado na coleção Usuarios.")
                }
            }
            .addOnFailureListener {
                binding.txtSaudacao.text = "Olá, Agente!"
                Log.e("MenuAgente", "Falha ao buscar nome do agente.", it)
            }
    }

    private fun contarChamadosDoAgente(agenteId: String) {
        db.collection("Chamados")
            .whereEqualTo("idAtendente", agenteId)
            .get()
            .addOnSuccessListener { documents ->
                val quantidade = documents.size()
                binding.txtChamadosFinalizados.text = "Chamados finalizados: $quantidade"
            }
            .addOnFailureListener {
                binding.txtChamadosFinalizados.text = "Chamados finalizados: -"
                Log.e("MenuAgente", "Falha ao contar chamados do agente.", it)
            }
    }

    private fun setupRecyclerView() {
        chamadoAdapter = ChamadoAdapter(emptyList()) { chamado ->
            val intent = Intent(this, VisualizarChamadoActivity::class.java)
            intent.putExtra("chamado_id", chamado.id)
            intent.putExtra("user_id", chamado.userId)
            startActivity(intent)
        }
        binding.recyclerChamados.layoutManager = LinearLayoutManager(this)
        binding.recyclerChamados.adapter = chamadoAdapter
    }

    private fun carregarChamadosAbertos(categoria: String? = null) {
        var query: Query = db.collection("Chamados")
            .whereEqualTo("status", "Aberto")

        if (!categoria.isNullOrEmpty()) {
            query = query.whereEqualTo("categoria", categoria)
        }

        query.orderBy("numeroChamado", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val listaChamados = if (documents != null && !documents.isEmpty) {
                    documents.map { document ->
                        val chamado = document.toObject(Chamado::class.java)
                        chamado.copy(id = document.id)
                    }
                } else {
                    emptyList()
                }
                chamadoAdapter.updateData(listaChamados)
            }
            .addOnFailureListener { exception ->
                chamadoAdapter.updateData(emptyList())
                Toast.makeText(this, "Erro ao buscar chamados.", Toast.LENGTH_SHORT).show()
                Log.w("Firestore", "Erro ao buscar documentos: ", exception)
            }
    }
}
