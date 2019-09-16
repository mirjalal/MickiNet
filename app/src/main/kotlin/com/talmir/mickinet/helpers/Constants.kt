package com.talmir.mickinet.helpers

/**
 * Following constants describes nearby devices search statuses.
 *
 * They have following meanings:
 * [STARTED] - discovery started
 * [STOPPED] - discovery stopped due to any reason
 *             (devices found, user stopped, system error occurred etc.)
 * [NOTHING_FOUND] - no any device found
 *
 * Because of memory problems, we should escape from using Enums in Android.
 * Thus, I created those integer constants to use.
 * For more information about Enums in Android [visit](https://youtu.be/Hzs6OBcvNQE).
 */
object NearbyDeviceDiscoveryState {
    const val STARTED = 0
    const val STOPPED = 1
    const val NOTHING_FOUND = 2
}
