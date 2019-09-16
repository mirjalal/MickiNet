package com.talmir.mickinet.screens.main.fragments

import android.content.Context
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.talmir.mickinet.databinding.FragmentDevicesListBinding
import com.talmir.mickinet.helpers.NearbyDeviceDiscoveryState

/**
 * A [Fragment] subclass to discover, show and connect
 * via WiFi Direct channel to nearby devices.
 *
 * In this class we will listen to Wifi Direct changes
 * on system level. To do this I implemented the
 * [WifiP2pManager.PeerListListener] interface here.
 * For more information please look at UML diagram.
 */
class DevicesListFragment : Fragment(), WifiP2pManager.PeerListListener {
    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var binding: FragmentDevicesListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = context as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDevicesListBinding.inflate(inflater, container, false)

        binding.nearbyDeviceDiscoverStatus = NearbyDeviceDiscoveryState.STOPPED

//        val manager = fragmentActivity.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
//        val channel = manager.initialize(fragmentActivity, fragmentActivity.mainLooper, null)
//        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
//            override fun onSuccess() {
//                binding.nearbyDeviceDiscoverStatus = STARTED
//                //Toast.makeText(fragmentActivity, R.string.discovery_started, Toast.LENGTH_LONG).show()
//            }
//
//            override fun onFailure(reasonCode: Int) {
//                when (reasonCode) {
////                    WifiP2pManager.ERROR -> Toast.makeText(fragmentActivity, R.string.discovery_error_1, Toast.LENGTH_LONG).show()
////                    WifiP2pManager.P2P_UNSUPPORTED -> Toast.makeText(fragmentActivity, R.string.discovery_error_2, Toast.LENGTH_LONG).show()
////                    WifiP2pManager.BUSY -> Toast.makeText(fragmentActivity, R.string.discovery_error_3, Toast.LENGTH_LONG).show()
////                    else -> Toast.makeText(fragmentActivity, R.string.discovery_error_4, Toast.LENGTH_LONG).show()
//                }
//            }
//        })

//        manager.requestPeers()

        return binding.root
    }

    override fun onPeersAvailable(peers: WifiP2pDeviceList?) {
//        if (peers != null) {
//
//        }
    }
}
