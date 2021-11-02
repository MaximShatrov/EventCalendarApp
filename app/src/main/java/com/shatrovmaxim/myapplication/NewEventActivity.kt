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


class NewEventActivity : AppCompatActivity() {
    private val eventService: EventService = EventServiceImpl(EventJsonFileRepository())
    private var selectedDateCalendar: Calendar = Calendar.getInstance()
    private lateinit var timestampStart: Timestamp
    private lateinit var timestampFinish: Timestamp
    private lateinit var textViewStartTime: TextView
    private lateinit var textViewFinishTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_event)
        selectedDateCalendar.timeInMillis = intent.getSerializableExtra("Timestamp") as Long
        timestampStart = Timestamp(0L)
        timestampFinish = Timestamp(3600000L)
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

        val builder = DatePickerBuilder(
            this,
            OnSelectDateListener { calendar ->
                selectedDateCalendar = calendar.get(0)
                textViewDate.text =
                    SimpleDateFormat("EEEE, dd MMMM YYYY").format(selectedDateCalendar.time)
                        .capitalize()
            })
            .setPickerType(CalendarView.ONE_DAY_PICKER)
        builder.setDate(selectedDateCalendar)
        val datePicker: DatePicker = builder.build()

        textViewDate.setOnClickListener {
            datePicker.show()
        }


        val saveButton: Button = findViewById(R.id.bt_saveButton)
        saveButton.setOnClickListener {
            if (editTextEventTitle.text.toString().equals("")){
                val toast: Toast = Toast.makeText(this,getString(R.string.newEventSetTitleError),Toast.LENGTH_SHORT)
                toast.show()
            } else if (timestampFinish.time < timestampStart.time){
                val toast: Toast = Toast.makeText(this,getString(R.string.newEventIncorrectTime),Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val newEventEntity = EventEntity(
                    -1,
                    Timestamp(timestampStart.time + selectedDateCalendar.timeInMillis),
                    Timestamp(timestampFinish.time + selectedDateCalendar.timeInMillis),
                    editTextEventTitle.text.toString(),
                    editTextEventDescription.text.toString()
                )
                if (eventService.createEvent(newEventEntity).id != -1){
                    super.onBackPressed()
                } else {
                    val toast: Toast = Toast.makeText(this,getString(R.string.newEventBusy),Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
    }

    private fun textViewTimeSet() {
        val sdf = SimpleDateFormat("HH:mm")
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        textViewStartTime.text = sdf.format(timestampStart)
        textViewFinishTime.text = sdf.format(timestampFinish)

    }

    private fun timePickerShow(timestamp: Timestamp, textView: TextView) {

        val timePickerDialog: TimePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { timePicker, i, i2 ->
                timestamp.time = (60000L * ((i * 60) + i2))
                val sdf = SimpleDateFormat("HH:mm")
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                textView.text = sdf.format(timestamp)
                if (timestampFinish.time < timestampStart.time) {
                    textViewFinishTime.setTextColor(Color.RED)
                } else {
                    textViewFinishTime.setTextColor(ContextCompat.getColor(this, R.color.colorDefaultText))
                }
            },
            selectedDateCalendar.get(Calendar.HOUR_OF_DAY),
            selectedDateCalendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}