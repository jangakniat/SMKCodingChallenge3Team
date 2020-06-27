package com.pedolu.smkcodingchallenge3

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.pedolu.smkcodingchallenge3.util.tampilToast
import com.pedolu.smkcodingchallenge3.viewmodel.UserViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.progress_overlay.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var telp: String
    private lateinit var address: String
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var uid: String


    companion object {
        val REQUEST_CODE = 100

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser!!.uid
        retriveRoomUserData()
        retrieveUserData()
        btnToEdit.setOnClickListener { goToEditProfileActivity() }
        btnExit.setOnClickListener { logoutUser() }
        btnNotif.setOnClickListener { createFCMToken() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createFCMToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                sendNotification("Apa Kabar", "Notifikasi Sukses dikirim")
                val token = task.result?.token
                Log.d("FCM", token.toString())

            })
    }


    private fun retriveRoomUserData() {
        progressBarOverlay.visibility = View.VISIBLE
        profileLinearLayout.visibility = View.GONE
        userViewModel.init(this, uid)
        userViewModel.user.observe(this, Observer { user ->
            name = user.name
            gender = user.gender
            age = user.age
            telp = user.telp
            address = user.address
            setViewText()
            progressBarOverlay.visibility = View.GONE
            profileLinearLayout.visibility = View.VISIBLE
        })
    }


    private fun retrieveUserData() {
        progressBarOverlay.visibility = View.VISIBLE
        profileLinearLayout.visibility = View.GONE
        ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid).child("Data").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                tampilToast(applicationContext, "Database Error")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                name = dataSnapshot.child("name").value.toString()
                gender = dataSnapshot.child("gender").value.toString()
                age = dataSnapshot.child("age").value.toString()
                telp = dataSnapshot.child("telp").value.toString()
                address = dataSnapshot.child("address").value.toString()
                setViewText()
                progressBarOverlay.visibility = View.GONE
                profileLinearLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun setViewText() {
        txtName.text = name
        txtGender.text = gender
        txtAge.text = age
        txtTelephone.text = telp
        txtAddress.text = address
    }

    private fun sendNotification(messageTitle: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "notif"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun goToEditProfileActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("gender", gender)
        intent.putExtra("age", age)
        intent.putExtra("telp", telp)
        intent.putExtra("address", address)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun logoutUser() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                val i = Intent(this, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                finish()
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }


}
