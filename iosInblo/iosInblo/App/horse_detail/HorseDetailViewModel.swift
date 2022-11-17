//
//  HorseDetailViewModel.swift
//  iosInblo
//
//  Created by Jhondee Diaz on 11/16/21.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import Alamofire

class HorseDetailViewModel: ObservableObject {
    
    let getHorseDailyReports: GetHorseDailyReports
    let getDailyReportForm: GetDailyReportForm
    let getHorseTreatments: GetHorseTreatments
    let addDailyReport: AddHorseDailyReport
    let addHorseTreatment: AddHorseTreatment
    let deleteHorseDailyReport: DeleteHorseDailyReport
    let deleteHorseTreatment: DeleteHorseTreatment
    let activeUrl: String
    
    @Published var alert: AlertContent?
    @Published var othersAlert: AlertContent?

    

    @Published var dailyReports = [HorseDailyDto]()
//    @Published var attachedFiles = [AttachedFile]()
//    @Published var treatmentAttachedFiles = [AttachedFile]()
    @Published var horseTreatments = [HorseTreatment]()
    @Published var riders = [DropdownData]()
    @Published var trainingTypes = [DropdownData]()
    @Published var hiddenColumns = ""
    
    init(
        horseId: String,
        getDailyReportForm: GetDailyReportForm,
        getHorseDailyReports: GetHorseDailyReports,
        getHorseTreatments: GetHorseTreatments,
        addDailyReport: AddHorseDailyReport,
        addHorseTreatment: AddHorseTreatment,
        deleteHorseDailyReport: DeleteHorseDailyReport,
        deleteHorseTreatment: DeleteHorseTreatment,
        activeUrl: String
        
    ){
        self.getDailyReportForm = getDailyReportForm
        self.getHorseDailyReports = getHorseDailyReports
        self.getHorseTreatments = getHorseTreatments
        self.addDailyReport = addDailyReport
        self.addHorseTreatment = addHorseTreatment
        self.deleteHorseDailyReport = deleteHorseDailyReport
        self.deleteHorseTreatment = deleteHorseTreatment
        self.activeUrl = activeUrl
//        print("inithorsedetail")
    }
    
    
    
