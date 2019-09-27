package com.talmir.mickinet.screens.main.fragments

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.talmir.mickinet.R
import com.talmir.mickinet.helpers.NearbyDeviceDiscoveryState

private fun Int.getDeviceStatus(context: Context) =
    when (this) {
        WifiP2pDevice.AVAILABLE -> context.getString(R.string.available)
        WifiP2pDevice.INVITED -> context.getString(R.string.invited)
        WifiP2pDevice.CONNECTED -> context.getString(R.string.connected)
        WifiP2pDevice.FAILED -> context.getString(R.string.failed)
        WifiP2pDevice.UNAVAILABLE -> context.getString(R.string.unavailable)
        else -> context.getString(R.string.unknown)
    }

/**
 * Binds [name] to device name field in view.
 */
@BindingAdapter("deviceName")
fun TextView.bindDeviceName(name: String) {
    text = HtmlCompat.fromHtml(
        context.getString(R.string.device_name).format(name),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

/**
 * Binds [status] to device status field in view.
 */
@BindingAdapter("deviceStatus")
fun TextView.bindDeviceStatus(status: Int) {
    text = HtmlCompat.fromHtml(
        context.getString(R.string.device_status).format(
            status.getDeviceStatus(context)
        ), HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

/**
 * Binds [macAddress] to device mac address field in view.
 */
@BindingAdapter("deviceMacAddress")
fun TextView.bindDeviceMacAddress(macAddress: String) {
    text = HtmlCompat.fromHtml(
        context.getString(R.string.device_mac_address).format(macAddress),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

/**
 * Handles view visibility depending on [state].
 *
 * @param state one of the constants from [NearbyDeviceDiscoveryState]
 */
@BindingAdapter("shouldBeVisible")
fun View.bindVisibility(state: Int) {
    when (state) {
        NearbyDeviceDiscoveryState.STARTED -> {
            if (this is TextView)
                text = context.getString(R.string.discovering_nearby_devices)
            visibility = View.VISIBLE
        }

        NearbyDeviceDiscoveryState.DISCOVERED -> {
            visibility = View.GONE
        }

        else -> {
            if (this is TextView) {
                text = context.getString(R.string.no_device_found)
                visibility = View.VISIBLE
            }
            if (this is ContentLoadingProgressBar)
                visibility = View.GONE
        }
    }
}

@BindingAdapter("imageResourceByState")
fun FloatingActionButton.bindImageResourceByState(state: Int) {
    when (state) {
        NearbyDeviceDiscoveryState.STARTED -> setImageResource(R.drawable.ic_stop_discovery)
        else -> setImageResource(R.drawable.ic_start_discovery)
    }
}
