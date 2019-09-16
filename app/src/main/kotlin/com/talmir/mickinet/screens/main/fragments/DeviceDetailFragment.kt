package com.talmir.mickinet.screens.main.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.talmir.mickinet.R
import com.talmir.mickinet.databinding.DeviceNameChangeLayoutBinding
import com.talmir.mickinet.databinding.FragmentDeviceDetailsBinding
import com.talmir.mickinet.helpers.DeviceNameChangerUtil
import com.talmir.mickinet.helpers.Repository

/**
 * A [Fragment] subclass to show user's device information in main page.
 */
class DeviceDetailFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var binding: FragmentDeviceDetailsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = context as FragmentActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDeviceDetailsBinding.inflate(inflater, container, false)

        /**
         * Lines below we set initial values of some properties.
         * Otherwise, we will get [NullPointerException]. We'll
         * get device's real information just after observer gets
         * the data.
         */
        binding.deviceName = "---"
        binding.deviceStatus = "---"
        binding.deviceMacAddress = "---"
        binding.deviceInfoHolder.setOnClickListener { showDeviceNameChangeDialog() }

        // subscribe to deviceInfo changes to get device information
        Repository.instance().deviceInfo().observe(fragmentActivity, Observer {
            it.run {
                binding.deviceName = name
                binding.deviceStatus = status
                binding.deviceMacAddress = macAddress
            }
        })

        return binding.root
    }

    private fun showDeviceNameChangeDialog() {
        val deviceNameChangeViewBinding = DeviceNameChangeLayoutBinding.inflate(layoutInflater, null, false)
        val alert: AlertDialog = AlertDialog.Builder(fragmentActivity).create()

        deviceNameChangeViewBinding.newDeviceName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing special to do
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    deviceNameChangeViewBinding.newDeviceNameParent.error = getString(R.string.provide_device_name_error)
                    alert.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
                } else {
                    deviceNameChangeViewBinding.newDeviceNameParent.error = null
                    alert.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // nothing special to do
            }
        })

        alert.setTitle(R.string.change_device_name)
        alert.setView(deviceNameChangeViewBinding.root)
        alert.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.confirm_device_name_change)) { _: DialogInterface?, _: Int ->
            alert.dismiss()
            /** check if device name changed successfully */
            if (DeviceNameChangerUtil.changeDeviceName(fragmentActivity, deviceNameChangeViewBinding.newDeviceName.text.toString()))
                Snackbar.make(binding.root, R.string.device_name_change_successful, Snackbar.LENGTH_LONG).show()
            else
                Snackbar.make(binding.root, R.string.device_name_change_unsuccessful, Snackbar.LENGTH_LONG).show()
        }
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ -> alert.dismiss() }
        alert.show()
        /** initially disable POSITIVE_BUTTON */
        alert.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
    }
}
