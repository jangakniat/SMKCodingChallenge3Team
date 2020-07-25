package com.pedolu.smkcodingchallenge3team

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedolu.smkcodingchallenge3team.data.model.room.UserModel
import com.pedolu.smkcodingchallenge3team.viewmodel.UserViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var inputEmail: String
    private lateinit var inputPassword: String
    private lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference
    private val RC_SIGN_IN = 1
    private val viewModel by viewModels<UserViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Users")
        btnGoogleLogin.setOnClickListener { GoogleLogin() }
        btnLogin.setOnClickListener { inputValidation() }
    }



    private fun GoogleLogin() {
        progressBar.visibility = View.VISIBLE
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                storeUser()
                goToMainActivity()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeUser() {
        val user = auth.currentUser
        val uid = auth.currentUser!!.uid
        val User = UserModel(
            user!!.displayName.toString(),
            "",
            "",
            user.phoneNumber.toString(),
            "",
            user.photoUrl.toString(),
            uid
        )
        ref.child(uid).child("Data").setValue(User).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModel.init(this, uid)
                viewModel.addData(User)
            }
        }
    }

    private fun inputValidation() {
        inputEmail = inpEmail.text.toString()
        inputPassword = inpPassword.text.toString()
        when {
            inputEmail.isEmpty() -> {
                inpEmail.error = "Username tidak boleh kosong"
                inpEmail.requestFocus()
            }
            inputPassword.isEmpty() -> {
                inpPassword.error = "Password tidak boleh kosong"
                inpPassword.requestFocus()
            }
            else -> {
                loginUser(inputEmail, inputPassword)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    goToMainActivity()
                    Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthException) {
                        var errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                        when (errorCode) {
                            "ERROR_INVALID_EMAIL" -> {
                                inpEmail.error = "Email Anda tidak valid"
                                inpEmail.requestFocus()
                            }
                            "ERROR_USER_NOT_FOUND" -> {
                                inpEmail.error = "Akun Anda belum terdaftar"
                                inpEmail.requestFocus()
                            }
                            "ERROR_WRONG_PASSWORD" -> {
                                inpPassword.error = "Password anda salah"
                                inpPassword.requestFocus()
                            }
                        }
                        Log.e("error", errorCode)
                    } catch (e: FirebaseTooManyRequestsException) {

                    }
                    Log.e("error", "tomany")

                }

            }
    }


    private fun goToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
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

}
