package com.aca.lovalaundry

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aca.lovalaundry.databinding.ActivityLoginBinding
import com.aca.lovalaundry.helper.SharePref

import com.aca.lovalaundry.model.ResponModel
import com.aca.lovalaundry.network.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    lateinit var shp :SharePref
    private var email : String = ""
    private var pass : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shp = SharePref(this)
        binding.register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
        binding.login.setOnClickListener {
            email = binding.edEmail.text.toString()
            pass = binding.edPassword.text.toString()

            when{
                email == "" -> {
                    binding.edEmail.error = "Email tidak boleh kosong"
                }
                pass == "" -> {
                    binding.edPassword.error = "Password tidak boleh kosong"
                }
                else -> {
                    binding.pbLogin.visibility = View.VISIBLE
                    getData()
                }
            }
        }


    }

    private fun getData(){
        ApiConfig.instanceRetrofit.login(email, pass).enqueue(object : Callback<ResponModel> {
                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    binding.pbLogin.visibility =View.GONE
                    val respon = response.body()!!
                    if (respon.success == 1) {
                        shp.setStatusLogin(true)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        Toast.makeText(
                            this@LoginActivity, "Selamat Datang " +respon.user.name, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this@LoginActivity, "Error" +respon.message, Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                    binding.pbLogin.visibility =View.GONE
                    Toast.makeText(this@LoginActivity, "Error" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }

            })

    }

}