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

/*
* Экран подробного описания дела, переданного из MainActivity
* Содержит название, дату и промежуток времени, краткое описание дела текстом и кнопку для удаления дела
 */
class EventDetailsActivity : AppCompatActivity() {
    private val eventService: EventService = EventServiceImpl(EventJsonFileRepository())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)
        init()
    }

    /*
    *   Инициализация вьюшек EventDetailsActivity и обработчика событий
    */
    private fun init() {
        val event = intent.getSerializableExtra("Event") as EventEntity
        findViewById<TextView>(R.id.tv_eventName).text = event.name
        findViewById<TextView>(R.id.tv_description).text = event.description
        findViewById<TextView>(R.id.tv_eventTime).text =
            SimpleDateFormat("EEEE, dd MMMM HH:mm").format(Date(event.date_start.getTime()))
                .capitalize() + SimpleDateFormat(" - dd MMMM HH:mm").format(Date(event.date_finish.getTime()))
        findViewById<Button>(R.id.bt_deleteEvent)?.apply {
            this.setOnClickListener {
                eventService.deleteEvent(event)
                super.onBackPressed()
            }
        }
    }
}