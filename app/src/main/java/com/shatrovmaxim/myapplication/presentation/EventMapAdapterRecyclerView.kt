package com.shatrovmaxim.myapplication.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.shatrovmaxim.myapplication.R
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import java.text.SimpleDateFormat
import java.util.*

class EventMapAdapterRecyclerView(
    val events: Map<Int, EventEntity>,
    private val listener: OpenViewListener
) :
    RecyclerView.Adapter<EventMapAdapterRecyclerView.ViewHolder>() {

    override fun getItemCount() = 24

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.event_card_list_new, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initCard(position, events.get(position), listener)
        // обработка нажатия
        /*
         holder.eventName.setOnClickListener { // вызываем метод слушателя, передавая ему данные
             listener.openView(events[position])
         }*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.tv_event_name)
        val eventTime: TextView = itemView.findViewById(R.id.tv_eventTime)

        //val cardConstraintLayout: ConstraintLayout = itemView.findViewById(R.id.cl_eventCard)
        val cardView: CardView = itemView.findViewById(R.id.cv_eventCard)
        val sectorStart: TextView = itemView.findViewById(R.id.tv_sectorStart)

        //val sectorFinish: TextView = itemView.findViewById(R.id.tv_sectorFinish)
        val calendar = Calendar.getInstance()

        fun initCard(position: Int, event: EventEntity?, listener: OpenViewListener) {
            timelineInit(position)
            if (event == null) cardView.visibility = View.GONE

            else {
                cardView.visibility = View.VISIBLE
                eventName.text = event.name
                calendar.timeInMillis = event.date_start.time
                val simpleDateFormat = SimpleDateFormat("HH:mm")
                eventTime.text =
                    simpleDateFormat.format(Date(event.date_start.getTime())) + " - " + simpleDateFormat.format(
                        Date(event.date_finish.getTime())
                    )
                cardView.setOnClickListener {
                    // вызываем метод слушателя, передавая ему данные
                    listener.openView(event)
                }
            }


        }

        private fun timelineInit(position: Int) {
            if (position < 10) {
                sectorStart.text = "0" + position.toString() + ":00"
            } else sectorStart.text = position.toString() + ":00"

        /*
            if (position in 0..8) {
                sectorFinish.text = "0" + (position + 1).toString() + ":00"
            }
            if (position in 9..22) {
                sectorFinish.text = (position + 1).toString() + ":00"
            }
            if (position == 23) sectorFinish.text = "00:00"*/
        }


        private fun emptyCardInit() {
        }


    }
}
