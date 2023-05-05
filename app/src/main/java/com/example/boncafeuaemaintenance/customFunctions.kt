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
     * @param substringsAmount Set amount on how many substrings to be coloured (as Int)
     * @return substring(s) with its set colour (as SpannableString) **/
    fun spanTextColor(_text: String, startIndexValue: Array<Int>, endIndexValue: Array<Int>, substringsAmount: Int, colorInHex: Array<String>): SpannableString {
        val spannableString = SpannableString(_text)

        for (i in 0 until substringsAmount){
            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor(colorInHex[i])),
                startIndexValue[i],
                endIndexValue[i],
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannableString
    }
}