package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.projeto_suporte.ui.VisualizarChamadoActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val botaoEntrar = binding.btnEntrar
        botaoEntrar.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val senha = binding.txtSenha.text.toString().trim()


            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, VisualizarChamadoActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
        val botaoCadastro = binding.btnCadastro
        botaoCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}