package com.aca.lovalaundry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.aca.lovalaundry.databinding.ActivityRegisterBinding
import com.aca.lovalaundry.helper.SharePref
import com.aca.lovalaundry.model.ResponModel
import com.aca.lovalaundry.network.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    lateinit var shp: SharePref
    private var email : String = ""
    private var name : String = ""
    private var pass : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shp = SharePref(this)
        binding.login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.register.setOnClickListener {
            email = binding.edEmail.text.toString()
            name = binding.edName.text.toString()
            pass = binding.edPassword.text.toString()

            when{
                email == "" -> {
                    binding.edEmail.error = "Email tidak boleh kosong"
                }
                name == "" -> {
                    binding.edName.error = "Name tidak boleh kosong"
                }
                pass == "" -> {
                    binding.edPassword.error = "Password tidak boleh kosong"
                }
                else -> {
                    binding.pbRegister.visibility = View.VISIBLE
                    getData()
                }
            }
        }

    }

    private fun getData() {
        ApiConfig.instanceRetrofit.register(email, name, pass)
            .enqueue(object : Callback<ResponModel> {
                override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                    binding.pbRegister.visibility =View.GONE
                    val respon = response.body()!!
                    if (respon.success == 1) {
                        shp.setStatusLogin(true)
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                        Toast.makeText(
                            this@RegisterActivity, "Akun Terdafar " +respon.message, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity, "Error" +respon.message, Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                    binding.pbRegister.visibility =View.GONE
                    Toast.makeText(this@RegisterActivity, "Error" + t.message, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }
}