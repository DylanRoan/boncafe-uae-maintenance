package com.example.boncafeuaemaintenance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_pdf_sample.*

class PDF_Sample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_sample)

        // Load PDF File
        val assetManger = this.assets
        val inputStream = assetManger.open("service_report_sample.pdf")
        view_pdf.fromStream(inputStream).load()

        // Go back
        icon_back.setOnClickListener {
            onBackPressed()
        }
    }
}