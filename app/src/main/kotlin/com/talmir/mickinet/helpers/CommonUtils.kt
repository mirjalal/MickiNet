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

/**
 * Merges [this] with [source]. If [this] is empty,
 * then copy all elements from [this] to temp
 * list and return. Otherwise, check both lists by
 * their elements' MAC address property. Then, add
 * non-existing elements to temp list and return it.
 */
fun MutableList<WifiP2pDevice>.populateList(another: Collection<WifiP2pDevice>) {

    fun compareAndMerge() {
        if (!another.isNullOrEmpty()) {
            val iterator = iterator()

            while (iterator.hasNext()) {
                val nextElem = iterator.next()
                another.forEach { sourceElem ->
                    if (nextElem.deviceAddress != sourceElem.deviceAddress)
                        add(sourceElem)
                }
            }
        }
    }

    if (isEmpty())
        addAll(another)
    else
        compareAndMerge()
}
