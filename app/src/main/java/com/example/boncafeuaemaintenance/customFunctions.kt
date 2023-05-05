package com.example.boncafeuaemaintenance

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

class CustomFunctions {
    /** To set a specific color of a substring
     * @param _text Pass a text string (as String)
     * @param startIndexValue Start position of a substring to be coloured (as Int)
     * @param endIndexValue End position of a substring to be coloured (as Int)
     * @param colorInHex Pass colour hex code (as String)
     * @return substring with its set colour (as SpannableString) **/
    fun spanTextColor(_text: String, startIndexValue: Int, endIndexValue: Int, colorInHex: String): SpannableString {
        val spannableString = SpannableString(_text)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(colorInHex)),
            startIndexValue,
            endIndexValue,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}