package com.example.projeto_suporte.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityLoginBinding
import com.example.projeto_suporte.databinding.DialogResetPasswordBinding
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

        binding.btnEntrar.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val senha = binding.txtSenha.text.toString().trim()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            verificarTipoUsuario()
                        } else {
                            Toast.makeText(this, "Usuário ou senha inválidos.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCadastro.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        // --- CÓDIGO ATUALIZADO AQUI ---
        binding.tvEsqueciSenha.setOnClickListener {
            mostrarDialogRedefinicaoSenha()
        }
    }

    // --- CÓDIGO ATUALIZADO AQUI ---
    // Esta função agora usa View Binding para o diálogo
    private fun mostrarDialogRedefinicaoSenha() {
        // 1. Infla o layout do diálogo usando sua classe de Binding gerada automaticamente
        val dialogBinding = DialogResetPasswordBinding.inflate(layoutInflater)

        // 2. Cria o construtor do AlertDialog
        val builder = AlertDialog.Builder(this)
            .setTitle("Redefinir Senha")
            .setMessage("Digite o e-mail da sua conta para receber o link de redefinição.")
            // 3. Define a view do diálogo como a view raiz do nosso binding
            .setView(dialogBinding.root)
            // 4. Configura os botões
            .setPositiveButton("Enviar") { dialog, _ ->
                // Acessa o EditText através do binding, sem findViewById!
                val email = dialogBinding.edtEmailReset.text.toString().trim()
                if (email.isNotEmpty()) {
                    enviarEmailRedefinicao(email)
                } else {
                    Toast.makeText(this, "O campo de e-mail não pode estar vazio.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        // 5. Cria e exibe o diálogo
        builder.create().show()
    }

    private fun verificarTipoUsuario() {
        val email = auth.currentUser?.email
        if (email == null) {
            Toast.makeText(this, "Erro: email do usuário não encontrado.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("Usuarios")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents != null && !documents.isEmpty) {
                    val document = documents.documents[0]
                    val tipoUsuarioString = document.getString("tipoUsuario") ?: "cliente"
                    val tipoUsuario = TipoUsuario.fromString(tipoUsuarioString)
                    redirecionarParaTelaCorreta(tipoUsuario)
                } else {
                    Toast.makeText(this, "Dados do usuário não encontrados.", Toast.LENGTH_SHORT).show()
                    redirecionarParaTelaCorreta(TipoUsuario.CLIENTE)
                }
            }
            .addOnFailureListener {
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

    private fun enviarEmailRedefinicao(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SENHA_RESET", "E-mail de redefinição enviado com sucesso.")
                    AlertDialog.Builder(this)
                        .setTitle("Verifique seu E-mail")
                        .setMessage("Um link para redefinir sua senha foi enviado para $email. Por favor, verifique sua caixa de entrada e spam.")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                } else {
                    Log.w("SENHA_RESET", "Falha ao enviar e-mail de redefinição.", task.exception)
                    Toast.makeText(this, "Falha ao enviar o e-mail. Verifique se o e-mail está correto e tente novamente.", Toast.LENGTH_LONG).show()
                }
            }
    }
}
