package com.shatrovmaxim.myapplication

import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.DatePicker
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener
import com.shatrovmaxim.myapplication.repository.EventJsonFileRepository
import com.shatrovmaxim.myapplication.repository.entity.EventEntity
import com.shatrovmaxim.myapplication.repository.service.EventService
import com.shatrovmaxim.myapplication.repository.service.EventServiceImpl
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/*
* Экран создания нового дела
* Получает Timestamp из MainActivity для предварительного задания даты нового дела.
 */
class NewEventActivity : AppCompatActivity() {
    private val eventService: EventService = EventServiceImpl(EventJsonFileRepository())
    private var selectedDateCalendar: Calendar = Calendar.getInstance()
    private var timestampStart: Timestamp = Timestamp(ZERO_HOUR_TIMESTAMP)
    private var timestampFinish: Timestamp = Timestamp(ONE_HOUR_TIMESTAMP)
    private lateinit var textViewStartTime: TextView
    private lateinit var textViewFinishTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)
        init()
    }

    /*
    * Инициализация вьюшек NewEventActivity и обработчиков событий
     */
    private fun init() {
        selectedDateCalendar.timeInMillis = intent.getSerializableExtra("Timestamp") as Long
        val editTextEventTitle: EditText = findViewById(R.id.et_eventTitle)
        val editTextEventDescription: EditText = findViewById(R.id.et_eventDescription)


        val textViewDate: TextView = findViewById(R.id.tv_NewEventDate)
        textViewDate.text =
            SimpleDateFormat("EEEE, dd MMMM YYYY г.").format(selectedDateCalendar.time).capitalize()

        textViewStartTime = findViewById(R.id.tv_newEventStartTime)
        textViewStartTime.setOnClickListener {
            timePickerShow(timestampStart, textViewStartTime)
        }

        textViewFinishTime = findViewById(R.id.tv_newEventFinishTime)
        textViewFinishTime.setOnClickListener {
            timePickerShow(timestampFinish, textViewFinishTime)
        }
        textViewTimeSet()

        val datePickerBuilder = DatePickerBuilder(
            this,
            OnSelectDateListener { calendar ->
                selectedDateCalendar = calendar.get(0)
                textViewDate.text =
                    SimpleDateFormat("EEEE, dd MMMM YYYY").format(selectedDateCalendar.time)
                        .capitalize()
            })
            .setPickerType(CalendarView.ONE_DAY_PICKER)
        datePickerBuilder.setDate(selectedDateCalendar)
        val datePicker: DatePicker = datePickerBuilder.build()

        textViewDate.setOnClickListener {
            datePicker.show()
        }

        findViewById<Button>(R.id.bt_saveButton)?.apply {
            this.setOnClickListener {
                if (editTextEventTitle.text.toString().equals("")) {
                    val toast: Toast = Toast.makeText(
                        this@NewEventActivity,
                        getString(R.string.newEventSetTitleError),
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else if (timestampFinish.time < timestampStart.time) {
                    val toast: Toast = Toast.makeText(
                        this@NewEventActivity,
                        getString(R.string.newEventIncorrectTime),
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                } else {
                    val newEventEntity = EventEntity(
                        NEW_EVENT_ID,
                        Timestamp(timestampStart.time + selectedDateCalendar.timeInMillis),
                        Timestamp(timestampFinish.time + selectedDateCalendar.timeInMillis),
                        editTextEventTitle.text.toString(),
                        editTextEventDescription.text.toString()
                    )
                    if (eventService.createEvent(newEventEntity).id != NEW_EVENT_ID) {
                        super.onBackPressed()
                    } else {
                        val toast: Toast = Toast.makeText(
                            this@NewEventActivity,
                            getString(R.string.newEventBusy),
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
        }
    }

    /*
    * Задает отображаемую дату начала и конца дела в формате "HH:mm" с учетом часового пояса устройства при первичной инициализации
     */
    private fun textViewTimeSet() {
        val sdf = SimpleDateFormat("HH:mm")
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        textViewStartTime.text = sdf.format(timestampStart)
        textViewFinishTime.text = sdf.format(timestampFinish)

    }

    /*
    * Вызов TimePickerDialog в котором пользователь задает время начала или конца события. Меняет цвет текста textViewFinishTime, если дело начинается позже, чем заканчивается
    * @param timestamp - Timestamp который будет перезаписан, если пользователь выберет время
    * @param textView - TextView которому будет задан текст в формате "HH:mm" с учетом часового пояса устройства, если пользователь выберет время
     */
    private fun timePickerShow(timestamp: Timestamp, textView: TextView) {
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                timestamp.time = (MILLISEC_IN_MIN * ((i * MIN_IN_HOUR) + i2))
                val sdf = SimpleDateFormat("HH:mm")
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                textView.text = sdf.format(timestamp)
                if (timestampFinish.time < timestampStart.time) {
                    textViewFinishTime.setTextColor(Color.RED)
                } else {
                    textViewFinishTime.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorDefaultText
                        )
                    )
                }
            },
            selectedDateCalendar.get(Calendar.HOUR_OF_DAY),
            selectedDateCalendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    companion object {
        val ZERO_HOUR_TIMESTAMP = 0L
        val ONE_HOUR_TIMESTAMP = 3600000L
        val MILLISEC_IN_MIN = 60000L
        val MIN_IN_HOUR = 60
        val NEW_EVENT_ID = -1
    }
}