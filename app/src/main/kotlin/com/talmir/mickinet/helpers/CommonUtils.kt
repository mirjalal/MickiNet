package com.talmir.mickinet.helpers

import android.net.wifi.p2p.WifiP2pDevice
import com.talmir.mickinet.models.DeviceDetails

/**
 * A simple mapper extension function
 * to convert [WifiP2pDevice] object
 * to [DeviceDetails] item.
 */
fun WifiP2pDevice.deviceDetails() =
    DeviceDetails(deviceName, status, deviceAddress)

fun MutableList<WifiP2pDevice>.populateList(another: Collection<WifiP2pDevice>) {
    if (isEmpty())
        addAll(another)
    else {
        /**
         * merge [another] with [this] by comparing deviceAddress properties...
         */
    }
}
