package com.sit.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
class BaseAdapter<T : Any, VB : ViewDataBinding, BVH : BaseAdapter.BaseViewHolder<T>>(
    private val items: ArrayList<T>,
    private val layoutId: Int,
    private val viewHolderFactory: (VB) -> BVH,
) : RecyclerView.Adapter<BVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BVH  {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<VB>(inflater, layoutId, parent, false)
        return viewHolderFactory(binding)
    }

    override fun onBindViewHolder(holder: BVH , position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: T) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun addItemsAll(items: List<T>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItemAt(position: Int) {
        this.items.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateItemAt(position: Int, newItem: T) {
        this.items[position] = newItem
        notifyDataSetChanged()
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}