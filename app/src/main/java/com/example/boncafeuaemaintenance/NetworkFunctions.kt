package com.example.boncafeuaemaintenance

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class NetworkFunctions {

    @RequiresApi(Build.VERSION_CODES.M)
    fun hasNetwork(context : Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    val client = OkHttpClient()

    fun getRequest(
        context: Context,
        url: String,
        func: (Context, JSONArray) -> Unit
    ) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {e.printStackTrace()}

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body()?.string()?.let { setToJson(it, context, func) }
                }
            }

        })
    }

    fun loginRequest(
        context: Context,
        url: String,
        func: (Context, JSONArray) -> Unit,
        password: String,
        email: String
    ) {
        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {e.printStackTrace()}

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body()?.string()?.let { setToJson(it, context, func) }
                }
            }

        })
    }

    fun maintenancePageRequest(
        view: View,
        context: Context,
        url: String,
        func: (View, Context, JSONArray) -> Unit,
        password: String,
        email: String
    ) {
        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {e.printStackTrace()}

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body()?.string()?.let {
                        var objstring = it

                        if (objstring.substring(0, 1) != "[")
                            objstring = "[$it]"

                        var obj = JSONArray(objstring)

                        func(view, context, obj)
                    }
                }
            }

        })
    }

    //boncafe-backend.herokuapp.com/email
    fun emailRequest(
        context: Context,
        func: (Context, JSONArray) -> Unit,
        password: String,
        email: String,
        text: String,
        html: String
    ) {
        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("email", email)
            .addFormDataPart("password", password)
            .addFormDataPart("text", text)
            .addFormDataPart("html", html)
            .build()

        val request = Request.Builder()
            .url("http://boncafe-backend.herokuapp.com/email")
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {e.printStackTrace()}

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    response.body()?.string()?.let { setToJson(it, context, func) }
                }
            }

        })
    }

    fun setToJson(
        result: String,
        context: Context,
        func: (Context, JSONArray) -> Unit
    )
    {

        var objstring = result

        if (objstring.substring(0, 1) != "[")
            objstring = "[$result]"

        var obj = JSONArray(objstring)

        func(context, obj)
    }
}