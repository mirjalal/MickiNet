package com.talmir.mickinet.repository

import android.net.wifi.p2p.WifiP2pDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.talmir.mickinet.models.DeviceDetails

/**
 * Our Single source of truth (SSoT) class.
 *
 * Flow of data that comes from
 * [com.talmir.mickinet.background.WifiP2pStateChangeReceiver]
 * to view through SSoT class (in this case [Repository] class).
 */
class Repository private constructor() {
    /**
     * Singleton pattern applied here due
     * to default constructor is private.
     */
    companion object {
        private val repositoryInstance = Repository()

        /**
         * Retrieve class instance.
         */
        fun instance() = repositoryInstance
    }


    //region WiFi Direct status related stuff
    private val mIsWifiP2pEnabled: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>()
    }
    val isWifiP2pEnabled: LiveData<Boolean> = mIsWifiP2pEnabled

    /**
     * Register a [LiveData] object to be observed.
     *
     * @param source a [LiveData] object to register
     */
    fun addWifiP2pEnableSource(source: LiveData<Boolean>) =
        mIsWifiP2pEnabled.addSource(source) {
            mIsWifiP2pEnabled.value = it
        }

    /**
     * Unregister [source] to prevent its observation.
     *
     * @param source a [LiveData] source to unsubscribe
     */
    fun removeWifiP2pEnableSource(source: LiveData<Boolean>) =
        mIsWifiP2pEnabled.removeSource(source)
    //endregion WiFi Direct status related stuff


    //region Device connection info related stuff
    private val mIsConnected: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>()
    }
    val isConnected: LiveData<Boolean> = mIsConnected

    /**
     * Register a [LiveData] object to be observed.
     *
     * @param source a [LiveData] object to register
     */
    fun addConnectionSource(source: LiveData<Boolean>) =
        mIsConnected.addSource(source) {
            mIsConnected.value = it
        }

    /**
     * Unregister [source] to prevent its observation.
     *
     * @param source a [LiveData] source to unsubscribe
     */
    fun removeConnectionSource(source: LiveData<Boolean>) =
        mIsConnected.removeSource(source)
    //endregion Device connection info related stuff


    //region Device info related stuff
    private val mDeviceInfo: MediatorLiveData<DeviceDetails> by lazy {
        MediatorLiveData<DeviceDetails>()
    }
    val deviceInfo: LiveData<DeviceDetails> = mDeviceInfo

    /**
     * Register a [LiveData] object to be observed.
     *
     * @param source a [LiveData] object to register
     */
    fun addDeviceInfoSource(source: LiveData<DeviceDetails>) =
        mDeviceInfo.addSource(source) {
            mDeviceInfo.value = it
        }

    /**
     * Unregister [source] to prevent its observation.
     *
     * @param source a [LiveData] source to unsubscribe
     */
    fun removeDeviceInfoSource(source: LiveData<DeviceDetails>) =
        mDeviceInfo.removeSource(source)
    //endregion Device info related stuff


    //region Peer list related stuff
    private val mPeerList: MediatorLiveData<List<WifiP2pDevice>> by lazy {
        MediatorLiveData<List<WifiP2pDevice>>()
    }
    val peerList: LiveData<List<WifiP2pDevice>> = mPeerList

    /**
     * Register a [LiveData] object to be observed.
     *
     * @param source a [LiveData] object to register
     */
    fun addPeerListSource(source: LiveData<List<WifiP2pDevice>>) =
        mPeerList.addSource(source) {
            mPeerList.value = it
        }

    /**
     * Unregister [source] to prevent its observation.
     *
     * @param source a [LiveData] source to unsubscribe
     */
    fun removePeerListSource(source: LiveData<List<WifiP2pDevice>>) =
        mPeerList.removeSource(source)
    //endregion Peer list related stuff
}
