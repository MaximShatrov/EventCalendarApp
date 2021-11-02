package com.shatrovmaxim.myapplication.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.shatrovmaxim.myapplication.R
import com.shatrovmaxim.myapplication.repository.entity.EventEntity

class EventAdapterRecyclerView(
    private val events: List<EventEntity>,
    private val listener: OpenViewListener
) :
    RecyclerView.Adapter<EventAdapterRecyclerView.ViewHolder>() {

    override fun getItemCount() = events.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.event_card_list_old2, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initCard(events[position], listener)
        // обработка нажатия
       /*
        holder.eventName.setOnClickListener { // вызываем метод слушателя, передавая ему данные
            listener.openView(events[position])
        }*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.tv_event_name)
        val eventBegin: TextView = itemView.findViewById(R.id.tv_event_start)
        val eventFinish: TextView = itemView.findViewById(R.id.tv_event_finish)
        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.cl_card)

        fun initCard(event: EventEntity, listener: OpenViewListener) {
            eventName.text = event.name
            eventBegin.text = event.date_start.toString()
            eventFinish.text = event.date_finish.toString()
            constraintLayout.setOnClickListener { // вызываем метод слушателя, передавая ему данные
                listener.openView(event)
            }
        }

    }
}
