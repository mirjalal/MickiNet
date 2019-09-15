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

    /** BEGIN: WiFi-Direct status related stuff */
    private val mIsWifiP2pEnabled = MediatorLiveData<Boolean>()
    fun isWifiP2pEnabled(): LiveData<Boolean> = mIsWifiP2pEnabled

    fun addWifiP2pEnableSource(data: LiveData<Boolean>) =
        mIsWifiP2pEnabled.addSource(data) {
            mIsWifiP2pEnabled.value = it
        }

    fun removeWifiP2pEnableSource(data: LiveData<Boolean>) =
        mIsWifiP2pEnabled.removeSource(data)
    /** END: WiFi-Direct status related stuff */


    /** BEGIN: device connection info related stuff */
    private val mIsConnected = MediatorLiveData<Boolean>()
    fun isConnected(): LiveData<Boolean> = mIsConnected

    fun addConnectionSource(data: LiveData<Boolean>) =
        mIsConnected.addSource(data) {
            mIsConnected.value = it
        }

    fun removeConnectionSource(data: LiveData<Boolean>) =
        mIsConnected.removeSource(data)
    /** END: device connection info related stuff */


    /** BEGIN: device info related stuff */
    private val mDeviceInfo = MediatorLiveData<DeviceDetails>()
    fun deviceInfo(): LiveData<DeviceDetails> = mDeviceInfo

    fun addDeviceInfoSource(data: LiveData<DeviceDetails>) =
        mDeviceInfo.addSource(data) {
            mDeviceInfo.value = it
        }

    fun removeDeviceInfoSource(data: LiveData<DeviceDetails>) =
        mDeviceInfo.removeSource(data)
    /** END: device info related stuff */
}
