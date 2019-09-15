package com.talmir.mickinet.screens.main.fragments

import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.talmir.mickinet.R

@BindingAdapter(value = ["devicePropType", "deviceProp"])
fun TextView.bindDeviceProp(which: Int, what: String) {
    val boldText = when (which) {
        0 -> context.getString(R.string.device_name)
        1 -> context.getString(R.string.device_status)
        else -> context.getString(R.string.device_mac_address)
    }

    text = HtmlCompat.fromHtml(String.format(boldText, what), HtmlCompat.FROM_HTML_MODE_LEGACY)
}

@BindingAdapter("shouldBeVisible")
fun View.bindVisibility(status: Int) {
    when (status) {
        DevicesListFragment.NO_NEARBY_DEVICE -> {
            if (this is TextView) {
                text = context.getString(R.string.no_device_found)
                visibility = View.VISIBLE
            }
            if (this is ContentLoadingProgressBar)
                visibility = View.GONE
        }

        DevicesListFragment.STARTED -> {
            if (this is TextView)
                text = context.getString(R.string.discovering_nearby_devices)
            visibility = View.VISIBLE
        }

        DevicesListFragment.STOPPED -> {
            if (this is TextView) {
                text = context.getString(R.string.no_device_found)
                visibility = View.VISIBLE
            }
            if (this is ContentLoadingProgressBar)
                visibility = View.GONE
        }
    }
}
