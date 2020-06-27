package com.pedolu.smkcodingchallenge3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pedolu.smkcodingchallenge3.R
import com.pedolu.smkcodingchallenge3.data.model.global.GlobalStatusSummaryItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.global_status_item.view.*
import java.text.NumberFormat
import java.util.*

class GlobalStatusAdapter(
    private val context: Context,
    private val items:
    List<GlobalStatusSummaryItem>,
    private val status: String,
    private val listener: (GlobalStatusSummaryItem) -> Unit
) :
    RecyclerView.Adapter<GlobalStatusAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =

        ViewHolder(
            context, LayoutInflater.from(context).inflate(
                R.layout.global_status_item,
                parent, false
            )
        )

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position], this.status, listener)
    }

    class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val txtCountry = containerView.txtCountry
        private val txtJumlah = containerView.txtJumlah
        private val txtStatus = containerView.txtStatus
        fun bindItem(
            item: GlobalStatusSummaryItem,
            status: String,
            listener: (GlobalStatusSummaryItem) -> Unit
        ) {
            txtCountry.text = item.combinedKey
            setStatusText(status, item)
            containerView.setOnClickListener { listener(item) }
        }

        private fun setStatusText(status: String, item: GlobalStatusSummaryItem) {
            if (status == "confirmed") {
                txtJumlah.text = NumberFormat.getNumberInstance(Locale.US).format(item.confirmed)
                txtStatus.text = "Confirmed"
                txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorYellow))
            }
            when (status) {
                "confirmed" -> {
                    txtJumlah.text =
                        NumberFormat.getNumberInstance(Locale.US).format(item.confirmed)
                    txtStatus.text = "Confirmed"
                    txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorYellow))
                }
                "recovered" -> {
                    txtJumlah.text =
                        NumberFormat.getNumberInstance(Locale.US).format(item.recovered)
                    txtStatus.text = "Recovered"
                    txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }
                "death" -> {
                    txtJumlah.text = NumberFormat.getNumberInstance(Locale.US).format(item.deaths)
                    txtStatus.text = "Death"
                    txtStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
            }
        }
    }
}