package com.talmir.mickinet.decorator

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * [fragmentActivity] will be used in all
 * [Fragment] subclasses.
 */
open class CustomFragment : Fragment() {
    lateinit var fragmentActivity: FragmentActivity
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActivity = context as FragmentActivity
    }
}
