package com.talmir.mickinet.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.talmir.mickinet.helpers.deviceDetails
import com.talmir.mickinet.helpers.populateList
import com.talmir.mickinet.models.DeviceDetails

/**
 * A class which handles Wifi P2P status changes and delivers to
 * MainActivity on demand by deriving from [BroadcastReceiver].
 */
class WifiP2pStateChangeReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel
) : BroadcastReceiver() {

    private val _isWifiP2pEnabled: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isWifiP2pEnabled: LiveData<Boolean> = _isWifiP2pEnabled

    private val _isConnected: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val isConnected: LiveData<Boolean> = _isConnected

    private val _deviceInfo: MutableLiveData<DeviceDetails> by lazy {
        MutableLiveData<DeviceDetails>()
    }
    val deviceInfo: LiveData<DeviceDetails> = _deviceInfo

    private val peerListLocalCache = mutableListOf<WifiP2pDevice>()
    private val peerListChangeListener: WifiP2pManager.PeerListListener by lazy {
        WifiP2pManager.PeerListListener {
            // update list with last discovered devices
            peerListLocalCache.populateList(it.deviceList)

            // The peer list has changed. Update LiveData too.
            _peerList.postValue(peerListLocalCache)
        }
    }

    private val _peerList: MutableLiveData<List<WifiP2pDevice>> by lazy {
        MutableLiveData<List<WifiP2pDevice>>()
    }
    val peerList: LiveData<List<WifiP2pDevice>> = _peerList

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wifi P2P mode is enabled or not, update the LiveData
                // and the changes will be observed in DeviceDetailsFragment.
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                _isWifiP2pEnabled.postValue(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity/fragment is notified
                // with a callback on PeerListListener.onPeersAvailable().
                manager.requestPeers(channel, peerListChangeListener)
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Connection state changed
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val activeNetwork = cm.getNetworkCapabilities(cm.activeNetwork)
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                        // connected to wifi direct
                        println("SUCCESS!! CONNECTED TO OTHER DEVICE.")
                    } else {
                        println("COULD NOT CONNECT TO OTHER DEVICE!")
                    }
                } else {
                    println("activeNetwork is null")
                }
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val wifiP2pDevice =
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice

                _deviceInfo.postValue(wifiP2pDevice.deviceDetails())
            }
        }
    }
}
