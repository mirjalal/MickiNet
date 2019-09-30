package com.talmir.mickinet.screens.main.fragments

import android.content.Context
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.talmir.mickinet.R
import com.talmir.mickinet.databinding.FragmentDevicesListBinding
import com.talmir.mickinet.decorator.CustomFragment
import com.talmir.mickinet.helpers.NearbyDeviceDiscoveryState
import com.talmir.mickinet.helpers.deviceDetails
import com.talmir.mickinet.models.DeviceDetails
import com.talmir.mickinet.repository.Repository

/**
 * A [Fragment] subclass to discover, show and connect
 * via WiFi Direct channel to nearby devices.
 *
 * In this class we will listen to Wifi Direct changes
 * on system level. To do this I implemented the
 * [WifiP2pManager.PeerListListener] object in our
 * [com.talmir.mickinet.background.WifiP2pStateChangeReceiver]
 * broadcast receiver class.
 * For more information please look at UML diagram.
 */
class DevicesListFragment : CustomFragment() {

    private lateinit var binding: FragmentDevicesListBinding

    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var manager: WifiP2pManager

    private fun wifiDirectActionListener(action: () -> Unit) =
        object : WifiP2pManager.ActionListener {
            override fun onSuccess() = action()

            override fun onFailure(reasonCode: Int) =
                when (reasonCode) {
                    WifiP2pManager.ERROR -> toast(R.string.discovery_error_1)
                    WifiP2pManager.P2P_UNSUPPORTED -> toast(R.string.discovery_error_2)
                    WifiP2pManager.BUSY -> toast(R.string.discovery_error_3)
                    else -> toast(R.string.discovery_error_4)
                }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = fragmentActivity.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(fragmentActivity, getMainLooper(), null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDevicesListBinding.inflate(inflater, container, false)

        binding.devicesListRecyclerView.addItemDecoration(
            DividerItemDecoration(fragmentActivity, LinearLayoutManager.VERTICAL)
        )
        binding.devicesListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                when {
                    dy > 0 -> binding.startDiscover.hide()
                    else -> binding.startDiscover.show()
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.startDiscover.setOnClickListener {
            if (binding.nearbyDeviceDiscoverStatus == NearbyDeviceDiscoveryState.USER_STARTED) // if discovery started but no any device found
                stopPeerDiscovery(NearbyDeviceDiscoveryState.NO_ANY)
            else if (binding.nearbyDeviceDiscoverStatus == NearbyDeviceDiscoveryState.DISCOVERED)
                stopPeerDiscovery(NearbyDeviceDiscoveryState.USER_STOPPED)
            else
                manager.discoverPeers(channel, wifiDirectActionListener {
                    binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.USER_STARTED
                    showDiscoveredItems()
                })
        }

        Repository.instance().peerList.observe(fragmentActivity, Observer {
            it?.let { peers ->
                if (peers.isNotEmpty()) {
                    /**
                     * check if proper setting is active or not.
                     * depending on it, set status to DISCOVERED
                     * or DEVICE_FOUND.
                     */
                    binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.DISCOVERED
//                    binding.nearbyDev iceDiscoverStatus = NearbyDeviceDiscoveryState.DEVICE_FOUND

                    val devicesListItemClickListener = NearbyDevicesListItemClickListener { macAddress ->
                        val config = WifiP2pConfig()
                        config.deviceAddress = macAddress
                        config.wps.setup = WpsInfo.PBC

                        manager.connect(channel, config, wifiDirectActionListener {
                            /**
                             * First thing first, check if peer discovery
                             * had already been stopped automatically or not.
                             * If yes, execute, otherwise bypass the
                             * following line.
                             */
                            if (binding.nearbyDeviceDiscoverStatus == NearbyDeviceDiscoveryState.USER_STARTED ||
                                binding.nearbyDeviceDiscoverStatus == NearbyDeviceDiscoveryState.DISCOVERED)
                                stopPeerDiscovery(NearbyDeviceDiscoveryState.USER_STOPPED)

                            println("okay :)))")
                        })
                    }

                    showDiscoveredItems(devicesListItemClickListener, peers)
                } else
                    binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.NO_ANY
            }
        })

        return binding.root
    }

    private fun showDiscoveredItems(
        clickListener: NearbyDevicesListItemClickListener? = null,
        peers: List<WifiP2pDevice>? = null) {
        val adapter = DevicesListAdapter(clickListener)
        /** transform elements' type from [WifiP2pDevice] to [DeviceDetails] */
        adapter.submitList(peers?.map(WifiP2pDevice::deviceDetails))
        /** bind [peers] to recyclerView */
        binding.devicesListRecyclerView.adapter = adapter
    }

    private fun stopPeerDiscovery(status: Int) =
        manager.stopPeerDiscovery(channel, wifiDirectActionListener {
            binding.nearbyDeviceDiscoverStatus = status
        })

    private fun toast(@StringRes what: Int) =
        Toast.makeText(fragmentActivity, what, Toast.LENGTH_LONG).show()
}
