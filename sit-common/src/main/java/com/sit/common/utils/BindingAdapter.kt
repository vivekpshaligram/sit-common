package com.sit.common.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("setViewAdapter")
fun bindItemViewModels(viewPager2: ViewPager2, adapter: RecyclerView.Adapter<*>) {
    viewPager2.adapter = adapter
}

@BindingAdapter("setRecyclerAdapter")
fun bindItemViewModels(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    recyclerView.adapter = adapter
}