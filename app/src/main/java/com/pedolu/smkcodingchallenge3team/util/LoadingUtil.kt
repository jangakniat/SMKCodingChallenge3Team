package com.pedolu.smkcodingchallenge3team.util

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pedolu.smkcodingchallenge3team.R

fun showLoading(context: Context, swipeRefreshLayout: SwipeRefreshLayout) {
    swipeRefreshLayout.setColorSchemeColors(
        ContextCompat.getColor(
            context,
            R.color.colorPrimary
        )
    )
    swipeRefreshLayout.isRefreshing = true
}

fun dismissLoading(swipeRefreshLayout: SwipeRefreshLayout) {
    swipeRefreshLayout.isRefreshing = false
}