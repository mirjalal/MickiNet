package com.talmir.mickinet.helpers

/**
 * Following constants describes nearby devices search statuses.
 *
 * They have following meanings:
 * [NO_ANY]       - no any device found.
 * [USER_STARTED] - discovery started.
 * [USER_STOPPED] - user stopped a ongoing discovery.
 * [DEVICE_FOUND] - only one device found (respecting to user setting).
 * [DISCOVERED]   - some nearby devices discovered.
 *
 * Because of memory problems, we should escape from using Enums in Android.
 * Thus, I created those integer constants to use.
 * For more information about Enums in Android [visit](https://youtu.be/Hzs6OBcvNQE).
 */
object NearbyDeviceDiscoveryState {
    const val NO_ANY = 0
    const val USER_STARTED = 1
    const val USER_STOPPED = 2
    const val DEVICE_FOUND = 3
    const val DISCOVERED = 4
}
