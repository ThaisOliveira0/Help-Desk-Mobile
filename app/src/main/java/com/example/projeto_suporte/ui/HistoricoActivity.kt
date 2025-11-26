package com.example.projeto_suporte.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityHistoricoBinding
import com.example.projeto_suporte.model.Chamado
import com.example.projeto_suporte.ui.models.ItemHistorico
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    // Lista para armazenar todos os chamados buscados do Firestore
    private val todosChamados = mutableListOf<ItemHistorico.ChamadoItem>()
    // Mapa para guardar a posição dos cabeçalhos de categoria na lista
    private val categoriasPos = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupWindowInsets()
        setupUI()
        buscarHistorico()
    }

    private fun setupUI() {
        binding.recyclerHistorico.layoutManager = LinearLayoutManager(this)
        binding.btnBack.setOnClickListener { finish() }

        binding.edtBuscaData.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                // Filtra a lista já carregada na memória
                val filtrado = todosChamados.filter {
                    it.data.contains(text.toString(), ignoreCase = true)
                }
                atualizarLista(filtrado)
            }
        })
    }

    private fun buscarHistorico() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        db.collection("Chamados")
            .whereEqualTo("status", "Fechado") // Filtra APENAS pelos chamados finalizados
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Nenhum histórico encontrado.", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                todosChamados.clear()
                for (document in documents) {
                    val chamado = document.toObject(Chamado::class.java)
                    todosChamados.add(
                        ItemHistorico.ChamadoItem(
                            id = chamado.id,
                            data = chamado.dataAbertura,
                            categoria = chamado.categoria,
                            numeroChamado = chamado.numeroChamado
                        )
                    )
                }
                // Ordena por data (opcional, mas recomendado)
                todosChamados.sortByDescending { it.data }

                // Atualiza a lista e o menu de categorias
                atualizarLista(todosChamados)
                criarMenuCategorias(todosChamados)
            }
            .addOnFailureListener { exception ->
                Log.e("HISTORICO", "Erro ao buscar histórico", exception)
                Toast.makeText(this, "Falha ao carregar histórico.", Toast.LENGTH_SHORT).show()
            }
    }

    // Esta função agora processa a lista e exibe no RecyclerView
    private fun atualizarLista(lista: List<ItemHistorico.ChamadoItem>) {
        val itensAgrupados = mutableListOf<ItemHistorico>()
        categoriasPos.clear()

        val agrupadoPorCategoria = lista.groupBy { it.categoria }

        var index = 0
        for ((categoria, chamadosDaCategoria) in agrupadoPorCategoria) {
            itensAgrupados.add(ItemHistorico.CategoriaHeader(categoria))
            categoriasPos[categoria] = index
            index++

            chamadosDaCategoria.forEach { item ->
                itensAgrupados.add(item)
                index++
            }
        }

        binding.recyclerHistorico.adapter = HistoricoAdapter(itensAgrupados)
    }

    // Esta função cria os botões de atalho para cada categoria
    private fun criarMenuCategorias(lista: List<ItemHistorico.ChamadoItem>) {
        binding.menuCategorias.removeAllViews()
        val categoriasUnicas = lista.map { it.categoria }.distinct()

        categoriasUnicas.forEach { categoria ->
            val btn = TextView(this).apply {
                text = categoria
                textSize = 16f
                setPadding(24, 12, 24, 12)
                setBackgroundColor(Color.parseColor("#A8B5E0"))
                setTextColor(Color.WHITE)
                val lp = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(8, 0, 8, 0)
                layoutParams = lp

                setOnClickListener {
                    val pos = categoriasPos[categoria] ?: 0
                    (binding.recyclerHistorico.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(pos, 0)
                }
            }
            binding.menuCategorias.addView(btn)
        }
    }

    private fun setupWindowInsets() {
        //enableEdgeToEdge() // Removido se não for necessário
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
