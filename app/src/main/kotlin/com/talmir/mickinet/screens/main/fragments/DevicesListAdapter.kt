package com.talmir.mickinet.screens.main.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.talmir.mickinet.databinding.DevicesListItemBinding
import com.talmir.mickinet.models.DeviceDetails

class DevicesListAdapter : ListAdapter<DeviceDetails, DevicesListAdapter.ViewHolder>(
    DeviceDetailsDiffItemCallback) {
    /**
     * Called when recycler view request for the `layout` to show.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    /**
     * Called just after the [onCreateViewHolder] invocation to bind
     * the data to pre-initialized view.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    /**
     * A standard view holder class to hold the views ~_~
     */
    class ViewHolder private constructor(private val binding: DevicesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DeviceDetails) {
            // sets the properties in the binding class
            binding.nearbyDeviceName = item.name
            binding.nearbyDeviceMAC = item.macAddress

            /**
             * causes the properties updates to execute immediately.
             * since I'm calling [bind] from [onBindViewHolder] having the bindings execute immediately.
             * as a practice can prevent the recycler view from having to perform extra calculations when it figures out how to display the list
             */
            binding.executePendingBindings()
        }

        companion object {
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
     * Used by ListAdapter to calculate the minimum number of changes between
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
