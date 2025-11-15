package com.example.projeto_suporte.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto_suporte.databinding.ActivityHistoricoBinding

class HistoricoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricoBinding
    private lateinit var adapter: HistoricoAdapter

    private val chamados = mutableListOf(
        ItemHistorico.ChamadoItem(1, "10/11/2025", "Software"),
        ItemHistorico.ChamadoItem(2, "09/11/2025", "Hardware"),
        ItemHistorico.ChamadoItem(3, "07/11/2025", "Software"),
        ItemHistorico.ChamadoItem(4, "05/11/2025", "Rede"),
        ItemHistorico.ChamadoItem(5, "10/11/2025", "Software"),
        ItemHistorico.ChamadoItem(6, "09/11/2025", "Hardware"),
        ItemHistorico.ChamadoItem(7, "07/11/2025", "Software"),
        ItemHistorico.ChamadoItem(8, "05/11/2025", "Rede")
    )

    private val categoriasPos = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerHistorico.layoutManager = LinearLayoutManager(this)

        atualizarLista(chamados)

        criarMenuCategorias(chamados)

        binding.edtBuscaData.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                val filtrado = chamados.filter {
                    it.data.contains(text.toString(), ignoreCase = true)
                }
                atualizarLista(filtrado)
            }
        })
    }

    private fun atualizarLista(lista: List<ItemHistorico.ChamadoItem>) {
        val itens = mutableListOf<ItemHistorico>()
        categoriasPos.clear()

        val agrupado = lista.groupBy { it.categoria }

        var index = 0
        for ((categoria, chamados) in agrupado) {
            itens.add(ItemHistorico.CategoriaHeader(categoria))
            categoriasPos[categoria] = index
            index++

            chamados.forEach { item ->
                itens.add(item)
                index++
            }
        }

        adapter = HistoricoAdapter(itens)
        binding.recyclerHistorico.adapter = adapter
    }

    private fun criarMenuCategorias(lista: List<ItemHistorico.ChamadoItem>) {
        binding.menuCategorias.removeAllViews()

        val categorias = lista.map { it.categoria }.distinct()

        categorias.forEach { categoria ->
            val btn = android.widget.TextView(this).apply {
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
                    binding.recyclerHistorico.scrollToPosition(pos)
                }
            }
            binding.menuCategorias.addView(btn)
        }
    }
}
