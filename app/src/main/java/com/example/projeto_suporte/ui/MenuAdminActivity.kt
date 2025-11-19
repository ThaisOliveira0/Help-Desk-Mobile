package com.example.projeto_suporte.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projeto_suporte.databinding.ActivityMenuAdminBinding

class MenuAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
