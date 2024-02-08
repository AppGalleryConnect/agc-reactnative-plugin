/*
 * Copyright (c) 2020-2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import Foundation
import AGConnectStorage

@objc(TaskImpl)

class TaskImpl :  RCTEventEmitter, AGCStorageHanding{
    
    private let TASK_FILE_METADATA = "TaskFileMetadata";
    private let TASK_LIST_RESULT = "TaskListResult";
    private let TASK_BYTE = "TaskByte";
    private let TASK_DELETE = "TaskDelete";
    private let TASK_UPLOAD_RESULT = "TaskUploadResult";
    private let TASK_DOWNLOAD_RESULT = "TaskDownloadResult";
    private let TASK_URI = "TaskUri";
    private let errorMsg = "Not found task instance."
    private let errorMsg2 = "Not found parameter value."
    
    private var taskFileMetadata = Task<AGCStorageMetadata>.init()
    private var taskListResult = Task<AGCStorageListResult>.init()
    private var taskByte = Task<NSData>.init()
    private var taskDelete = Task<AnyObject>.init()
    private var taskUploadResult = AGCStorageTask<AGCStorageUploadResult>.init()
    private var taskDownloadResult = AGCStorageTask<AGCStorageDownloadResult>.init()
    private var taskUri = Task<NSURL>.init()
    
    var events: [String] = []
    
    @objc func addOnSuccessListener(_ className: String,taskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
                
                switch(className) {
                case self.TASK_FILE_METADATA:
                    let fileMetadataId = ConcurrentHashMap<String, AGCStorageMetadata>.getUUID(set: AGCCloudStorageMaps.FILE_METADATA_MAP)
                    self.taskFileMetadata.onSuccess(callback: { metadata in
                        AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] = metadata
                        self.sendEvent(withName:eventKey, body: ["fileMetadataId":fileMetadataId])
                    })
                    break
                case self.TASK_LIST_RESULT:
                    let listResultId = ConcurrentHashMap<String, AGCStorageListResult>.getUUID(set: AGCCloudStorageMaps.LIST_RESULT_MAP)
                    self.taskListResult.onSuccess(callback: { listResult in
                        AGCCloudStorageMaps.LIST_RESULT_MAP[listResultId] = listResult
                        self.sendEvent(withName:eventKey, body: ["listResultId":listResultId])
                    })
                    break
                case self.TASK_BYTE:
                    self.taskByte.onSuccess(callback: { data in
                        guard let data = data else {
                            return
                        }
                        let byteArray = [UInt8](data)
                        self.sendEvent(withName:eventKey, body: byteArray)
                    })
                    break
                case self.TASK_DELETE:
                    self.taskDelete.onSuccess(callback: { _ in
                        self.sendEvent(withName:eventKey, body: nil)
                    })
                    break
                case self.TASK_UPLOAD_RESULT:
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    self.taskUploadResult.onSuccess(callback: { uploadResult in
                        AGCCloudStorageMaps.UPLOAD_RESULT_MAP[uploadResultId] = uploadResult
                        self.sendEvent(withName:eventKey, body: ["uploadResultId",uploadResultId])
                    })
                    break
                case self.TASK_DOWNLOAD_RESULT:
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    self.taskDownloadResult.onSuccess(callback: { downloadResult in
                        AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP[downloadResultId] = downloadResult
                        self.sendEvent(withName:eventKey, body: ["downloadResultId",downloadResultId])
                    })
                    break
                case self.TASK_URI:
                    self.taskUri.onSuccess(callback: { nsUrl in
                        guard let mUrl = nsUrl?.description else {
                            self.fail(with: self.errorMsg2, reject: reject)
                            return
                        }
                        self.sendEvent(withName:eventKey, body: "'\(mUrl)'")
                    })
                    break
                default:
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
        self.handle(resolve: resolve )
    }
    
    @objc func addOnFailureListener(_ className: String,taskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
                
                switch(className) {
                case self.TASK_FILE_METADATA:
                    self.taskFileMetadata.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                case self.TASK_LIST_RESULT:
                    self.taskListResult.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                case self.TASK_BYTE:
                    self.taskListResult.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                case self.TASK_DELETE:
                    self.taskListResult.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                case self.TASK_UPLOAD_RESULT:
                    self.taskListResult.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                case self.TASK_DOWNLOAD_RESULT:
                    self.taskListResult.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                case self.TASK_URI:
                    self.taskListResult.onFailure(callback: { error in
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(error as NSError))
                    })
                    break
                default:
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
        self.handle(resolve: resolve )
    }
    
    @objc func addOnCompleteListener(_ className: String,taskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
                
                switch(className) {
                case self.TASK_FILE_METADATA:
                    let taskFileMetadataId = ConcurrentHashMap<String, Task<AGCStorageMetadata>>.getUUID(set: AGCCloudStorageMaps.TASK_FILE_METADATA_MAP)
                    self.taskFileMetadata.onComplete(callback: { taskMetadata in
                        AGCCloudStorageMaps.TASK_FILE_METADATA_MAP[taskFileMetadataId] = taskMetadata
                        self.sendEvent(withName:eventKey, body: ["taskFileMetadataId":taskFileMetadataId])
                        
                    })
                    break
                case self.TASK_LIST_RESULT:
                    let taskListResultId = ConcurrentHashMap<String, Task<AGCStorageListResult>>.getUUID(set: AGCCloudStorageMaps.TASK_LIST_RESULT_MAP)
                    self.taskListResult.onComplete(callback: { taskListResult in
                        AGCCloudStorageMaps.TASK_LIST_RESULT_MAP[taskListResultId] = taskListResult
                        self.sendEvent(withName:eventKey, body: ["taskListResultId":taskListResultId])
                    })
                    break
                case self.TASK_BYTE:
                    self.taskByte.onSuccess(callback: { data in
                        guard let data = data else {
                            return
                        }
                        let byteArray = [UInt8](data)
                        self.sendEvent(withName:eventKey, body: String(bytes: byteArray, encoding: .utf8))
                          
                    })
                    let taskByteId = ConcurrentHashMap<String, Task<NSData>>.getUUID(set: AGCCloudStorageMaps.TASK_BYTE_MAP)
                    self.taskByte.onComplete(callback: { taskData in
                        AGCCloudStorageMaps.TASK_BYTE_MAP[taskByteId] = taskData
                        self.sendEvent(withName:eventKey, body: ["taskByteId":taskByteId])
                    })
                    break
                case self.TASK_DELETE:
                    self.taskDelete.onComplete(callback: { _ in
                        self.sendEvent(withName:eventKey, body: nil)
                    })
                    break
                case self.TASK_UPLOAD_RESULT:
                    let taskUploadResultId = ConcurrentHashMap<String, AGCStorageTask<AGCStorageUploadResult>>.getUUID(set: AGCCloudStorageMaps.TASK_UPLOAD_RESULT_MAP)
                    self.taskUploadResult.onComplete(callback: { taskUploadResult in
                        AGCCloudStorageMaps.TASK_UPLOAD_RESULT_MAP[taskUploadResultId] = taskUploadResult
                        self.sendEvent(withName:eventKey, body: ["taskUploadResult":taskUploadResult])
                    })
                    break
                case self.TASK_DOWNLOAD_RESULT:
                    let taskDownloadResultId = ConcurrentHashMap<String, AGCStorageTask<AGCStorageDownloadResult>>.getUUID(set: AGCCloudStorageMaps.TASK_DOWNLOAD_RESULT_MAP)
                    self.taskDownloadResult.onComplete(callback: { taskDownloadResult in
                        AGCCloudStorageMaps.TASK_DOWNLOAD_RESULT_MAP[taskDownloadResultId] = taskDownloadResult
                        self.sendEvent(withName:eventKey, body: ["taskDownloadResultId":taskDownloadResultId])
                    })
                    break
                case self.TASK_URI:
                    let taskUriId = ConcurrentHashMap<String, Task<NSURL>>.getUUID(set: AGCCloudStorageMaps.TASK_URI_MAP)
                    self.taskUri.onComplete(callback: { taskNSUrl in
                        AGCCloudStorageMaps.TASK_URI_MAP[taskUriId] = taskNSUrl
                        self.sendEvent(withName:eventKey, body: ["taskUriId":taskUriId])
                    })
                    break
                default:
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
        self.handle(resolve: resolve )
    }
    
    @objc func addOnCanceledListener(_ className: String,taskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
                
                switch(className) {
                case self.TASK_FILE_METADATA:
                    self.taskFileMetadata.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                case self.TASK_LIST_RESULT:
                    self.taskListResult.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                case self.TASK_BYTE:
                    self.taskByte.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                case self.TASK_DELETE:
                    self.taskDelete.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                case self.TASK_UPLOAD_RESULT:
                    self.taskUploadResult.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                case self.TASK_DOWNLOAD_RESULT:
                    self.taskDownloadResult.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                case self.TASK_URI:
                    self.taskUri.onCancel(callback: {
                        self.sendEvent(withName:eventKey, body:nil)
                    })
                    break
                default:
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
        self.handle(resolve: resolve )
    }
    
    @objc func isComplete(_ className :String,taskId :String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
                
                switch(className) {
                case self.TASK_FILE_METADATA:
                    self.handle(resolve: resolve, self.taskFileMetadata.isComplete)
                    break
                case self.TASK_LIST_RESULT:
                    self.handle(resolve: resolve, self.taskListResult.isComplete)
                    break
                case self.TASK_BYTE:
                    self.handle(resolve: resolve, self.taskByte.isComplete)
                    break
                case self.TASK_DELETE:
                    self.handle(resolve: resolve, self.taskDelete.isComplete)
                    break
                case self.TASK_UPLOAD_RESULT:
                    self.handle(resolve: resolve, self.taskUploadResult.isComplete)
                    break
                case self.TASK_DOWNLOAD_RESULT:
                    self.handle(resolve: resolve, self.taskDownloadResult.isComplete)
                    break
                case self.TASK_URI:
                    self.handle(resolve: resolve, self.taskUri.isComplete)
                    break
                default:
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func isSuccessful(_ className: String,taskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
        
        switch(className) {
        case self.TASK_FILE_METADATA:
            self.handle(resolve: resolve, self.taskFileMetadata.isSuccessful)
            break
        case self.TASK_LIST_RESULT:
            self.handle(resolve: resolve, self.taskListResult.isSuccessful)
            break
        case self.TASK_BYTE:
            self.handle(resolve: resolve, self.taskByte.isSuccessful)
            break
        case self.TASK_DELETE:
            self.handle(resolve: resolve, self.taskDelete.isSuccessful)
            break
        case self.TASK_UPLOAD_RESULT:
            self.handle(resolve: resolve, self.taskUploadResult.isSuccessful)
            break
        case self.TASK_DOWNLOAD_RESULT:
            self.handle(resolve: resolve, self.taskDownloadResult.isSuccessful)
            break
        case self.TASK_URI:
            self.handle(resolve: resolve, self.taskUri.isSuccessful)
            break
        default:
            self.fail(with: self.errorMsg, reject: reject)
            return
        }
    }
    
    @objc func isCanceled(_ className: String,taskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        self.setTask(className, storageTaskId: taskId, resolver: resolve, rejecter: reject)
        
        switch(className) {
        case self.TASK_FILE_METADATA:
            self.handle(resolve: resolve, self.taskFileMetadata.isCanceled)
            break
        case self.TASK_LIST_RESULT:
            self.handle(resolve: resolve, self.taskListResult.isCanceled)
            break
        case self.TASK_BYTE:
            self.handle(resolve: resolve, self.taskByte.isCanceled)
            break
        case self.TASK_DELETE:
            self.handle(resolve: resolve, self.taskDelete.isCanceled)
            break
        case self.TASK_UPLOAD_RESULT:
            self.handle(resolve: resolve, self.taskUploadResult.isCanceled)
            break
        case self.TASK_DOWNLOAD_RESULT:
            self.handle(resolve: resolve, self.taskDownloadResult.isCanceled)
            break
        case self.TASK_URI:
            self.handle(resolve: resolve, self.taskUri.isCanceled)
            break
        default:
            self.fail(with: self.errorMsg, reject: reject)
            return
        }
    }
    
    func setTask (_ className :String, storageTaskId :String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock){
        switch(className) {
        case self.TASK_FILE_METADATA:
            guard let taskFileMetadata = AGCCloudStorageMaps.TASK_FILE_METADATA_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskFileMetadata = taskFileMetadata
            break
        case self.TASK_LIST_RESULT:
            guard let taskListResult = AGCCloudStorageMaps.TASK_LIST_RESULT_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskListResult = taskListResult
            break
        case self.TASK_BYTE:
            guard let taskByte = AGCCloudStorageMaps.TASK_BYTE_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskByte = taskByte
            break
        case self.TASK_DELETE:
            guard let taskDelete = AGCCloudStorageMaps.TASK_DELETE_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskDelete = taskDelete
            break
        case self.TASK_UPLOAD_RESULT:
            guard let taskUploadResult = AGCCloudStorageMaps.TASK_UPLOAD_RESULT_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskUploadResult = taskUploadResult
            break
        case self.TASK_DOWNLOAD_RESULT:
            guard let taskDownloadResult = AGCCloudStorageMaps.TASK_DOWNLOAD_RESULT_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskDownloadResult = taskDownloadResult
            break
        case self.TASK_URI:
            guard let taskUri = AGCCloudStorageMaps.TASK_URI_MAP[storageTaskId] else {
                self.fail(with: self.errorMsg, reject: reject)
                return
            }
            self.taskUri = taskUri
            break
        default:
            self.fail(with: self.errorMsg, reject: reject)
            return
        }
    }
    
    @objc func addSupportedEvent(_ eventKey: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                self.events.append(eventKey);
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    
    override func supportedEvents() -> [String]! {
        return self.events
    }
    
    @objc override static func requiresMainQueueSetup() -> Bool {
        return true
    }
}

