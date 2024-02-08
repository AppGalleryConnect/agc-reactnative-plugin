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

@objc(StorageTaskImpl)

class StorageTaskImpl :  RCTEventEmitter, AGCStorageHanding{
    
    private let UPLOAD_TASK: String = "UploadTask"
    private let DOWNLOAD_TASK: String  = "DownloadTask"
    private let errorMsg = "Not found task instance."
    private let errorMsg2 = "Not found parameter value."
    
    var events: [String] = []
    
    @objc func addOnCanceledListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    uploadTask.onCancel {
                        self.sendEvent(withName:eventKey, body: nil)
                    }
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    downloadTask.onCancel {
                        self.sendEvent(withName: eventKey, body: nil)
                    }
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
    
    @objc func addOnCompleteListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let taskUploadResultId = ConcurrentHashMap<String, AGCStorageTask<AGCStorageUploadResult>>.getUUID(set: AGCCloudStorageMaps.TASK_UPLOAD_RESULT_MAP)
                    uploadTask.onComplete(callback: { taskUploadResult in
                        self.sendEvent(withName:eventKey, body: ["taskUploadResultId":taskUploadResultId])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let taskDownloadResultId = ConcurrentHashMap<String, AGCStorageTask<AGCStorageDownloadResult>>.getUUID(set: AGCCloudStorageMaps.TASK_DOWNLOAD_RESULT_MAP)
                    downloadTask.onComplete(callback: { taskDownloadResult in
                        self.sendEvent(withName:eventKey, body: ["taskDownloadResultId":taskDownloadResultId])
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
    }
    
    @objc func addOnFailureListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    uploadTask.onFailure(callback: { error in
                        let err = error as NSError
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(err))
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    downloadTask.onFailure(callback: { error in
                        let err = error as NSError
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(err))
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
        
    }
    
    @objc func addOnSuccessListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    uploadTask.onSuccess(callback: { storageUploadResult in
                        let bytesTransferred = storageUploadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageUploadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"uploadResultId":"\(uploadResultId)"])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    downloadTask.onSuccess(callback: { storageDownloadResult in
                        let bytesTransferred = storageDownloadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageDownloadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"downloadResultId":"\(downloadResultId)"])
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
    }
    
    @objc func addOnPausedListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    uploadTask.onPaused(callback: { storageUploadResult in
                        let bytesTransferred = storageUploadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageUploadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"uploadResultId":"\(uploadResultId)"])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    downloadTask.onPaused(callback: { storageDownloadResult in
                        let bytesTransferred = storageDownloadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageDownloadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"downloadResultId":"\(downloadResultId)"])
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
    }
    
    @objc func addOnProgressListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    uploadTask.onProgress(callback: { storageUploadResult in
                        let bytesTransferred = storageUploadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageUploadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"uploadResultId":"\(uploadResultId)"])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    downloadTask.onProgress(callback: { storageDownloadResult in
                        let bytesTransferred = storageDownloadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageDownloadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"downloadResultId":"\(downloadResultId)"])
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
    }
    
    
    @objc func cancel(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    uploadTask.cancel();
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    downloadTask.cancel()
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
    
    @objc func isCanceled(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.isCanceled)
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.isCanceled)
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
    
    @objc func isSuccessful(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.isSuccessful)
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.isSuccessful)
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
    
    @objc func isComplete(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.isComplete)
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.isComplete)
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
    
    @objc func isInProgress(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.isInProgress)
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.isInProgress)
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
    
    @objc func isPaused(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.isPaused)
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.isPaused)
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
    
    @objc func pause(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.pause())
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.pause())
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
    
    @objc func resume(_ className: String,storageTaskId:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,uploadTask.resume())
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    self.handle(resolve: resolve,downloadTask.resume())
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
    
    @objc func removeOnCanceledListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    uploadTask.removeOnCancel {
                        self.sendEvent(withName:eventKey, body: nil)
                    }
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    downloadTask.removeOnCancel {
                        self.sendEvent(withName:eventKey, body: nil)
                    }
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
    
    @objc func removeOnCompleteListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let taskUploadResultId = ConcurrentHashMap<String, AGCStorageTask<AGCStorageUploadResult>>.getUUID(set: AGCCloudStorageMaps.TASK_UPLOAD_RESULT_MAP)
                    uploadTask.removeOnComplete(callback: { taskUploadResult in
                        self.sendEvent(withName:eventKey, body: ["taskUploadResultId":taskUploadResultId])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let taskDownloadResultId = ConcurrentHashMap<String, AGCStorageTask<AGCStorageDownloadResult>>.getUUID(set: AGCCloudStorageMaps.TASK_DOWNLOAD_RESULT_MAP)
                    downloadTask.removeOnComplete(callback: { taskDownloadResult in
                        self.sendEvent(withName:eventKey, body: ["taskDownloadResultId":taskDownloadResultId])
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
    }
    
    @objc func removeOnFailureListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    uploadTask.removeOnFailure(callback: { error in
                        let err = error as NSError
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(err))
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    downloadTask.removeOnFailure(callback: { error in
                        let err = error as NSError
                        self.sendEvent(withName:eventKey, body: AGCCloudStorageMaps.getErrorMessage(err))
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
    }
    
    @objc func removeOnSuccessListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    uploadTask.removeOnSuccess(callback: { storageUploadResult in
                        let bytesTransferred = storageUploadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageUploadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"uploadResultId":"\(uploadResultId)"])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    downloadTask.removeOnSuccess(callback: { storageDownloadResult in
                        let bytesTransferred = storageDownloadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageDownloadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"downloadResultId":"\(downloadResultId)"])
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
    }
    
    @objc func removeOnPausedListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    uploadTask.removeOnPaused(callback: { storageUploadResult in
                        let bytesTransferred = storageUploadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageUploadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"uploadResultId":"\(uploadResultId)"])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    downloadTask.removeOnPaused(callback: { storageDownloadResult in
                        let bytesTransferred = storageDownloadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageDownloadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"downloadResultId":"\(downloadResultId)"])
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
    }
    
    @objc func removeOnProgressListener(_ className: String,storageTaskId:String,eventKey:String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                switch(className) {
                case self.UPLOAD_TASK:
                    guard let uploadTask = AGCCloudStorageMaps.UPLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let uploadResultId = ConcurrentHashMap<String, AGCStorageUploadResult>.getUUID(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP)
                    uploadTask.removeOnProgress(callback: { storageUploadResult in
                        let bytesTransferred = storageUploadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageUploadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"uploadResultId":"\(uploadResultId)"])
                    })
                    break
                case self.DOWNLOAD_TASK:
                    guard let downloadTask = AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[storageTaskId] else {
                        self.fail(with: self.errorMsg, reject: reject)
                        return
                    }
                    let downloadResultId = ConcurrentHashMap<String, AGCStorageDownloadResult>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP)
                    downloadTask.removeOnProgress(callback: { storageDownloadResult in
                        let bytesTransferred = storageDownloadResult?.bytesTransferred ?? -1
                        let totalByteCount = storageDownloadResult?.totalByteCount ?? -1
                        self.sendEvent(withName:eventKey, body: ["bytesTransferred":bytesTransferred,"totalByteCount":totalByteCount,"downloadResultId":"\(downloadResultId)"])
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
    
    @objc func removeSupportedEvent(_ eventKey: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock){
        do {
            try ObjC.catchException {
                if let index = self.events.firstIndex(of: eventKey) {
                    self.events.remove(at: index)
                }
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
