package com.pedolu.smkcodingchallenge3team.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pedolu.smkcodingchallenge3team.R
import com.pedolu.smkcodingchallenge3team.data.model.room.VolunteerModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.volunteer_item.view.*


class VolunteerAdapter(
    private val context: Context,
    private val items: List<VolunteerModel>,
    private val listener: (VolunteerModel) -> Unit
) : RecyclerView.Adapter<VolunteerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            context, LayoutInflater.from(context).inflate(
                R.layout.volunteer_item,
                parent, false
            )
        )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private lateinit var storageRef: StorageReference
        private lateinit var imagesRef: StorageReference
        private val txtName = containerView.txtName
        private val txtTelephone = containerView.txtTelephone
        private val txtAddress = containerView.txtAddress
        fun bindItem(item: VolunteerModel, listener: (VolunteerModel) -> Unit) {
            storageRef = FirebaseStorage.getInstance().reference
            imagesRef = storageRef.child("images/${item.key}")
            txtName.text = item.name
            txtTelephone.text = item.telp
            txtAddress.text = item.address
            showVolunteerImage(item.image)
            containerView.setOnClickListener { listener(item) }
        }

        private fun showVolunteerImage(img: String) {
            val localImage = Uri.parse(img)
            imagesRef.getFile(localImage).addOnSuccessListener {
                Glide.with(context)
                    .load(localImage)
                    .into(containerView.volunteerImg)
                Log.i("img", "firebase local tem file created  created $localImage")
            }.addOnFailureListener { exception ->
                Log.e(
                    "firebase ",
                    ";local tem file not created  created $exception"
                )
            }
        }
    }


}

