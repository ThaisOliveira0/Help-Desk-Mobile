package com.example.projeto_suporte.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityCadastroBinding
import com.example.projeto_suporte.enums.TipoUsuario
import com.example.projeto_suporte.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CadastroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dataInput = binding.txtDataNasc
        dataInput.setOnClickListener {
            showDatePicker()
        }

        val btnCadastrar = binding.btnCadastrar
        btnCadastrar.setOnClickListener {
            realizarCadastro()
        }
    }

    private fun showDatePicker() {
        var calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            {_, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "%02d/%02d/%04d".format(selectedDay, selectedMonth + 1, selectedYear)
                binding.txtDataNasc.setText(formattedDate)
            },
            year, month, day
        )

        datePicker.datePicker.maxDate = System.currentTimeMillis()

        datePicker.show()
    }

    private fun realizarCadastro() {
        val nome = binding.txtNome.text.toString()
        val sobrenome = binding.txtSobrenome.text.toString()
        val email = binding.txtEmail.text.toString()
        val dataNasc = binding.txtDataNasc.text.toString()
        val senha = binding.txtSenha.text.toString()
        val confirmarSenha = binding.txtConfirmarSenha.text.toString()

        if(senha != confirmarSenha){
            return Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
        }

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    var firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid ?: return@addOnCompleteListener
                    val usuario = Usuario(userId, nome, sobrenome,email, dataNasc, TipoUsuario.CLIENTE);

                    addNewUser(usuario);
                }
            }
    }

    private fun addNewUser(usuario: Usuario) {
        db.collection("Usuarios")
            .document(usuario.email)
            .set(usuario)
            .addOnSuccessListener {
                // Dados salvos com sucesso no Firestore
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // Caso ocorra um erro ao salvar no Firestore
                Toast.makeText(this, "Ocorreu um erro ao realizar o cadastro.", Toast.LENGTH_SHORT).show()
                println("Erro ao salvar no Firestore: ${e.message}")
            }

    }

}