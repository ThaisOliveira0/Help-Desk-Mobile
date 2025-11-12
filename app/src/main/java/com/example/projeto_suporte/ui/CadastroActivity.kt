package com.example.projeto_suporte.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projeto_suporte.R
import com.example.projeto_suporte.databinding.ActivityCadastroBinding
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
    }
    private fun addNewUser(nome: String, email: String, idade: Int) {
        // 2. Prepare os dados como um Map ou um objeto de dados (data class)
        val user = hashMapOf(
            "nome" to nome,
            "email" to email,
            "idade" to idade,
            "timestampCriacao" to com.google.firebase.firestore.FieldValue.serverTimestamp() // Adiciona um timestamp do servidor
        )
        // 3. Adicione os dados à coleção "Usuarios"
        // O método .add() gera um ID de documento automático para você
        db.collection("Usuarios")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Documento adicionado com ID: ${documentReference.id}")
                // Aqui você pode adicionar lógica de sucesso, como mostrar uma mensagem
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Erro ao adicionar documento", e)
                // Aqui você pode adicionar lógica de erro
            }
    }
    companion object {
        private const val TAG = "FirestoreHelper"
    }

}