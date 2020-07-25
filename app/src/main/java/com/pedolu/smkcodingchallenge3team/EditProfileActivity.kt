package com.pedolu.smkcodingchallenge3team

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pedolu.smkcodingchallenge3team.data.model.room.UserModel
import com.pedolu.smkcodingchallenge3team.util.tampilToast
import com.pedolu.smkcodingchallenge3team.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.progress_overlay.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var telp: String
    private lateinit var address: String
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var image: Uri
    private lateinit var inputImage: Uri
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var storageRef: StorageReference
    private lateinit var imagesRef: StorageReference


    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        retrieveUserData()

        auth = FirebaseAuth.getInstance()
        btnSaveEdit.setOnClickListener { updateUserData() }
        iv_image.setOnClickListener { checkStoragePermissions() }
    }

    private fun retrieveUserData() {
        val intentData = intent.extras
        image = Uri.parse(intentData?.getString("image"))
        name = intentData?.getString("name").toString()
        gender = intentData?.getString("gender").toString()
        age = intentData?.getString("age").toString()
        telp = intentData?.getString("telp").toString()
        address = intentData?.getString("address").toString()
        txtName.setText(name)
        txtAge.setText(age)
        Glide.with(this).load(image).into(iv_image)
        setGenderSpinner(gender)
        txtTelephone.setText(telp)
        txtAddress.setText(address)
    }

    private fun setGenderSpinner(string: String) {
        genderSpinner.background.setColorFilter(
            ContextCompat.getColor(applicationContext, R.color.colorGray),
            PorterDuff.Mode.SRC_ATOP
        )
        var x = 0
        while (x < genderSpinner.adapter.count) {
            if (genderSpinner.getItemAtPosition(x).toString() == string) {
                genderSpinner.setSelection(x)
            }
            x++
        }
    }

    private fun checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED
            ) {
                //permission denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                //show popup to request runtime permission
                requestPermissions(permissions, EditProfileActivity.PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        } else {
            //system OS is < Marshmallow
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, EditProfileActivity.IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            EditProfileActivity.PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == EditProfileActivity.IMAGE_PICK_CODE) {
            iv_image.setImageURI(data?.data)
            inputImage = Uri.parse(data?.data.toString())
            Log.i("img uri", image.toString())
        }
    }

    private fun updateUserData() {
        progressBarOverlay.visibility = View.VISIBLE
        inputImage=image
        name = txtName.text.toString()
        gender = genderSpinner.selectedItem.toString()
        age = txtAge.text.toString()
        telp = txtTelephone.text.toString()
        address = txtAddress.text.toString()
        inputValidation()
    }

    private fun inputValidation() {
        when {
            name.isEmpty() -> txtName.error = "Nama tidak boleh kosong"
            else -> {
                val uid: String = auth.currentUser!!.uid
                val User =
                    UserModel(
                        name,
                        gender,
                        age,
                        telp,
                        address,
                        image.toString(),
                        uid
                    )
                ref = FirebaseDatabase.getInstance().getReference("Users")
                storageRef = FirebaseStorage.getInstance().reference
                imagesRef = storageRef.child("images/${auth.uid}")

                ref.child(uid).child("Data").setValue(User)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            userViewModel.init(this, uid)
                            userViewModel.updateData(User)
                            if (image != inputImage) {
                                val uploadTask = imagesRef.putFile(image)
                                uploadTask.addOnSuccessListener {
                                    tampilToast(this, "Sukses Mengedit Profile")
                                    completeEditProfileActivity()
                                }.addOnFailureListener {
                                    tampilToast(this, "gagal upload gambar")
                                }
                            }else{
                                completeEditProfileActivity()
                            }
                        } else {
                            tampilToast(this, "Coba Lagi")
                            try {
                                throw task.exception!!
                            } catch (e: Exception) {
                                Log.e("error", e.message.toString())
                            }
                        }
                    }
            }
        }
    }

    private fun completeEditProfileActivity() {
        progressBarOverlay.visibility = View.VISIBLE
        val result = Intent()
        result.putExtra("name", name)
        result.putExtra("age", age)
        result.putExtra("gender", gender)
        result.putExtra("telp", telp)
        result.putExtra("address", address)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }
}
