package com.talmir.mickinet.screens.main.fragments

import android.content.Context
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.talmir.mickinet.R
import com.talmir.mickinet.databinding.FragmentDevicesListBinding
import com.talmir.mickinet.helpers.NearbyDeviceDiscoveryState
import com.talmir.mickinet.helpers.deviceDetails
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
class DevicesListFragment : Fragment() {
    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var binding: FragmentDevicesListBinding

    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var manager: WifiP2pManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = fragmentActivity.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(fragmentActivity, getMainLooper(), null)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDevicesListBinding.inflate(inflater, container, false)

        binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.STOPPED

        binding.button.setOnClickListener {
            manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.STARTED
                }

                override fun onFailure(reasonCode: Int) {
                    when (reasonCode) {
                        WifiP2pManager.ERROR -> toast(R.string.discovery_error_1)
                        WifiP2pManager.P2P_UNSUPPORTED -> toast(R.string.discovery_error_2)
                        WifiP2pManager.BUSY -> toast(R.string.discovery_error_3)
                        else -> toast(R.string.discovery_error_4)
                    }
                }
            })
        }

        Repository.instance().peerList.observe(fragmentActivity, Observer {
            it?.let { peers ->
                if (peers.isNotEmpty()) {
                    binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.DISCOVERED

                    binding.devicesListRecyclerView.addItemDecoration(
                        DividerItemDecoration(fragmentActivity, LinearLayoutManager.VERTICAL)
                    )

                    val adapter = DevicesListAdapter()
                    adapter.submitList(peers.map(WifiP2pDevice::deviceDetails))
                    binding.devicesListRecyclerView.adapter = adapter
                } else
                    binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.NOT_FOUND
            }
        })

        return binding.root
    }

    private fun toast(@StringRes what: Int) =
        Toast.makeText(fragmentActivity, what, Toast.LENGTH_LONG).show()
}
