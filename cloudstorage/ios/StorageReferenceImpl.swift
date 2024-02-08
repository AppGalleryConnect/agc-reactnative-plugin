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

@objc(StorageReferenceImpl)

class StorageReferenceImpl:  NSObject, AGCStorageHanding {
    
    private let errorMsg = "Not found FileMetadata instance."
    private let errorMsg2 = "Not found parameter value."
    
    @objc func child(_ storageReferenceId:String, objectPath: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let child = storageReference.child(objectPath)
                let childId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
                AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[childId] = child
                self.handle(resolve: resolve,  childId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getParent(_ storageReferenceId: String,resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let parent = storageReference.parent()
                let parentId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
                AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[parentId] = parent
                self.handle(resolve: resolve,  parentId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getRoot(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let root = storageReference.root()
                let rootId = ConcurrentHashMap<String, AGCStorageReference>.getUUID(set: AGCCloudStorageMaps.STORAGE_REFERENCE_MAP)
                AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[rootId] = root
                self.handle(resolve: resolve,  rootId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getBucket(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let bucketName = storageReference.bucketName
                self.handle(resolve: resolve,  bucketName )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getName(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let name = storageReference.name
                self.handle(resolve: resolve,  name)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getPath(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let path = storageReference.path
                self.handle(resolve: resolve,  path )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getFileMetadata(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let taskFileMetadata = storageReference.getMetadata()
                let taskFileMetadataId = ConcurrentHashMap<String, Task<AGCStorageMetadata>>.getUUID(set: AGCCloudStorageMaps.TASK_FILE_METADATA_MAP)
                AGCCloudStorageMaps.TASK_FILE_METADATA_MAP[taskFileMetadataId] = taskFileMetadata
                self.handle(resolve: resolve,  taskFileMetadataId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func updateFileMetadata(_ storageReferenceId: String, fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let taskFileMetadata = storageReference.update(fileMetadata)
                let taskFileMetadataId = ConcurrentHashMap<String, Task<AGCStorageMetadata>>.getUUID(set: AGCCloudStorageMaps.TASK_FILE_METADATA_MAP)
                AGCCloudStorageMaps.TASK_FILE_METADATA_MAP[taskFileMetadataId] = taskFileMetadata
                self.handle(resolve: resolve,  taskFileMetadataId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func delete(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                
                let taskDeleteFile = storageReference.deleteFile()
                let taskDeleteFileId = ConcurrentHashMap<String, Task<AnyObject>>.getUUID(set: AGCCloudStorageMaps.TASK_DELETE_MAP)
                AGCCloudStorageMaps.TASK_DELETE_MAP[taskDeleteFileId] = taskDeleteFile
                self.handle(resolve: resolve,  taskDeleteFileId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func list(_ storageReferenceId: String, max :Int ,marker : String ,resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                var taskListResult = Task<AGCStorageListResult>.init()
                if("" != marker) {
                    taskListResult = storageReference.list(max, marker: marker)
                } else {
                    taskListResult = storageReference.list(max)
                }
                let taskListResultId = ConcurrentHashMap<String, Task<AGCStorageListResult>>.getUUID(set: AGCCloudStorageMaps.TASK_LIST_RESULT_MAP)
                AGCCloudStorageMaps.TASK_LIST_RESULT_MAP[taskListResultId] = taskListResult
                self.handle(resolve: resolve,  taskListResultId )
                
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func listAll(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let taskListResult = storageReference.listAll()
                let taskListResultId = ConcurrentHashMap<String, Task<AGCStorageListResult>>.getUUID(set: AGCCloudStorageMaps.TASK_LIST_RESULT_MAP)
                AGCCloudStorageMaps.TASK_LIST_RESULT_MAP[taskListResultId] = taskListResult
                self.handle(resolve: resolve,  taskListResultId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func putFile(_ storageReferenceId: String,srcFile : String, fileMetadataId : String, offset : String ,resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                var mUploadTask: AGCStorageUploadTask = AGCStorageUploadTask.init()
                let url = URL(fileURLWithPath: srcFile)
                if("" != srcFile && "" != fileMetadataId && "" != offset) {
                    guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                        return
                    }
                    guard let offsetNumber = Int64(offset) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    guard let task = storageReference.uploadFile(url, metadata: fileMetadata, offset: offsetNumber) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    mUploadTask = task
                }else if("" != fileMetadataId) {
                    guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                        return
                    }
                    guard let task = storageReference.uploadFile(url, metadata: fileMetadata) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    mUploadTask = task
                }else {
                    guard let task = storageReference.uploadFile(url) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    mUploadTask = task
                }
                let uploadTaskId = ConcurrentHashMap<String, AGCStorageUploadTask>.getUUID(set: AGCCloudStorageMaps.UPLOAD_TASK_MAP)
                
                AGCCloudStorageMaps.UPLOAD_TASK_MAP[uploadTaskId] = mUploadTask
                self.handle(resolve: resolve,  uploadTaskId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func putBytes(_ storageReferenceId: String,bytes:NSArray, fileMetadataId : String, offset :String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                var mUploadTask: AGCStorageUploadTask = AGCStorageUploadTask.init()
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg2, reject: reject)
                    return
                }
                guard let dataByteArray = bytes as? [UInt8] else {
                    self.fail(with: self.errorMsg2, reject: reject)
                    return
                }
                let data: Data = Data(dataByteArray)
                if("" != fileMetadataId && "" != offset) {
                    guard let offsetNumber = Int64(offset) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    guard let task = storageReference.uploadData(data, metadata: fileMetadata, offset: offsetNumber) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    mUploadTask = task
                } else {
                    guard let task = storageReference.uploadData(data, metadata: fileMetadata) else {
                        self.fail(with: self.errorMsg2, reject: reject)
                        return
                    }
                    mUploadTask = task
                }
                let uploadTaskId = ConcurrentHashMap<String, AGCStorageUploadTask>.getUUID(set: AGCCloudStorageMaps.UPLOAD_TASK_MAP)
                
                AGCCloudStorageMaps.UPLOAD_TASK_MAP[uploadTaskId] = mUploadTask
                self.handle(resolve: resolve,  uploadTaskId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getFile(_ storageReferenceId: String,destFile :String, destUri:String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                var mDownloadTask: AGCStorageDownloadTask = AGCStorageDownloadTask.init()
                
                if let task = storageReference.download(toFile: URL(fileURLWithPath: destFile)) {
                    mDownloadTask = task
                } else {
                    self.fail(with: self.errorMsg2, reject: reject)
                    return
                }
               
                let downloadTaskId = ConcurrentHashMap<String, AGCStorageDownloadTask>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_TASK_MAP)
                AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[downloadTaskId] = mDownloadTask
                self.handle(resolve: resolve,  downloadTaskId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getBytes(_ storageReferenceId: String,maxDownloadBytes :String,  resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                var mDownloadTask: Task<NSData> = Task<NSData>.init()
                
                guard let maxDownloadBytesNumber = Int64(maxDownloadBytes) else {
                    self.fail(with: self.errorMsg2, reject: reject)
                    return
                }
                if let task = storageReference.downloadData(withMaxSize: maxDownloadBytesNumber) {
                    mDownloadTask = task
                } else {
                    return
                }
                let downloadTaskId = ConcurrentHashMap<String, Task<NSData>>.getUUID(set: AGCCloudStorageMaps.TASK_BYTE_MAP)
                AGCCloudStorageMaps.TASK_BYTE_MAP[downloadTaskId] = mDownloadTask
                self.handle(resolve: resolve,  downloadTaskId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getDownloadUrl(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let downloadUrlTaskId = ConcurrentHashMap<String, Task<NSURL>>.getUUID(set: AGCCloudStorageMaps.TASK_URI_MAP)
                AGCCloudStorageMaps.TASK_URI_MAP[downloadUrlTaskId] = storageReference.getDownloadUrl()
                self.handle(resolve: resolve,  downloadUrlTaskId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getActiveUploadTasks(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let activeUploadTaskArray = storageReference.activeUploadTasks()
                var activeUploadTaskArrayIdList = [String]()
                for activeUploadTask in activeUploadTaskArray {
                    let activeUploadTaskId = ConcurrentHashMap<String, AGCStorageUploadTask>.getUUID(set: AGCCloudStorageMaps.UPLOAD_TASK_MAP)
                    AGCCloudStorageMaps.UPLOAD_TASK_MAP[activeUploadTaskId] = activeUploadTask
                    activeUploadTaskArrayIdList.append(activeUploadTaskId)
                }
                self.handle(resolve: resolve,  activeUploadTaskArrayIdList )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getActiveDownloadTasks(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let activeDownloadTaskArray = storageReference.activeDownloadTasks()
                var activeDownloadTaskArrayIdList = [String]()
                for activeDownloadTask in activeDownloadTaskArray {
                    let activeDownloadTaskId = ConcurrentHashMap<String, AGCStorageDownloadTask>.getUUID(set: AGCCloudStorageMaps.DOWNLOAD_TASK_MAP)
                    AGCCloudStorageMaps.DOWNLOAD_TASK_MAP[activeDownloadTaskId] = activeDownloadTask
                    activeDownloadTaskArrayIdList.append(activeDownloadTaskId)
                }
                self.handle(resolve: resolve,  activeDownloadTaskArrayIdList)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func toString(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let description = storageReference.description
                self.handle(resolve: resolve,  description)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    
    @objc func equals(_ storageReferenceId: String,otherStorageReferenceId :String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                guard let otherStorage = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[otherStorageReferenceId] else {
                    self.fail(with: self.errorMsg2, reject: reject)
                    return
                }
                let res = storageReference.isEqual(to: otherStorage)
                
                self.handle(resolve: resolve,  res)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func hashCode(_ storageReferenceId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
        do {
            try ObjC.catchException {
                guard let storageReference = AGCCloudStorageMaps.STORAGE_REFERENCE_MAP[storageReferenceId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                let hash = storageReference.hash
                self.handle(resolve: resolve,  hash)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}

