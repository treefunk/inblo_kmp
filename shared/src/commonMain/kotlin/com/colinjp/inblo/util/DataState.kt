package com.colinjp.inblo.util

import com.colinjp.inblo.datasource.network.MetaResponse
import com.colinjp.inblo.domain.model.Message


sealed class DataState<out R> {

    data class Data<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    data class Loading(val isLoading: Boolean) : DataState<Nothing>()
    object Empty: DataState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Data<*> -> "dATA[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading[isLoading=$isLoading]"
            is Empty -> "EmptyState"
        }
    }
}

//data class DataState<T>(
//    val data: T? = null,
//    val
//)


//data class DataState<T>(
//    val data: T? = null,
//    val isError: Exception? = null,
//    val isLoading: Boolean? = false,
//) {
//
//
//    data class Data<T>(val d: T) : return DataState<T>()
//    data class Error(val exception: Exception) : DataState<Nothing>()
//    data class Loading(val isLoading: Boolean) : DataState<Nothing>()
//    object Empty: DataState<Nothing>()
//    companion object {
//
//        fun <T> data(
//            data: T? = null,
//        ): DataState<T> {
//            return DataState(
//                data = data,
//            )
//        }
//        fun <T> error(exception: Exception) = DataState<T>(isError = exception)
//
//
//        fun <T> loading(isLoading: Boolean) = DataState<T>(isLoading = isLoading)
//    }
//}