
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

@objc(StorageManagementImpl)

class StorageManagementImpl: NSObject, AGCStorageHanding {
    
    private let errorMsg = "Not found StorageManagement instance."
    private let errorMsg2 = "Not found parameter value."
    
    @objc func getStorageReference(_ storageManagementId: String, objectPath: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        
        var reference: AGCStorageReference
        
        let objectPath = objectPath
        
        guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
            self.fail(with: errorMsg, reject: reject)
            return
        }
        
        if("" != objectPath) {
            reference = storage.reference(withPath :objectPath)
        } else {
            reference = storage.reference()
        }
        
        let storageReferenceId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
        AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] = reference
        self.handle(resolve: resolve, storageReferenceId )
    }
    
    @objc func getReferenceFromUrl(_ map: Dictionary<String, Any>, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        
        if(map.isEmpty){
            self.fail(with: errorMsg, reject: reject)
            return
        }
        
        let storageManagementId = map["storageManagementId"] as? String ?? ""
        
        guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
            self.fail(with: errorMsg, reject: reject)
            return
        }
        
        guard let fullUrl: String = map["fullUrl"] as? String else {
            self.fail(with: errorMsg2, reject: reject)
            return
        }
        
        guard let url = URL(string: fullUrl) else {
            self.fail(with: errorMsg2, reject: reject)
            return
        }
        
        do {
            try ObjC.catchException {
                
                let reference = storage.reference(from: url)
                let storageReferenceId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
                AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] = reference
                self.handle(resolve: resolve, storageReferenceId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getMaxUploadTimeout(_ storageManagementId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let maxUploadTime = (Int(storage.maxUploadTimeout)) * 1000
                self.handle(resolve: resolve,  "\(maxUploadTime)" )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getMaxDownloadTimeout(_ storageManagementId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let maxUploadTime = (Int(storage.maxDownloadTimeout)) * 1000
                self.handle(resolve: resolve,  "\(maxUploadTime)" )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getMaxRequestTimeout(_ storageManagementId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let maxUploadTime = (Int(storage.maxRequestTimeout)) * 1000
                self.handle(resolve: resolve,  "\(maxUploadTime)" )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getRetryTimes(_ storageManagementId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let maxUploadTime = (Int(storage.retryTimes)) * 1000
                self.handle(resolve: resolve,  "\(maxUploadTime)" )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setMaxUploadTimeout(_ storageManagementId: String, maxUploadTimeout: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                storage.maxUploadTimeout = TimeInterval(maxUploadTimeout) ?? storage.maxUploadTimeout
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setMaxDownloadTimeout(_ storageManagementId: String,maxDownloadTimeout: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                storage.maxDownloadTimeout = TimeInterval(maxDownloadTimeout) ?? storage.maxDownloadTimeout
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setMaxRequestTimeout(_ storageManagementId: String, maxRequestTimeout: String,resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                storage.maxRequestTimeout = TimeInterval(maxRequestTimeout) ?? storage.maxRequestTimeout
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setRetryTimes(_ storageManagementId: String,retryTimes: Int, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                
                guard let storage = AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP[storageManagementId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                storage.retryTimes = retryTimes
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func clearReference(_ referenceKey: String,referenceName: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        switch (referenceName) {
        case "STORAGE_MANAGEMENT_MAP":
            if (!ConcurrentHashMap<String, AGCStorage>.isExist(set: AGCCloudStorageMaps.STORAGE_MANAGEMENT_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "STORAGE_REFERENCE_MAP":
            if (!ConcurrentHashMap<String, AGCStorageReference>.isExist(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "FILE_METADATA_MAP":
            if (!ConcurrentHashMap<String, AGCStorageMetadata>.isExist(set: AGCCloudStorageMaps.FILE_METADATA_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "LIST_RESULT_MAP":
            if (!ConcurrentHashMap<String, AGCStorageListResult>.isExist(set: AGCCloudStorageMaps.LIST_RESULT_MAP,key: referenceKey, reject:reject)) {
                return
            }
            break
        case "UPLOAD_RESULT_MAP":
            if (!ConcurrentHashMap<String, AGCStorageUploadResult>.isExist(set: AGCCloudStorageMaps.UPLOAD_RESULT_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "DOWNLOAD_RESULT_MAP":
            if (!ConcurrentHashMap<String, AGCStorageDownloadResult>.isExist(set: AGCCloudStorageMaps.DOWNLOAD_RESULT_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "UPLOAD_TASK_MAP":
            if (!ConcurrentHashMap<String, AGCStorageUploadTask>.isExist(set: AGCCloudStorageMaps.UPLOAD_TASK_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "DOWNLOAD_TASK_MAP":
            if (!ConcurrentHashMap<String, AGCStorageDownloadTask>.isExist(set: AGCCloudStorageMaps.DOWNLOAD_TASK_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_DELETE_MAP":
            if (!ConcurrentHashMap<String, Task<AnyObject>>.isExist(set: AGCCloudStorageMaps.TASK_DELETE_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_LIST_RESULT_MAP":
            if (!ConcurrentHashMap<String, Task<AGCStorageListResult>>.isExist(set: AGCCloudStorageMaps.TASK_LIST_RESULT_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_FILE_METADATA_MAP":
            if (!ConcurrentHashMap<String, Task<AGCStorageMetadata>>.isExist(set: AGCCloudStorageMaps.TASK_FILE_METADATA_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_DOWNLOAD_RESULT_MAP":
            if (!ConcurrentHashMap<String, AGCStorageTask<AGCStorageDownloadResult>>.isExist(set: AGCCloudStorageMaps.TASK_DOWNLOAD_RESULT_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_UPLOAD_RESULT_MAP":
            if (!ConcurrentHashMap<String, AGCStorageTask<AGCStorageUploadResult>>.isExist(set: AGCCloudStorageMaps.TASK_UPLOAD_RESULT_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_BYTE_MAP":
            if (!ConcurrentHashMap<String, Task<NSData>>.isExist(set: AGCCloudStorageMaps.TASK_BYTE_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        case "TASK_URI_MAP":
            if (!ConcurrentHashMap<String, Task<NSURL>>.isExist(set: AGCCloudStorageMaps.TASK_URI_MAP, key: referenceKey, reject:reject)) {
                return
            }
            break
        default:
            self.fail(with: "Not found reference name.", reject: reject)
            return;
        }
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}

