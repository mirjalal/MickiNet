package com.talmir.mickinet.helpers

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import androidx.core.content.edit
import com.talmir.mickinet.models.DeviceDetails

/**
 * A simple mapper extension function
 * to convert [WifiP2pDevice] object
 * to [DeviceDetails] item.
 */
fun WifiP2pDevice.deviceDetails() =
    DeviceDetails(deviceName, status, deviceAddress)

/**
 * Merges [this] with [another]. If [this] is empty,
 * then copy all elements from [another] to temp
 * list and return. Otherwise, check both lists by
 * their elements' MAC address property. Then, add
 * non-existing elements to temp list and return it.
 */
fun MutableList<WifiP2pDevice>.populateList(another: Collection<WifiP2pDevice>) {

    fun compareAndMerge() {
        if (!another.isNullOrEmpty())
            forEach { destElem ->
                another.forEach { sourceElem ->
                    if (destElem.deviceAddress != sourceElem.deviceAddress)
                        add(sourceElem)
                }
            }
    }

    if (isEmpty())
        addAll(another)
    else
        compareAndMerge()
}
