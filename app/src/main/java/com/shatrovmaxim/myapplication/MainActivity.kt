package com.shatrovmaxim.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shatrovmaxim.myapplication.presentation.EventAdapterRecyclerView
import com.shatrovmaxim.myapplication.presentation.EventMapAdapterRecyclerView
import com.shatrovmaxim.myapplication.presentation.OpenViewListener
import com.shatrovmaxim.myapplication.repository.EventJsonFileRepository
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import com.shatrovmaxim.myapplication.repository.service.EventService
import com.shatrovmaxim.myapplication.repository.service.EventServiceImpl
import com.shatrovmaxim.myapplication.utils.EventMapMaker
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var eventService: EventService
    var selectedCalendarDate = Calendar.getInstance()
    val mapMaker = EventMapMaker()

    lateinit var toolbarTextViewDate: TextView
    lateinit var toolbarTodayIcon: ImageView
    lateinit var calendarView: CalendarView
    lateinit var recyclerViewTimeline: RecyclerView

    lateinit var fabNewEvent: FloatingActionButton
    var listener: OpenViewListener = object : OpenViewListener {
        override fun openView(eventEntity: EventEntity) {
            val intent = Intent(this@MainActivity, EventDetailsActivity::class.java)
            intent.putExtra("Event", eventEntity)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView = findViewById(R.id.calendarView)
        calendarView.setOnDayClickListener {
            selectedCalendarDate = it.calendar
            refreshToolbarTitle(it.calendar)
            refreshRecyclerView(it.calendar)
        }


        toolbarTextViewDate = findViewById(R.id.tv_toolbarDate)
        toolbarTextViewDate.text =
            SimpleDateFormat("d MMMM YYYY").format(selectedCalendarDate.time)
        toolbarTextViewDate.setOnClickListener {
            val imageView: ImageView = findViewById(R.id.iv_dateArrow)
            if (calendarView.visibility == View.VISIBLE) {
                calendarView.visibility = View.GONE
                imageView.rotation = 180f
            } else {
                calendarView.visibility = View.VISIBLE
                imageView.rotation = 0f
            }
        }

        toolbarTodayIcon = findViewById(R.id.iv_todayIcon)
        toolbarTodayIcon.setOnClickListener {
            setTodayDate()
            refreshRecyclerView(selectedCalendarDate)
            refreshToolbarTitle(selectedCalendarDate)
        }

        recyclerViewTimeline = findViewById(R.id.rv_eventsList)
        recyclerViewTimeline.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //refreshRecyclerView(selectedCalendarDate)

        fabNewEvent = findViewById(R.id.floatingActionButton)
        fabNewEvent.setOnClickListener {
            val intent = Intent(this@MainActivity, NewEventActivity::class.java)
            intent.putExtra("Timestamp", selectedCalendarDate.timeInMillis)
            startActivity(intent)
        }

        //debug button
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            println(eventService.findAll())
            println(eventService.findAll().sorted())
            recyclerViewTimeline.adapter =
                EventAdapterRecyclerView(
                    eventService.findAll().sortedBy { it.date_start.time },
                    listener
                )
        }
    }

    override fun onResume() {
        super.onResume()
        eventService = EventServiceImpl(EventJsonFileRepository())
        refreshToolbarTitle(selectedCalendarDate)
        addEventsIcons()
        refreshRecyclerView(selectedCalendarDate)
    }

    private fun refreshRecyclerView(calendar: Calendar) {
        recyclerViewTimeline.adapter =
            EventMapAdapterRecyclerView(
                mapMaker.getMapOfEvents(
                    eventService.getEventsByCalendarDate(
                        calendar
                    )
                ), listener
            )
        recyclerViewTimeline.scrollToPosition(8)
    }

    private fun refreshToolbarTitle(calendar: Calendar) {
        toolbarTextViewDate.text = SimpleDateFormat("dd MMMM YYYY").format(calendar.time)
    }

    private fun addEventsIcons() {
        val eventsDays: MutableList<EventDay> = ArrayList()
        eventService.findAll().forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.date_start.time
            eventsDays.add(EventDay(calendar, R.drawable.ic_event_dot))
        }
        calendarView.setEvents(eventsDays)
    }

    private fun setTodayDate() {
        selectedCalendarDate = Calendar.getInstance()
        calendarView.setDate(selectedCalendarDate)
    }
}