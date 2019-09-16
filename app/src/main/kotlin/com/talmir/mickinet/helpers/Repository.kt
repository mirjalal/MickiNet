package com.talmir.mickinet.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.talmir.mickinet.models.DeviceDetails

/**
 * Our Single source of truth (SSoT) class.
 *
 * Flow of data that comes from [WifiP2pStateChangeReceiver]
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

    // BEGIN: WiFi-Direct status related stuff
    private val mIsWifiP2pEnabled = MediatorLiveData<Boolean>()

    /**
     * Returns immutable variant of [mIsWifiP2pEnabled].
     */
    fun isWifiP2pEnabled(): LiveData<Boolean> = mIsWifiP2pEnabled

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
    // END: WiFi-Direct status related stuff


    // BEGIN: device connection info related stuff
    private val mIsConnected = MediatorLiveData<Boolean>()

    /**
     * Returns immutable variant of [mIsConnected].
     */
    fun isConnected(): LiveData<Boolean> = mIsConnected

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
    // END: device connection info related stuff


    // BEGIN: device info related stuff
    private val mDeviceInfo = MediatorLiveData<DeviceDetails>()

    /**
     * Returns immutable variant of [mDeviceInfo].
     */
    fun deviceInfo(): LiveData<DeviceDetails> = mDeviceInfo

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
    // END: device info related stuff
}
