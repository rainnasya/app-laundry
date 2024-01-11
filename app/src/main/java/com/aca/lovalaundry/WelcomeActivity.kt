package com.aca.lovalaundry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aca.lovalaundry.databinding.ActivityWelcomeBinding

import com.aca.lovalaundry.helper.SharePref

class WelcomeActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityWelcomeBinding

    private lateinit var  sph: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sph = SharePref(this)
        mainButton()

    }

    private fun mainButton() {
        binding.btnMasuk.setOnClickListener {
            sph.setStatusLogin(true)
            startActivity(Intent(this, LoginActivity::class.java))
//
        }

        binding.btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}