package com.junho.admob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainRecyclerAdapter(private val items: ArrayList<DutchItem>) : RecyclerView.Adapter<MainRecyclerAdapter.ViewHodler>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerAdapter.ViewHodler{
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return MainRecyclerAdapter.ViewHodler(inflatedView)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: MainRecyclerAdapter.ViewHodler, position: Int) {

        holder.apply {

        }

    }

    class ViewHodler(v: View): RecyclerView.ViewHolder(v) {
        fun bind(listener: View.OnClickListener, item: DutchItem) {

        }
    }

}