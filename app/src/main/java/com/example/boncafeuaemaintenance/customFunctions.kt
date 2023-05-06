package com.example.boncafeuaemaintenance

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class CustomFunctions {
    /** To make substring(s) have their own color and clickable URL
     *
     * @param _text Pass a text string (as String)
     * @param _clickableLink Make the substring clickable URL? (as Boolean)
     * @param _context Pass what context/activity it is (as Context)
     * @param _link Pass URL(s) (as String Array)
     * @param _spanSubstrings Pass which substrings to be span (as String Array)
     * @param _colorInHex Pass colour hex code (as String Array)
     *
     * @return substring(s) with its set colour (as SpannableString) **/
    fun spanTextCustom(_text: String, _clickableLink : Boolean, _context: Context,  _link: Array<String>, _spanSubstrings: Array<String>, _colorInHex: Array<String>): SpannableString {
        val spannableString = SpannableString(_text)

        for (i in _spanSubstrings.indices){
            val startIndex = _text.indexOf(_spanSubstrings[i])
            val endIndex = startIndex + _spanSubstrings[i].length
            
            if (_clickableLink){
                spannableString.setSpan(object : ClickableSpan(){
                    // Handle click events
                    override fun onClick(view: View) {
                        /*Toast.makeText(view.context, "Clicked $i", Toast.LENGTH_SHORT).show()*/
                        openURL(_context, _link[i])
                    }

                    // Customize span text
                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.color = Color.parseColor(_colorInHex[i])
                        ds.bgColor = 0
                        ds.isUnderlineText = false
                    }
                }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)    
            } else {
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(_colorInHex[i])),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            
        }

        return spannableString
    }

    /** Open URL
     * @param _context Pass context/activity (as Context)
     * @param _URL Pass URL (as string) **/
    fun openURL(_context: Context, _URL: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(_URL))
        _context.startActivity(intent)
    }
}