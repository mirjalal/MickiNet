package com.talmir.mickinet.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.Channel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.talmir.mickinet.R
import com.talmir.mickinet.models.DeviceDetails

class WifiP2pStateChangeReceiver(manager: WifiP2pManager, channel: Channel) : BroadcastReceiver() {

    private val _isWifiP2pEnabled = MutableLiveData<Boolean>()
    val isWifiP2pEnabled: LiveData<Boolean> = _isWifiP2pEnabled

    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean> = _isConnected

    private val _deviceInfo = MutableLiveData<DeviceDetails>()
    val deviceInfo: LiveData<DeviceDetails> = _deviceInfo

    private fun WifiP2pDevice.deviceDetails(context: Context): DeviceDetails {
        fun getDeviceStatus() =
            when (status) {
                WifiP2pDevice.AVAILABLE -> context.getString(R.string.available)
                WifiP2pDevice.INVITED -> context.getString(R.string.invited)
                WifiP2pDevice.CONNECTED -> context.getString(R.string.connected)
                WifiP2pDevice.FAILED -> context.getString(R.string.failed)
                WifiP2pDevice.UNAVAILABLE -> context.getString(R.string.unavailable)
                else -> context.getString(R.string.unknown)
            }

        return DeviceDetails(deviceName, getDeviceStatus(), deviceAddress)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wifi P2P mode is enabled or not, alert
                // the Activity
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                _isWifiP2pEnabled.postValue(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

                // The peer list has changed! We should probably do something about
                // that.

            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                // Connection state changed! We should probably do something about
                // that.

            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val wifiP2pDevice =
                    (intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice)

                _deviceInfo.postValue(wifiP2pDevice.deviceDetails(context))
            }
        }
    }
}
