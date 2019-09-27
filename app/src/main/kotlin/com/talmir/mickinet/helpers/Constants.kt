package com.talmir.mickinet.helpers

/**
 * Following constants describes nearby devices search statuses.
 *
 * They have following meanings:
 * [STARTED]    - discovery started
 * [STOPPED]    - discovery stopped due to any reason
 *                (devices found, user stopped, system error occurred etc.)
 * [NOT_FOUND]  - no any device found
 * [DISCOVERED] - nearby devices discovered
 *
 * Because of memory problems, we should escape from using Enums in Android.
 * Thus, I created those integer constants to use.
 * For more information about Enums in Android [visit](https://youtu.be/Hzs6OBcvNQE).
 */
object NearbyDeviceDiscoveryState {
    const val STOPPED = 0
    const val STARTED = 1
    const val NOT_FOUND = STOPPED
    const val DISCOVERED = STOPPED
}
