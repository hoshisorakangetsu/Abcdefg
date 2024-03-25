package com.example.abcdefg.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.abcdefg.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Utils {

    public companion object {
        fun hideSoftKeyboard(activity: Activity, view: View) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0)
        }

        fun createChip(context: Context, tag: String): Chip {
            val chip = Chip(context)
            chip.text = tag
            val drawable =
                ChipDrawable.createFromAttributes(context, null, 0, R.style.PrimaryChip)
            chip.setChipDrawable(drawable)
            chip.isChipIconVisible = false
            chip.isCloseIconVisible = false
            chip.isCheckable = false
            chip.chipStrokeWidth = 0f
            chip.minHeight = 0
            chip.minWidth = 0
            chip.chipStrokeColor = null
            return chip
        }

        fun createSelectableChip(context: Context, tag: String): Chip {
            val chip = Chip(context)
            chip.text = tag
            val drawable =
                ChipDrawable.createFromAttributes(context, null, 0, R.style.PrimaryChip)
            chip.setChipDrawable(drawable)
            chip.isChipIconVisible = false
            chip.isCloseIconVisible = false
            chip.isCheckable = true
            chip.chipStrokeWidth = 0f
            chip.minHeight = 0
            chip.minWidth = 0
            chip.chipStrokeColor = null
            return chip
        }

    }
}

@SuppressLint("SimpleDateFormat")
fun EditText.transformIntoDatePicker(format: String) {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    isCursorVisible = false

    val myCalendar = Calendar.getInstance()
    val datePickerOnDataSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val sdf = SimpleDateFormat(format)
            setText(sdf.format(myCalendar.time))
        }

    setOnClickListener {
        DatePickerDialog(
            context, datePickerOnDataSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).run {
            show()
        }
    }
}

fun EditText.transformIntoTimePicker() {
    isFocusableInTouchMode = false
    isClickable = true
    isFocusable = false
    isCursorVisible = false

    val timePickerOnTimeSelectListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            setText(when {
                hourOfDay == 0 -> {
                    if (minute < 10) {
                        "12:0${minute} am"
                    } else {
                        "12:${minute} am"
                    }
                }
                hourOfDay > 12 -> {
                    if (minute < 10) {
                        "${hourOfDay - 12}:0${minute} pm"
                    } else {
                        "${hourOfDay - 12}:${minute} pm"
                    }
                }
                hourOfDay == 12 -> {
                    if (minute < 10) {
                        "${hourOfDay}:0${minute} pm"
                    } else {
                        "${hourOfDay}:${minute} pm"
                    }
                }
                else -> {
                    if (minute < 10) {
                        "${hourOfDay}:${minute} am"
                    } else {
                        "${hourOfDay}:${minute} am"
                    }
                }
            } )
        }

    setOnClickListener {
        val cal = Calendar.getInstance()
        TimePickerDialog(
            context, timePickerOnTimeSelectListener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true
        ).run {
            show()
        }
    }
}
