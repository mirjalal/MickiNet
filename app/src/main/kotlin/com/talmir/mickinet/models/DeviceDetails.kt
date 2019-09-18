package com.talmir.mickinet.models

/**
 * A class to represent required device
 * specific data all in one place.
 *
 * @param name       a name shown in Bluetooth or Wifi Direct menu in device
 * @param status     device availability indicator
 * @param macAddress the MAC address of a device
 */
data class DeviceDetails(val name: String, val status: Int, val macAddress: String)