    func loadDailyReportForm(stableId: String){
        do {
            getDailyReportForm.invoke(
                stableId: stableId
            ).collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    let dailyReportResponse = dataState?.value(forKey: "data") as! GetDailyReportFormResponse
                    self.riders = dailyReportResponse.data.riders
                    self.trainingTypes = dailyReportResponse.data.trainingTypes
                }
            })
        }
    }
    
    func loadDailyReports(horseId: String){
        do {
            getHorseDailyReports.invoke(
                horseId: horseId
            ).collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    let dailyReportsResponse = dataState?.value(forKey: "data") as! GetHorseDailyListResponse
                    print(dailyReportsResponse)
                    
//                    let meta = dataState?.value(forKey: "meta") as! MetaResponse
//                    print(meta)
                    let dailyReports = dailyReportsResponse.data
                    let hiddenColumnsString = dailyReportsResponse.hiddenColumns
                    self.dailyReports = dailyReports
                    self.hiddenColumns = hiddenColumnsString
                    print(dailyReports)
                    print("hidden cols -> \(hiddenColumnsString)")
                }
            })
        }
    }
    
    func addHorseDailyReport(
        horseId: String,
        date: Date,
        bodyTemperature: String,
        horseWeight: String,
        conditionGroup: String,
        riderId: String? = nil,
        trainingTypeId: String? = nil,
        trainingAmount: String,
        time5f: String,
        time4f: String,
        time3f: String,
        memo: String,
        attachedFiles: [AttachedFile],
        dailyAttachedIds: [String]?,
        dailyReportId: String?
    ){
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        
        let formattedDate = dateFormatterPrint.string(from: date)
        
        let reportId = dailyReportId == "0" ? nil : dailyReportId
        
        
        addDailyReport.invoke(horseId: horseId, date: formattedDate, bodyTemperature: bodyTemperature, horseWeight: horseWeight, conditionGroup: conditionGroup, riderName: "", trainingType: "", riderId: riderId, trainingTypeId: trainingTypeId, trainingAmount: trainingAmount, time5f: time5f, time4f: time4f, time3f: time3f, memo: memo, dailyAttachedIds: dailyAttachedIds, dailyReportId: reportId).collectCommon(coroutineScope: nil, callback: { dataState in
            if(dataState is DataStateData){
                let data = dataState?.value(forKey: "data") as! HorseDaily
                attachedFiles.forEach{ attachedFile in
                    self.attachDailyFile(attachedFile: attachedFile, dailyReportId: String(data.id))
                }
                self.loadDailyReports(horseId: horseId)

                let message: String = {
                    if dailyReportId == nil {
                        return "Daily Report added successfully!"
                    }else{
                        return "Daily Report updated successfully!"
                    }
                }()
                
                self.alert =
                AlertContent(title: "", body: message, type: getAlertTypeByCode(200))
                print(data)
            }
        })
    }
    
    func addTreatment(
        horseId: String,
        date: Date,
        vetName: String,
        treatmentDetail: String,
        injuredPart: String,
        occasionType: String,
        medicineName: String,
        memo: String,
        attachedFiles: [AttachedFile],
        treatmentAttachedIds: [String]?,
        horseTreatmentId: String?
    ) {
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "yyyy-MM-dd"
        
        let formattedDate = dateFormatterPrint.string(from: date)
        
        let treatmentId = horseTreatmentId == "0" ? nil : horseTreatmentId

        
        addHorseTreatment.invoke(horseId: horseId, date: formattedDate, vetName: vetName, treatmentDetail: treatmentDetail, injuredPart: injuredPart, occasionType: occasionType, medicineName: medicineName, memo: memo, dailyAttachedIds: treatmentAttachedIds, horseTreatmentId: treatmentId).collectCommon(coroutineScope: nil, callback: { dataState in
            if(dataState is DataStateData){
//                print("datastate -> \(dataState)")
                let data = dataState?.value(forKey: "data") as! HorseTreatment
                attachedFiles.forEach{ attachedFile in
                    self.attachTreatmentFile(attachedFile: attachedFile, treatmentId: String(data.id))
                }
                self.loadHorseTreatments(horseId: horseId)

                
                let message: String = {
                    if  horseTreatmentId == nil {
                        return "治療を記録しました。"
                    }else{
                        return "治療を修正しました。"
                    }
                }()
                
                self.othersAlert =
                AlertContent(title: "", body: "Horse Treatment added successfully!", type: getAlertTypeByCode(200))
                print(data)
            }
            
            
        })
    }
    
    
    
    func loadHorseTreatments(horseId: String){
        do {
            getHorseTreatments.invoke(
                horseId: horseId
            ).collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
                    let treatments = dataState?.value(forKey: "data") as! [HorseTreatment]
                    self.horseTreatments = treatments
                    print(treatments)
                    treatments.forEach{ treatment in
                        print(treatment.id)
                    }
                    print("treatments found")
                }
            })
        }
    }
    

    func attachDailyFile(attachedFile: AttachedFile, dailyReportId: String) {
        let uiImage = attachedFile.file
        
        if(uiImage != nil){
            let imageData: Data = uiImage!.jpegData(compressionQuality: 0.1) ?? Data()
            let imageStr = imageData.base64EncodedString()

            guard let url: String? = "\(activeUrl)/daily_reports/attachfile/\(dailyReportId)" else{
                print ("invalid url")
                return
            }
 
//            let paramStr: String = "file=\(imageStr)"
//            let paramData: Data = paramStr.data(using: .utf8) ?? Data()
//
//            var urlRequest: URLRequest = URLRequest(url: url)
//            urlRequest.httpMethod = "POST"
//            urlRequest.httpBody = paramData

//            urlRequest.setValue("multipart/form-data", forHTTPHeaderField: "Content-Type")


//                    URLSession.shared.dataTask(with: urlRequest, completionHandler: { (data,response,error) in
//                        guard let data = data else {
//                            print("invalid data")
//                            return
//                        }
//
//                        let responseStr: String = String(data: data, encoding: .utf8) ?? ""
//                        print(responseStr)
//                    })
//                        .resume()


//                    let imageInData = uiImage.JPEGRE
            if(url != nil){
//                let headers: HTTPHeaders = [
//                    "Content-Type": "application/json"
//                ]


                AF.upload(multipartFormData: { multipartFormData in
                    multipartFormData.append(uiImage!.pngData()!,withName: "file",fileName:attachedFile.fileName)
                }, to: url!,
                          method:.post
                )
                    .responseJSON(completionHandler: { response in
                        debugPrint(response)
                })


//                print("image size is \(uiImage.size)")
            }
            
        
        }
    }
    
    func attachTreatmentFile(attachedFile: AttachedFile, treatmentId: String) {
        let uiImage = attachedFile.file
        print("trying to upload \(attachedFile.fileName)")
        
        if(uiImage != nil){
            let imageData: Data = uiImage!.jpegData(compressionQuality: 0.1) ?? Data()
            let imageStr = imageData.base64EncodedString()

            guard let url: String? = "\(activeUrl)/api/treatments/attachfile/\(treatmentId)" else{
                print ("invalid url")
                return
            }

//            let paramStr: String = "file=\(imageStr)"
//            let paramData: Data = paramStr.data(using: .utf8) ?? Data()
//
//            var urlRequest: URLRequest = URLRequest(url: url)
//            urlRequest.httpMethod = "POST"
//            urlRequest.httpBody = paramData

//            urlRequest.setValue("multipart/form-data", forHTTPHeaderField: "Content-Type")


//                    URLSession.shared.dataTask(with: urlRequest, completionHandler: { (data,response,error) in
//                        guard let data = data else {
//                            print("invalid data")
//                            return
//                        }
//
//                        let responseStr: String = String(data: data, encoding: .utf8) ?? ""
//                        print(responseStr)
//                    })
//                        .resume()


//                    let imageInData = uiImage.JPEGRE
            if(url != nil){
//                let headers: HTTPHeaders = [
//                    "Content-Type": "application/json"
//                ]


                AF.upload(multipartFormData: { multipartFormData in
                    multipartFormData.append(uiImage!.pngData()!,withName: "file",fileName:attachedFile.fileName)
                }, to: url!,
                          method:.post
                )
                    .responseJSON(completionHandler: { response in
                        debugPrint(response)
                })


//                print("image size is \(uiImage.size)")
            }
            
        
        }else{
            print("image is nil")
        }
    }
    
    func removeDailyReport(
        dailyReportId: String,
        horseId: String
    ){
        do {
            print("deleting daily report id -> \(dailyReportId)")
            deleteHorseDailyReport.invoke(
                horseDailyId: dailyReportId
            ).collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
//                    print(dataState)
                    let response = dataState?.value(forKey: "data") as! BooleanResponse
                    print(response)
                    
//                    self.horseTreatments = treatments
//                    print(treatments)
//                    treatments.forEach{ treatment in
//                        print(treatment.id)
//                    }
//                    print(response)
                    self.loadDailyReports(horseId: horseId)

                    self.alert =
                    AlertContent(title: "", body: "状態を削除しました。", type: getAlertTypeByCode(200))
                }
            })
        }
    }
    
    func removeTreatment(
        treatmentId: String,
        horseId: String
    ){
        do {
            deleteHorseTreatment.invoke(horseTreatmentId: treatmentId)
                .collectCommon(coroutineScope: nil, callback: { dataState in
                if(dataState is DataStateData){
//                    print(dataState)
                    let response = dataState?.value(forKey: "data") as! BooleanResponse
                    print(response)
                    
//                    self.horseTreatments = treatments
//                    print(treatments)
//                    treatments.forEach{ treatment in
//                        print(treatment.id)
//                    }
//                    print(response)
                    self.loadHorseTreatments(horseId: horseId)

                    self.alert =
                    AlertContent(title: "", body: "治療を削除しました。", type: getAlertTypeByCode(200))
                }
            })
        }
    }
    
        
    
}
