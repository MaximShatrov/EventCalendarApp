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
    val eventService: EventService = EventServiceImpl(EventJsonFileRepository())
    lateinit var eventName: TextView
    lateinit var description: TextView
    lateinit var eventTime: TextView
    lateinit var buttonDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        val event = intent.getSerializableExtra("Event") as EventEntity

        eventName = findViewById(R.id.tv_event_name)
        eventName.text = event.name

        description = findViewById(R.id.tv_description)
        description.text = event.description

        eventTime = findViewById(R.id.tv_eventTime)
        eventTime.text =
            SimpleDateFormat("EEEE, dd MMMM HH:mm").format(Date(event.date_start.getTime()))
                .capitalize() + SimpleDateFormat(" - dd MMMM HH:mm").format(Date(event.date_finish.getTime()))

        buttonDelete = findViewById(R.id.bt_deleteEvent)
        buttonDelete.setOnClickListener {
            eventService.deleteEvent(event)
            super.onBackPressed()
        }
    }
}