package com.colinjp.inblo.datasource.network.horse_detail

import com.colinjp.inblo.datasource.network.BooleanResponse
import com.colinjp.inblo.datasource.network.horse_detail.responses.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import kotlin.collections.get

class HorseDetailServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
): HorseDetailService {
    override suspend fun getHorseDailyReports(horseId: String): GetHorseDailyListResponse {
        return httpClient.get {

            url("$baseUrl/horses/$horseId/daily-reports")
        }
    }

    override suspend fun getHorseConditions(horseId: String): GetHorseConditionListResponse {
        return httpClient.get {
            url("$baseUrl/horses/$horseId/conditions")
        }
    }

    override suspend fun getHorseTrainingRecords(horseId: String): GetHorseTrainingListResponse {
        return httpClient.get {
            url("$baseUrl/horses/$horseId/trainings")
        }
    }

    override suspend fun getHorseTreatmentRecords(horseId: String): GetHorseTreatmentListResponse {
        return httpClient.get {
            url("$baseUrl/horses/$horseId/treatments")
        }
    }

    override suspend fun getHorseStables(): DropDownDataResponse {
        return httpClient.get {
            url("$baseUrl/stables")
        }
    }

    override suspend fun getDailyReportForm(stableId: String): GetDailyReportFormResponse {
        return httpClient.get {
            url("$baseUrl/daily_reports/get-form/$stableId")
        }
    }

    override suspend fun addDailyReport(
        horseId: String,
        date: String,
        bodyTemperature: String,
        horseWeight: String,
        conditionGroup: String,
        riderName: String, // ignored
        trainingType: String, // ignored
        riderId: String?,
        trainingTypeId: String?,
        trainingAmount: String,
        time5f: String,
        time4f: String,
        time3f: String,
        memo: String,
        dailyAttachedIds: List<String>?,
        dailyReportId: String?
    ): GetHorseDailyResponse {
        return if(dailyReportId == null){
            httpClient.post{
                url("$baseUrl/daily-reports")
                parameter("horse_id",horseId)
                parameter("date",date)
                parameter("body_temperature",bodyTemperature)
                parameter("horse_weight",horseWeight)
                parameter("condition_group",conditionGroup)
                parameter("rider_name",riderName)
                parameter("training_type_name",trainingType)
                parameter("training_amount",trainingAmount)
                parameter("5f_time",time5f)
                parameter("4f_time",time4f)
                parameter("3f_time",time3f)
                if(riderId != null){
                    parameter("rider_id",riderId)
                }
                if(trainingTypeId != null){
                    parameter("training_type_id",trainingTypeId)
                }
                if(dailyAttachedIds != null){
                    for(id: String in dailyAttachedIds){
                        parameter("attached_file_ids[]", id)
                    }
                }
                parameter("memo",memo)
            }
        }else{
            httpClient.put{
                url("$baseUrl/daily-reports/${dailyReportId}")
                parameter("horse_id",horseId)
                parameter("date",date)
                parameter("body_temperature",bodyTemperature)
                parameter("horse_weight",horseWeight)
                parameter("condition_group",conditionGroup)
                parameter("rider_name",riderName)
                parameter("training_type_name",trainingType)
                parameter("training_amount",trainingAmount)
                parameter("5f_time",time5f)
                parameter("4f_time",time4f)
                parameter("3f_time",time3f)
                if(riderId != null){
                    parameter("rider_id",riderId)
                }
                if(trainingTypeId != null){
                    parameter("training_type_id",trainingTypeId)
                }
                if(dailyAttachedIds != null){
                    for(id: String in dailyAttachedIds){
                        parameter("attached_file_ids[]", id)
                    }
                }
                parameter("memo",memo)
            }
        }
    }

//    override suspend fun attachFile(
//        dailyReportId: String,
//        file: File,
//        contentType: String
//    ): GetDailyAttachedFileResponse {
//        return httpClient.submitForm {
//            url("${baseUrl}/${contentType}/attachfile/${dailyReportId}")
//
//            headers {
//                append("Accept", ContentType.Application.Json)
//            }
//            body = MultiPartFormDataContent(
//                formData {
//                    PartData.FileItem()
////                    append("content_type",contentType)
//                    append("file",file.readBytes(), Headers.build {
////                    append(HttpHeaders.ContentType, "image/jpg")
//                        append(HttpHeaders.ContentDisposition, " filename=${file.name}")
//                    })
////                appendInput("file",headers = Headers.build {
////                    append(HttpHeaders.ContentDisposition,"filename=${file.name}")
////                }, size = file.length()){ buildPacket { writeFully(file.readBytes()) }}
//                }
//            )
//
////            headers {
////                append("Accept", ContentType.Application.Json)
////            }
////            formData {
////                append("content_type",contentType)
////                appendInput("file",headers = Headers.build {
////                    append(HttpHeaders.ContentDisposition,"filename=${file.absolutePath}")
////                }, size = file.length()){ buildPacket { writeFully(file.readBytes()) }}
////            }
//            onUpload { bytesSentTotal, contentLength -> uploadListener.onUpload(bytesSentTotal,contentLength) }
//        }
//    }

//    override suspend fun attachFile(
//        dailyReportId: String,
//        file: PartData.FileItem,
//        contentType: String
//    ): GetDailyAttachedFileResponse {
//        return httpClient.post {
//            url("daily_reports/attachfile/${dailyReportId}")
//            headers {
//                append("Accept",ContentType.Application.Json)
//            }
//            formData {
//                appendInput("file",headers = Headers.build {
//                    append(HttpHeaders.ContentDisposition,"filename=${filePath}")
//                }, size = fileSize){  buildPacket { writeFully() }  }
//            }
//        }
//    }


    override suspend fun addHorseCondition(
        horseId: String,
        date: String,
        weather: String,
        bodyTemperature: String,
        horseWeight: String,
        accidentSite: String,
        accidentPart: String,
        accidentType: String,
        memo: String,
        horseConditionId: String?
    ): GetHorseConditionResponse {

        return if(horseConditionId == null){
            httpClient.post {
                url("$baseUrl/conditions")
                parameter("horse_id",horseId)
                parameter("date",date)
                parameter("weather",weather)
                parameter("accident_site", accidentSite)
                parameter("accident_part", accidentPart)
                parameter("accident_type", accidentType)
                parameter("body_temperature",bodyTemperature)
                parameter("horse_weight",horseWeight)
                parameter("memo",memo)
            }
        }else{
            httpClient.put{
                url("$baseUrl/conditions/$horseConditionId")
                parameter("horse_id",horseId)
                parameter("date",date)
                parameter("weather",weather)
                parameter("accident_site", accidentSite)
                parameter("accident_part", accidentPart)
                parameter("accident_type", accidentType)
                parameter("body_temperature",bodyTemperature)
                parameter("horse_weight",horseWeight)
                parameter("memo",memo)
            }
        }
    }

    override suspend fun removeHorseDaily(horseDailyId: String): BooleanResponse {
        return httpClient.delete{
            url("$baseUrl/daily-reports/$horseDailyId")
        }
    }


    override suspend fun addHorseTrainingRecord(
        horseId: String,
        date: String,
        weather: String,
        trainingType: String,
        trainingDetail: String,
        memo: String,
        time6F: String,
        time5F: String,
        time4F: String,
        time3F: String,
        time2F: String,
        time1F: String,
        horseTrainingId: String?
    ): GetHorseTrainingResponse {
        return if(horseTrainingId == null){
            httpClient.post {
                url("$baseUrl/trainings")
                parameter("horse_id",horseId)
                parameter("date",date)
                parameter("training_type_id",1) // todo remove this in api
                parameter("weather",weather)
                parameter("training_type",trainingType)
                parameter("training_detail", trainingDetail)
                parameter("memo",memo)
                parameter("6f_time",if(time6F.isNotBlank()) time6F else "0")
                parameter("5f_time",if(time5F.isNotBlank()) time5F else "0")
                parameter("4f_time",if(time4F.isNotBlank()) time4F else "0")
                parameter("3f_time",if(time3F.isNotBlank()) time3F else "0")
                parameter("2f_time",if(time2F.isNotBlank()) time2F else "0")
                parameter("1f_time",if(time1F.isNotBlank()) time1F else "0")
            }
        }else{
            httpClient.put {
                url("$baseUrl/trainings/$horseTrainingId")
                parameter("horse_id",horseId)
                parameter("date",date)
                parameter("training_type_id",1) // todo remove this in api
                parameter("weather",weather)
                parameter("training_type",trainingType)
                parameter("training_detail", trainingDetail)
                parameter("memo",memo)
                parameter("6f_time",if(time6F.isNotBlank()) time6F else "0")
                parameter("5f_time",if(time5F.isNotBlank()) time5F else "0")
                parameter("4f_time",if(time4F.isNotBlank()) time4F else "0")
                parameter("3f_time",if(time3F.isNotBlank()) time3F else "0")
                parameter("2f_time",if(time2F.isNotBlank()) time2F else "0")
                parameter("1f_time",if(time1F.isNotBlank()) time1F else "0")
            }
        }

    }

    override suspend fun removeHorseTraining(horseTrainingId: String): BooleanResponse {
        return httpClient.delete{
            url("$baseUrl/trainings/$horseTrainingId")
        }
    }

    override suspend fun addHorseTreatmentRecord(
        horseId: String,
        date: String,
        vetName: String,
        treatmentDetail: String,
        injuredPart: String,
        occasionType: String,
        medicineName: String,
        memo: String,
        dailyAttachedIds: List<String>?,
        horseTreatmentId: String?
    ): GetHorseTreatmentResponse {
        return if(horseTreatmentId == null) {
            httpClient.post {
                url("$baseUrl/treatment")
                parameter("horse_id", horseId)
                parameter("date", date)
                parameter("injured_part", injuredPart)
                parameter("treatment_detail", treatmentDetail)
                parameter("occasion_type",occasionType)
                parameter("doctor_name", vetName)
                parameter("medicine_name", medicineName)
                parameter("memo", memo)
                if(dailyAttachedIds != null){
                    for(id: String in dailyAttachedIds){
                        parameter("attached_file_ids[]", id)
                    }
                }
            }
        }else{
            httpClient.put {
                url("$baseUrl/treatment/$horseTreatmentId")
                parameter("horse_id", horseId)
                parameter("date", date)
                parameter("doctor_name", vetName)
                parameter("treatment_detail", treatmentDetail)
                parameter("injured_part", injuredPart)
                parameter("occasion_type",occasionType)
                parameter("medicine_name", medicineName)
                parameter("memo", memo)
                if(dailyAttachedIds != null){
                    for(id: String in dailyAttachedIds){
                        parameter("attached_file_ids[]", id)
                    }
                }
            }
        }
    }

    override suspend fun removeHorseTreatment(horseTreatmentId: String): BooleanResponse {
        return httpClient.delete{
            url("$baseUrl/treatment/$horseTreatmentId")
        }
    }


    override suspend fun getHorseTrainingTypes(): DropDownDataResponse {
        return httpClient.get {
            url("$baseUrl/training-types")
        }
    }

    override suspend fun getHorseTrainingDetails(): DropDownDataResponse {
        return httpClient.get {
            url("$baseUrl/training-details")
        }
    }

}