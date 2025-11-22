package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityMenuAdminBinding

class MenuAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReports.setOnClickListener {
            val intent = Intent(this, RelatoriosActivity::class.java)
            startActivity(intent)
        }
        binding.btnHistory.setOnClickListener {
            val intent = Intent(this, HistoricoActivity::class.java)
            startActivity(intent)
        }
        binding.btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroFuncionarioActivity::class.java)
            startActivity(intent)
        }
        binding.btnRemoverFuncionario.setOnClickListener {
            val intent = Intent(this, RemocaoFuncionariosActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
