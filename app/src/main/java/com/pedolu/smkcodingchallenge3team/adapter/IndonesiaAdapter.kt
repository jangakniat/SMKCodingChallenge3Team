package com.pedolu.smkcodingchallenge3team.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.pedolu.smkcodingchallenge3team.R
import com.pedolu.smkcodingchallenge3team.data.model.room.IndonesiaSummaryModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.provinsi_indonesia_item.view.*
import java.text.NumberFormat
import java.util.*


class IndonesiaAdapter(
    private val context: Context,
    private val items: List<IndonesiaSummaryModel>,
    private val listener: (IndonesiaSummaryModel) -> Unit
) : RecyclerView.Adapter<IndonesiaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            context, LayoutInflater.from(context).inflate(
                R.layout.provinsi_indonesia_item,
                parent, false
            )
        )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val txtProvinsi = containerView.txtProvinsi
        private val txtRecovered = containerView.txtRecovered
        private val txtConfirmed = containerView.txtConfirmed
        private val txtDeath = containerView.txtDeath
        fun bindItem(item: IndonesiaSummaryModel, listener: (IndonesiaSummaryModel) -> Unit) {
            txtProvinsi.text = item.nama_provinsi
            txtConfirmed.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.terkonfirmasi.toInt())
            txtRecovered.text =
                NumberFormat.getNumberInstance(Locale.US).format(item.sembuh.toInt())
            txtDeath.text = NumberFormat.getNumberInstance(Locale.US).format(item.meninggal.toInt())
            containerView.setOnClickListener { listener(item) }
        }
    }

}

