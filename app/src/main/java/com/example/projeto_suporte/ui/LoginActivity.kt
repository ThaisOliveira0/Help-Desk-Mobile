package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityLoginBinding
import com.example.projeto_suporte.enums.TipoUsuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val botaoEntrar = binding.btnEntrar
        botaoEntrar.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val senha = binding.txtSenha.text.toString().trim()


            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            verificarTipoUsuario()
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

    private fun verificarTipoUsuario() {
        // 1. Pega o email do usuário autenticado.
        val email = auth.currentUser?.email
        if (email == null) {
            Toast.makeText(this, "Erro: email do usuário não encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Faz uma consulta na coleção "Usuarios" buscando pelo campo "email".
        db.collection("Usuarios")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                // 3. Verifica se a consulta retornou algum documento.
                if (documents != null && !documents.isEmpty) {
                    // Pega o primeiro documento encontrado (deve ser apenas um).
                    val document = documents.documents[0]
                    val tipoUsuarioString = document.getString("tipoUsuario") ?: "cliente"

                    val tipoUsuario = TipoUsuario.fromString(tipoUsuarioString)

                    redirecionarParaTelaCorreta(tipoUsuario)
                } else {
                    Toast.makeText(this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT).show()
                    redirecionarParaTelaCorreta(TipoUsuario.CLIENTE)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Falha ao buscar dados do usuário.", Toast.LENGTH_SHORT).show()
                redirecionarParaTelaCorreta(TipoUsuario.CLIENTE)
            }
    }

    private fun redirecionarParaTelaCorreta(tipoUsuario: TipoUsuario) {
        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

        val intent = when (tipoUsuario) {
            TipoUsuario.ADMIN -> Intent(this, MenuAdminActivity::class.java)
            TipoUsuario.AGENTE -> Intent(this, MenuAgenteActivity::class.java)
            TipoUsuario.CLIENTE -> Intent(this, MenuUsuarioActivity::class.java)
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}