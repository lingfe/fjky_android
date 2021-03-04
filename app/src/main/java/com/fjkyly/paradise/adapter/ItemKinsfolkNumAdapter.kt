package com.fjkyly.paradise.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fjkyly.paradise.R
import com.fjkyly.paradise.expand.inflate
import com.fjkyly.paradise.model.Kinsfolk

class ItemKinsfolkNumAdapter : RecyclerView.Adapter<ItemKinsfolkNumAdapter.InnerHolder>() {

    private val mKinsfolkList = mutableListOf<Kinsfolk>()
    private lateinit var mClickListener: (kinsfolk: Kinsfolk, position: Int) -> Unit
    private lateinit var mLongClickListener: (kinsfolk: Kinsfolk, position: Int) -> Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemKinsfolkNumAdapter.InnerHolder {
        val itemView = inflate(R.layout.item_kinsfolk_num, parent)
        return InnerHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemKinsfolkNumAdapter.InnerHolder, position: Int) {
        val kinsfolk = mKinsfolkList[position]
        holder.run {
            itemView.run {
                setOnClickListener {
                    if (::mClickListener.isInitialized) mClickListener(kinsfolk, position)
                }
                setOnLongClickListener {
                    if (::mLongClickListener.isInitialized) mLongClickListener(kinsfolk, position)
                    true
                }
            }
            relativesNameTv.text = kinsfolk.name
            relativesPhoneNumTv.text = kinsfolk.phoneNumber
        }
    }

    fun setOnItemClickListener(listener: (kinsfolk: Kinsfolk, position: Int) -> Unit) {
        mClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (kinsfolk: Kinsfolk, position: Int) -> Unit) {
        mLongClickListener = listener
    }

    override fun getItemCount(): Int = mKinsfolkList.size

    fun setData(data: MutableList<Kinsfolk>) {
        mKinsfolkList.run {
            clear()
            addAll(data)
            notifyDataSetChanged()
        }
    }

   fun deleteByKinsfolkId(kinsfolk: Kinsfolk) {
        mKinsfolkList.removeIf { it.id == kinsfolk.id }
        notifyDataSetChanged()
    }

    inner class InnerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relativesNameTv: TextView = itemView.findViewById(R.id.relativesNameTv)
        val relativesPhoneNumTv: TextView = itemView.findViewById(R.id.relativesNumTv)
    }
}