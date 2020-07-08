package com.pedolu.smkcodingchallenge3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedolu.smkcodingchallenge3.data.model.room.RelawanModel
import kotlinx.android.synthetic.main.activity_volunteer_register.*

class VolunteerRegister : AppCompatActivity() {

    private lateinit var nama : String
    private lateinit var email : String
    private lateinit var telp : String
    private lateinit var alamat : String
    private lateinit var uid : String
    private lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_register)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference

        register.setOnClickListener { validate() }
        back.setOnClickListener { backToRegister() }
    }

    private fun validate() {

        nama = edtName.text.toString()
        email = edtEmail.text.toString()
        telp = edtTelp.text.toString()
        alamat = edtAlamat.text.toString()
        uid = auth?.currentUser?.uid.toString()

        if (nama.isEmpty() && email.isEmpty() && telp.isEmpty() && alamat.isEmpty()) {
            Toast.makeText(this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            val relawan = RelawanModel (
                nama,
                email,
                telp,
                alamat,
                uid
            )

            ref.child(uid).child("Relawan").push().setValue(relawan).addOnCompleteListener {
                Toast.makeText(this, "Data telah disimpan", Toast.LENGTH_SHORT).show()
            }

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