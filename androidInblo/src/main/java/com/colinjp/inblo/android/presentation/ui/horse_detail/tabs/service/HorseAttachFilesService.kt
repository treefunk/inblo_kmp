package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.service

import com.colinjp.inblo.datasource.network.horse_detail.responses.GetDailyAttachedFileResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.utils.io.core.*
import java.io.File

class HorseAttachFilesService (
    private val httpClient: HttpClient,
    private val baseUrl: String,
){
    @OptIn(InternalAPI::class)
    suspend fun attachFile(
        dailyReportId: String,
        file: File,
        contentType: String,
        uploadListener: UploadListener
    ): GetDailyAttachedFileResponse {
        return httpClient.submitForm {
        url("${baseUrl}/${contentType}/attachfile/${dailyReportId}")
        headers {
            append("Accept", ContentType.Application.Json)
        }
        body = MultiPartFormDataContent(
            formData {

//                    append("content_type",contentType)
                append("file",file.readBytes(), Headers.build {
//                    append(HttpHeaders.ContentType, "image/jpg")
                    append(HttpHeaders.ContentDisposition, " filename=${file.name}")
                })
//                appendInput("file",headers = Headers.build {
//                    append(HttpHeaders.ContentDisposition,"filename=${file.name}")
//                }, size = file.length()){ buildPacket { writeFully(file.readBytes()) }}
            }
        )

//            headers {
//                append("Accept", ContentType.Application.Json)
//            }
//            formData {
//                append("content_type",contentType)
//                appendInput("file",headers = Headers.build {
//                    append(HttpHeaders.ContentDisposition,"filename=${file.absolutePath}")
//                }, size = file.length()){ buildPacket { writeFully(file.readBytes()) }}
//            }
        onUpload { bytesSentTotal, contentLength -> uploadListener.onUpload(bytesSentTotal,contentLength) }
    }
}

    interface UploadListener {
        fun onUpload(bytesSentTotal: Long, contentLength: Long)
    }
}
