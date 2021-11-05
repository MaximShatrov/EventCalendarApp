package com.shatrovmaxim.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.shatrovmaxim.myapplication.repository.EventJsonFileRepository
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import com.shatrovmaxim.myapplication.repository.service.EventService
import com.shatrovmaxim.myapplication.repository.service.EventServiceImpl
import java.text.SimpleDateFormat
import java.util.*

class EventDetailsActivity : AppCompatActivity() {
    private val eventService: EventService = EventServiceImpl(EventJsonFileRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        init()
    }

    private fun init(){
        val event = intent.getSerializableExtra("Event") as EventEntity
        findViewById<TextView>(R.id.tv_eventName).text = event.name
        findViewById<TextView>(R.id.tv_description).text = event.description
        findViewById<TextView>(R.id.tv_eventTime).text =
            SimpleDateFormat("EEEE, dd MMMM HH:mm").format(Date(event.date_start.getTime()))
                .capitalize() + SimpleDateFormat(" - dd MMMM HH:mm").format(Date(event.date_finish.getTime()))
        findViewById<Button>(R.id.bt_deleteEvent)?.apply {
            this.setOnClickListener { eventService.deleteEvent(event)
                super.onBackPressed() }
        }
    }
}