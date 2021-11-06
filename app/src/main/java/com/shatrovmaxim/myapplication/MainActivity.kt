package com.shatrovmaxim.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shatrovmaxim.myapplication.presentation.EventMapAdapterRecyclerView
import com.shatrovmaxim.myapplication.presentation.OpenViewListener
import com.shatrovmaxim.myapplication.repository.EventJsonFileRepository
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import com.shatrovmaxim.myapplication.repository.service.EventService
import com.shatrovmaxim.myapplication.repository.service.EventServiceImpl
import com.shatrovmaxim.myapplication.utils.EventMapMaker
import java.text.SimpleDateFormat
import java.util.*

/*
* Главный экран приложения с возможностью выбрать дату в CalendarView и таймлайном дел в выбранное число
 */
class MainActivity : AppCompatActivity() {
    private lateinit var eventService: EventService
    private var selectedCalendarDate = Calendar.getInstance()
    private val mapMaker = EventMapMaker()

    private lateinit var toolbarTextViewDate: TextView
    private lateinit var toolbarTodayIcon: ImageView
    private lateinit var calendarView: CalendarView
    private lateinit var recyclerViewTimeline: RecyclerView
    private lateinit var fabNewEvent: FloatingActionButton

    private var listener: OpenViewListener = object : OpenViewListener {
        override fun openView(eventEntity: EventEntity) {
            val intent = Intent(this@MainActivity, EventDetailsActivity::class.java)
            intent.putExtra("Event", eventEntity)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onResume() {
        super.onResume()
        eventService = EventServiceImpl(EventJsonFileRepository())
        refreshToolbarTitle(selectedCalendarDate)
        addEventsIcons()
        refreshRecyclerView(selectedCalendarDate)
    }

    /*
    *   Инициализация вьюшек MainActivity и обработчиков событий
    */
    private fun init() {
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
            selectedCalendarDate = Calendar.getInstance()
            calendarView.setDate(selectedCalendarDate)
            refreshRecyclerView(selectedCalendarDate)
            refreshToolbarTitle(selectedCalendarDate)
        }

        recyclerViewTimeline = findViewById(R.id.rv_eventsList)
        recyclerViewTimeline.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        fabNewEvent = findViewById(R.id.floatingActionButton)
        fabNewEvent.setOnClickListener {
            val intent = Intent(this@MainActivity, NewEventActivity::class.java)
            intent.putExtra("Timestamp", selectedCalendarDate.timeInMillis)
            startActivity(intent)
        }
    }

    /*
    * Обновление элементов RecyclerView
    * @param calendar - календарь, на основании которого из eventService берутся events, преобразуются в MutableMap<Int, EventEntity> и помещаются в RecyclerView
     */
    private fun refreshRecyclerView(calendar: Calendar) {
        recyclerViewTimeline.adapter =
            EventMapAdapterRecyclerView(
                mapMaker.getMapOfEvents(
                    eventService.getEventsByCalendarDate(
                        calendar
                    )
                ), listener
            )
        recyclerViewTimeline.scrollToPosition(POSITION_TO_SCROLL)
    }

    /*
    * Обновление toolbarTextViewDate
    * @param calendar - дата помещаемая в toolbar
     */
    private fun refreshToolbarTitle(calendar: Calendar) {
        toolbarTextViewDate.text = SimpleDateFormat("dd MMMM YYYY").format(calendar.time)
    }

    /*
    * Создание EventDay из всех EventEntity в eventService. Установка их в CalendarView для отображение иконок событий
     */
    private fun addEventsIcons() {
        val eventsDays: MutableList<EventDay> = ArrayList()
        eventService.findAll().forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.date_start.time
            eventsDays.add(EventDay(calendar, R.drawable.ic_event_dot))
        }
        calendarView.setEvents(eventsDays)
    }

    companion object {
        val POSITION_TO_SCROLL = 8
    }
}