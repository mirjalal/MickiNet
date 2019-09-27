package com.talmir.mickinet.decorator

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 *
 */
open class CustomFragment : Fragment() {
    lateinit var fragmentActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = context as FragmentActivity
    }
}
