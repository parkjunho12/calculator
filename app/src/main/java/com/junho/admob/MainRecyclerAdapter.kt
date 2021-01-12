package com.junho.admob

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainRecyclerAdapter(private val items: ArrayList<DutchItem>) : RecyclerView.Adapter<MainRecyclerAdapter.ViewHodler>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerAdapter.ViewHodler{
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return MainRecyclerAdapter.ViewHodler(inflatedView)
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: MainRecyclerAdapter.ViewHodler, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            items.remove(item)
            notifyDataSetChanged()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }


    }

    class ViewHodler(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: DutchItem) {
            val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.resources.configuration.locales[0]
            } else {
                view.resources.configuration.locale
            }
            if (current != Locale.KOREA) {
                view.findViewById<EditText>(R.id.list_edit)
                    .setText("Pay $${item.price} at ${item.place}")
            } else {
                view.findViewById<EditText>(R.id.list_edit)
                    .setText("${item.place}에서 ${item.price}원 결제")
            }
            view.findViewById<TextView>(R.id.list_minus_but).setOnClickListener(listener)
        }
    }

}