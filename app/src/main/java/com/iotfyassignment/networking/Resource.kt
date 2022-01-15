package com.iotfyassignment.networking

import android.util.Log
import org.json.JSONObject
import retrofit2.Response


sealed class Resource<T>(
    val data:T? =null,
    val message:String? = null
){
    class Success<T>(data:T): Resource<T>(data)
    class Error<T>(data:T?=null,error:String): Resource<T>(data,error)
    class Loading<T>(data:T?=null,error:String?=null): Resource<T>(data,error)
    class Empty<T>(data:T?=null,error:String?=null): Resource<T>(data,error)
}
class CheckResponse<T>(private val response: Response<T>) {
    fun handleResponse(): Resource<T> {
        val result = response.body()
        Log.e("TAG", "handleResponse: "+response)
        return if (response.isSuccessful && result != null) {
            Resource.Success(result)
        } else {
            val error = response.errorBody()?.string()
            if (error != null) {
                val jsonObjectError = JSONObject(error)
                Log.e("TAG",jsonObjectError.toString())
                Resource.Error(null, jsonObjectError.getString("message") ?: response.message())
            } else
                Resource.Error(null, response.message())
        }
    }
}

fun Exception.getCause():String{
    return this.cause?.message ?: "Timeout"
}