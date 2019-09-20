package com.talmir.mickinet.screens.main.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.talmir.mickinet.databinding.DevicesListItemBinding
import com.talmir.mickinet.models.DeviceDetails

/**
 * A adapter class that holds items for [RecyclerView].
 */
class DevicesListAdapter(private val clickListener: NearbyDevicesListItemClickListener? = null) :
    ListAdapter<DeviceDetails, DevicesListAdapter.ViewHolder>(DeviceDetailsDiffItemCallback) {
    /**
     * Called when recycler view request for the `layout` to show.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    /**
     * Called just after the [onCreateViewHolder] invocation to bind
     * the data to pre-initialized view.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), clickListener)

    /**
     * A standard view holder class to hold the views ~_~
     */
    class ViewHolder private constructor(private val binding: DevicesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds [deviceItemDetails] object to layout file properties
         */
        fun bind(deviceItemDetails: DeviceDetails, clickListener: NearbyDevicesListItemClickListener?) {
            // sets the properties in the binding class
            binding.nearbyDeviceName = deviceItemDetails.name
            binding.nearbyDeviceMAC = deviceItemDetails.macAddress
            binding.nearbyDeviceItemClickListener = clickListener

            /**
             * causes the properties updates to execute immediately.
             * since I'm calling [bind] from [onBindViewHolder] having the bindings execute immediately.
             * as a practice can prevent the recycler view from having to perform extra calculations
             * when it figures out how to display the list.
             */
            binding.executePendingBindings()
        }

        companion object {
            /**
             * Inflate required layout file and pass it to [ViewHolder] as view.
             */
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DevicesListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    /**
     * Callback to calculate differences between two non-null items in a list.
     *
     * Used by [ListAdapter] to calculate the minimum number of changes between
     * and old list and a new list that's been passed to [submitList].
     */
    companion object DeviceDetailsDiffItemCallback : DiffUtil.ItemCallback<DeviceDetails>() {
        // I used referential equality operator - triple equal sign (===) - which returns
        // true if the object references are the same
        override fun areItemsTheSame(oldItem: DeviceDetails, newItem: DeviceDetails) =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: DeviceDetails, newItem: DeviceDetails) =
            oldItem.macAddress == newItem.macAddress
    }
}

/**
 * A click listener class to handle recycler view item click.
 */
class NearbyDevicesListItemClickListener(val clickListener: (macAddress: String) -> Unit) {
    /**
     * A function that will be fired on recycler view each item's click.
     */
    fun onClick(macAddress: String) = clickListener(macAddress)
}
