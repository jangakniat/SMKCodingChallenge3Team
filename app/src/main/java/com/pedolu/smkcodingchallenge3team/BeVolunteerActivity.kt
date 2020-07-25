package com.pedolu.smkcodingchallenge3team

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_be_volunteer.*

class BeVolunteerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_be_volunteer)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        btnRegister.setOnClickListener { goToRegisterActivity() }
        btnLogin.setOnClickListener { goToLoginActivity() }
    }

    private fun goToRegisterActivity() {
        val i = Intent(this, RegisterActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun goToLoginActivity() {
        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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