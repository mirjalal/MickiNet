package com.talmir.mickinet.screens.main.fragments

import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.talmir.mickinet.R
import com.talmir.mickinet.helpers.NearbyDeviceDiscoveryState

/**
 * Appends [what] to string resource and sets them
 * proper [TextView].
 *
 * @param which determines which string resource should
 *              be formatted
 * @param what  what should be append to pre-defined
 *              string resource
 */
@BindingAdapter(value = ["devicePropType", "deviceProp"])
fun TextView.bindDeviceProp(which: Int, what: String) {
    // get string resource by which
    val boldText = when (which) {
        0 -> context.getString(R.string.device_name)
        1 -> context.getString(R.string.device_status)
        else -> context.getString(R.string.device_mac_address)
    }

    // format string resource
    text = HtmlCompat.fromHtml(boldText.format(what), HtmlCompat.FROM_HTML_MODE_LEGACY)
}

/**
 * Handles view visibility depending on [status].
 *
 * @param status one of the constants from [NearbyDeviceDiscoveryState]
 */
@BindingAdapter("shouldBeVisible")
fun View.bindVisibility(status: Int) {
    when (status) {
        NearbyDeviceDiscoveryState.NOTHING_FOUND -> {
            if (this is TextView) {
                text = context.getString(R.string.no_device_found)
                visibility = View.VISIBLE
            }
            if (this is ContentLoadingProgressBar)
                visibility = View.GONE
        }

        NearbyDeviceDiscoveryState.STARTED -> {
            if (this is TextView)
                text = context.getString(R.string.discovering_nearby_devices)
            visibility = View.VISIBLE
        }

        NearbyDeviceDiscoveryState.STOPPED -> {
            if (this is TextView) {
                text = context.getString(R.string.no_device_found)
                visibility = View.VISIBLE
            }
            if (this is ContentLoadingProgressBar)
                visibility = View.GONE
        }
    }
}
