package com.example.boncafeuaemaintenance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    //------ STORE SERIAL IDS ------//
    val serialIDs: MutableList<String> = mutableListOf()
    val concatenatedSerialIDs: MutableLiveData<String> = MutableLiveData()

    // Store serial IDs
    fun addSerialID(inputSerialText: String) {
        serialIDs.add(inputSerialText)
        updateConcatenatedSerialIDs()
    }

    // For checking if user makes any changes to the EditText
    fun updateSerialID(index: Int, inputSerialText: String) {
        serialIDs[index] = inputSerialText
        updateConcatenatedSerialIDs()
    }

    fun removeSerialID(index:Int){
        serialIDs.removeAt(index)
    }

    // Get all serial IDs and then put them into one string
    private fun updateConcatenatedSerialIDs() {
        val concatenatedText = serialIDs.joinToString("\n")
        concatenatedSerialIDs.value = concatenatedText
    }
    //------------------------------//

    private val getText = MutableLiveData<String>()

    fun setData(text: String) {
        getText.value = text
    }

    fun getData(): LiveData<String> {
        return getText
    }
}