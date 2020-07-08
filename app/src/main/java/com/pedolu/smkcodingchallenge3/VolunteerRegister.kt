package com.pedolu.smkcodingchallenge3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_volunteer_register.*

class VolunteerRegister : AppCompatActivity() {

    private lateinit var nama : String
    private lateinit var email : String
    private lateinit var telp : String
    private lateinit var alamat : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_register)

        register.setOnClickListener { validate() }
        back.setOnClickListener { backToRegister() }
    }

    private fun validate() {

        nama = edtName.text.toString()
        email = edtEmail.text.toString()
        telp = edtTelp.text.toString()
        alamat = edtAlamat.text.toString()

        if (nama.isEmpty() && email.isEmpty() && telp.isEmpty() && alamat.isEmpty()) {
            Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }

    }

    private fun backToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right)
        finish()
    }
}