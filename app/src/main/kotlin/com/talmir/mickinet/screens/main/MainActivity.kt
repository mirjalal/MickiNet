package com.talmir.mickinet.screens.main

import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.talmir.mickinet.R
import com.talmir.mickinet.databinding.ActivityMainBinding
import com.talmir.mickinet.repository.Repository
import com.talmir.mickinet.background.WifiP2pStateChangeReceiver

/**
 * Entry-point activity.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding

    private val intentFilter = IntentFilter()
    private lateinit var wifiDirectBroadcastReceiver: WifiP2pStateChangeReceiver

    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var manager: WifiP2pManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        manager = getSystemService(WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        wifiDirectBroadcastReceiver =
            WifiP2pStateChangeReceiver(manager, channel)
    }

    override fun onStart() {
        super.onStart()

        registerReceiver(wifiDirectBroadcastReceiver, intentFilter)
        // add data sources
        Repository.instance().addWifiP2pEnableSource(wifiDirectBroadcastReceiver.isWifiP2pEnabled)
        Repository.instance().addConnectionSource(wifiDirectBroadcastReceiver.isConnected)
        Repository.instance().addDeviceInfoSource(wifiDirectBroadcastReceiver.deviceInfo)
        Repository.instance().addPeerListSource(wifiDirectBroadcastReceiver.peerList)
    }

    override fun onStop() {
        super.onStop()

        // don't forget to remove receiver data sources
        Repository.instance().removeWifiP2pEnableSource(wifiDirectBroadcastReceiver.isWifiP2pEnabled)
        Repository.instance().removeConnectionSource(wifiDirectBroadcastReceiver.isConnected)
        Repository.instance().removeDeviceInfoSource(wifiDirectBroadcastReceiver.deviceInfo)
        Repository.instance().removePeerListSource(wifiDirectBroadcastReceiver.peerList)
        unregisterReceiver(wifiDirectBroadcastReceiver)
    }
}
