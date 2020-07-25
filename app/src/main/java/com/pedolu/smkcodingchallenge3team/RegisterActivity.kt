package com.pedolu.smkcodingchallenge3team

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedolu.smkcodingchallenge3team.data.model.room.UserModel
import com.pedolu.smkcodingchallenge3team.util.tampilToast
import com.pedolu.smkcodingchallenge3team.viewmodel.UserViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var inputName: String
    private lateinit var inputTelp: String
    private lateinit var inputAddress: String
    private lateinit var inputEmail: String
    private lateinit var inputPassword: String
    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private val userViewModel by viewModels<UserViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference
        btnRegister.setOnClickListener { inputValidation() }
    }

    private fun inputValidation() {
        inputName = inpName.text.toString()
        inputEmail = inpEmail.text.toString()
        inputTelp = inpTelp.text.toString()
        inputAddress = inpAddress.text.toString()
        inputPassword = inpPassword.text.toString()
        when {
            inputName.isEmpty() -> {
                inpName.error = "Nama tidak boleh kosong"
                inpName.requestFocus()
            }
            inputEmail.isEmpty() -> {
                inpEmail.error = "Email tidak boleh kosong"
                inpEmail.requestFocus()
            }
            inputTelp.isEmpty() -> {
                inpEmail.error = "Telepon tidak boleh kosong"
                inpEmail.requestFocus()
            }
            inputAddress.isEmpty() -> {
                inpEmail.error = "Alamat tidak boleh kosong"
                inpEmail.requestFocus()
            }
            inputPassword.isEmpty() -> {
                inpPassword.error = "Password tidak boleh kosong"
                inpPassword.requestFocus()
            }
            inputPassword.length < 6 -> {
                inpPassword.error = "Password harus tidak kurang dari 6 karakter"
                inpPassword.requestFocus()
            }
            else -> {
                registerUser(inputEmail, inputPassword)
            }
        }

    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    storeUser()
                    goToMainActivity()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        inpPassword.error = "Password anda terlalu lemah"
                        inpPassword.requestFocus()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        inpEmail.error = "Email Anda tidak valid"
                        inpEmail.requestFocus()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        inpEmail.error = "Email telah digunakan pengguna lain"
                        inpEmail.requestFocus()
                    } catch (e: Exception) {
                        Log.e("error", e.message.toString())
                    }
                }
            }
    }

    private fun storeUser() {
        val uid = auth.currentUser!!.uid
        val User = UserModel(
            inputName,
            inputEmail,
            "",
            inputTelp,
            inputAddress,
            "",
            uid
        )
        ref.child("Users").child(uid).child("Data").setValue(User)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    userViewModel.init(this, uid)
                    userViewModel.addData(User)
                } else {
                    tampilToast(this, "Gagal Membuat Akun, Coba Lagi")
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }

    private fun goToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
}
