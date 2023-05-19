package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class SetDate{
    /*// Set last maintenance date
    val lastMaintenanceDate = "2023-03-11T16:26:05.000Z"

    // Set Contract Start date and End date
    val contractStartDate = "2023-01-01T16:26:05.000Z"
    val contractEndDate = "2023-12-31T16:26:05.000Z"*/
    val contractEndDate = "2023-05-21T16:26:05.000Z"

    // Update last maintenance date
    @SuppressLint("SetTextI18n")
    fun updateLastMaintenanceDate(view: View, passLastMaintenanceDate: String){
        val daysAgo = getDaysAgo(passLastMaintenanceDate)

        // Date Conversion
        val monthsAgo = daysAgo / 30
        val yearsAgo = monthsAgo / 12

        // Update text (number of days ago)
        val txtDateMaintenance = view.findViewById<TextView>(R.id.txt_date_maintenance)
        if (daysAgo == 0) txtDateMaintenance.text = "Today"
        else if (daysAgo < 31) txtDateMaintenance.text = "$daysAgo Days Ago"
        else if (daysAgo < 365) txtDateMaintenance.text = "$monthsAgo Months Ago"
        else txtDateMaintenance.text = "$yearsAgo Years Ago"

        // Update text color (based on: below 3 months, below 6 months, more than 6 months )
        if (daysAgo < 90) txtDateMaintenance.setTextColor(Color.parseColor("#009B06"))
        else if (daysAgo < 180) txtDateMaintenance.setTextColor(Color.parseColor("#F29D38"))
        else txtDateMaintenance.setTextColor(Color.parseColor("#FF0000"))
    }

    // Update maintenance due date
    @SuppressLint("SetTextI18n")
    fun updateMaintenanceDueDate(view: View, passMaintenanceDueDate: String){
        val daysLeft = getDaysLeft(passMaintenanceDueDate)

        // Update maintenance due date text
        val txtMaintanceDueDate = view.findViewById<TextView>(R.id.txt_date_maintenance)
        txtMaintanceDueDate.text = passMaintenanceDueDate.substring(0,10)

        // Update text color of number of days ago
        val txtDateMaintenanceDays = view.findViewById<TextView>(R.id.txt_maintenancedue_daysleft)
        txtDateMaintenanceDays.text = "($daysLeft Days Left)"
        if (daysLeft <= 7) txtDateMaintenanceDays.setTextColor(Color.parseColor("#FF0000"))
        else if (daysLeft <= 31)  txtDateMaintenanceDays.setTextColor(Color.parseColor("#F29D38"))
        else  txtDateMaintenanceDays.setTextColor(Color.parseColor("#009B06"))
    }

    // Update contract date ending
    @SuppressLint("SetTextI18n")
    fun updateContractDaysLeft(view: View, passStartContactDate: String, passEndContactDate: String ){
        val daysLeft = getDaysLeft(passEndContactDate)

        // Update Contract's Start Date, End Date and Days left
        val txtContractDaysLeft = view.findViewById<TextView>(R.id.txt_contract_daysLeft)
        val txtContractStartDate = view.findViewById<TextView>(R.id.txt_start_date)
        val txtContractEndDate = view.findViewById<TextView>(R.id.txt_end_date)
        txtContractDaysLeft.text = "($daysLeft Days Left)"
        txtContractStartDate.text = passStartContactDate.substring(0,10) //THIS VIEW IS GONE
        txtContractEndDate.text = passEndContactDate.substring(0,10) // THIS VIEW IS GONE

        // Update Contract's Start Date
        val (startDay,startMonth,startYear) = passSeperateDate(passStartContactDate)
        val txtStartDay = view.findViewById<TextView>(R.id.txt_start_day)
        txtStartDay.text = startDay
        val txtStartMonthYear = view.findViewById<TextView>(R.id.txt_start_month_year)
        txtStartMonthYear.text = "$startMonth, $startYear"

        // Update Contract's ENd Date
        val (endDay,endMonth,endYear) = passSeperateDate(passEndContactDate)
        val txtEndDay = view.findViewById<TextView>(R.id.txt_end_day)
        txtEndDay.text = endDay
        val txtEndMonthYear = view.findViewById<TextView>(R.id.txt_end_month_year)
        txtEndMonthYear.text = "$endMonth, $endYear"

        // Update days left text color
        if (daysLeft <= 7) txtContractDaysLeft.setTextColor(Color.parseColor("#FF0000"))
        else if (daysLeft <= 31)  txtContractDaysLeft.setTextColor(Color.parseColor("#F29D38"))
        else  txtContractDaysLeft.setTextColor(Color.parseColor("#009B06"))
    }

    /**
     * Calculate number of days ago
     * @param date Pass a date as "yyyy-MM-dd" (as String)
     * @return number of days ago (as Int)
     * */
    @SuppressLint("NewApi")
    fun getDaysAgo(date: String): Int {
        val dateSplit = date.substring(0,10)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val pastDate = LocalDate.parse(dateSplit, formatter)
        val currentDate = LocalDate.now()
        val daysAgo = pastDate.toEpochDay() - currentDate.toEpochDay()

        return daysAgo.toInt()
    }

    /**
     * Calculate number of days left
     * @param date Pass a date as "yyyy-MM-dd" (as String)
     * @return number of days left (as Int)
     * */
    @SuppressLint("NewApi")
    fun getDaysLeft(date: String): Int {
        val dateSplit = date.substring(0,10)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val futureDate = LocalDate.parse(dateSplit, formatter)
        val currentDate = LocalDate.now()
        val daysLeft = futureDate.toEpochDay() - currentDate.toEpochDay()

        return daysLeft.toInt()
    }

    /**
     * To convert date to another date format
     * @param passDate Pass a date as "yyyy-MM-dd" (as String)
     * @return new date format
     * */
    fun formatDateConvert(passDate: String?): String {
        val oldDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val newDateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

        val date = oldDateFormat.parse(passDate)
        return newDateFormat.format(date)
    }

    /**
     * To convert format date into day, month and year separately
     * @param passDate Pass a date as "yyyy-MM-dd" (as String)
     * @return day, month, year in separate variables
     * */
    fun passSeperateDate(passDate: String): Triple<String, String, String> {
        val oldDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = oldDateFormat.parse(passDate)

        val day = SimpleDateFormat("dd", Locale.getDefault()).format(date)
        val month = SimpleDateFormat("MMM", Locale.getDefault()).format(date)
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)

        return Triple(day, month, year)
    }
}