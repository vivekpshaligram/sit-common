package com.sit.common.ext

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.onBackPressedCustomAction(action: () -> Unit) {
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override
        fun handleOnBackPressed() {
            action()
        }
    })
}