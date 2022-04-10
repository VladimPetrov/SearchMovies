package ru.gb.searchmovies.presentation.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.db.HistoryEntity

class HistoryAdapter:
    RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {
    private var data: List<HistoryEntity> = arrayListOf()
    fun setData(data: List<HistoryEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_history, parent,
                    false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class RecyclerItemViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        fun bind(data: HistoryEntity) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.findViewById<TextView>(R.id.recyclerViewItem).text =
                    String.format("%s %s", data.date, data.name)
                itemView.findViewById<TextView>(R.id.recyclerViewItemNote).text = data.note
                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context,
                        "on click: ${data.name}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
    }
