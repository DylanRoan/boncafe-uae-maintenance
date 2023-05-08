package com.example.boncafeuaemaintenance

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.TextView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SetDate{
    // Set last maintenance date
    val lastMaintenanceDate = "2023-03-11T16:26:05.000Z"

    // Set Contract Start date and End date
    val contractStartDate = "2023-01-01T16:26:05.000Z"
    val contractEndDate = "2023-12-31T16:26:05.000Z"

    // Update last maintenance date
    @SuppressLint("SetTextI18n")
    fun updateLastMaintenanceDate(view: View){
        val daysAgo = getDaysAgo(lastMaintenanceDate)

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

    // Update contract date ending
    @SuppressLint("SetTextI18n")
    fun updateContractDaysLeft(view: View){
        val daysLeft = getDaysLeft(contractEndDate)

        // Update Contract's Start Date, End Date and Days left
        val txtContractDaysLeft = view.findViewById<TextView>(R.id.txt_contract_daysLeft)
        val txtContractStartDate = view.findViewById<TextView>(R.id.txt_start_date)
        val txtContractEndDate = view.findViewById<TextView>(R.id.txt_end_date)
        txtContractDaysLeft.text = "($daysLeft Days Left)"
        txtContractStartDate.text = contractStartDate.substring(0,10)
        txtContractEndDate.text = contractEndDate.substring(0,10)

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
        val daysAgo = currentDate.toEpochDay() - pastDate.toEpochDay()

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
}