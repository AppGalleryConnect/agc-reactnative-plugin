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

@objc(FileMetadataImpl)

class FileMetadataImpl: NSObject, AGCStorageHanding {
    
    private let errorMsg = "Not found FileMetadata instance."
    private let errorMsg2 = "Not found parameter value."
    
    @objc func newFileMetadata(_ resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) {
       
        do {
            try ObjC.catchException {
                let fileMetadata = AGCStorageMetadata.init()
                let fileMetadataId = ConcurrentHashMap<String, AGCStorageMetadata>.getUUID(set: AGCCloudStorageMaps.FILE_METADATA_MAP)
                AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] = fileMetadata
                self.handle(resolve: resolve, fileMetadataId )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getBucket(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let bucketName = fileMetadata.bucketName
                self.handle(resolve: resolve,  bucketName )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getCTime(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let createTime = fileMetadata.createTime
                self.handle(resolve: resolve,  createTime )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getMTime(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let modifyTime = fileMetadata.modifyTime
                self.handle(resolve: resolve,  modifyTime )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getName(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let name = fileMetadata.name
                self.handle(resolve: resolve,  name )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getPath(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let path = fileMetadata.path
                self.handle(resolve: resolve,  path )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getSize(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let size = fileMetadata.size
                self.handle(resolve: resolve,  "\(size)" )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getSHA256Hash(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let sha256Hash = fileMetadata.sha256Hash
                self.handle(resolve: resolve,  sha256Hash )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getContentType(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let contentType = fileMetadata.contentType
                self.handle(resolve: resolve,  contentType )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getCacheControl(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let cacheControl = fileMetadata.cacheControl
                self.handle(resolve: resolve,  cacheControl )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getContentDisposition(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let contentDisposition = fileMetadata.contentDisposition
                self.handle(resolve: resolve,  contentDisposition )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getContentLanguage(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let contentLanguage = fileMetadata.contentLanguage
                self.handle(resolve: resolve,  contentLanguage )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getCustomMetadata(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let customMetadata = fileMetadata.customMetadata
                self.handle(resolve: resolve,  customMetadata )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func getStorageReference(_ fileMetadataId: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                
                let bucketName = fileMetadata.bucketName
                self.handle(resolve: resolve,  bucketName )
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    //setter
    
    @objc func setSHA256Hash(_ fileMetadataId: String, sha256: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.sha256Hash = sha256
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setContentType(_ fileMetadataId: String, contentType: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.contentType = contentType
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setCacheControl(_ fileMetadataId: String, cacheControl: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.cacheControl = cacheControl
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setContentDisposition(_ fileMetadataId: String, contentDisposition: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.contentDisposition = contentDisposition
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setContentEncoding(_ fileMetadataId: String, contentEncoding: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.contentEncoding = contentEncoding
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setContentLanguage(_ fileMetadataId: String, contentLanguage: String, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.contentLanguage = contentLanguage
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc func setCustomMetadata(_ fileMetadataId: String, customMetadata: NSDictionary, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock)  {
        do {
            try ObjC.catchException {
                guard let fileMetadata = AGCCloudStorageMaps.FILE_METADATA_MAP[fileMetadataId] else {
                    self.fail(with: self.errorMsg, reject: reject)
                    return
                }
                                    
                fileMetadata.customMetadata = customMetadata as? [String: String] ?? [:]
                self.handle(resolve: resolve)
            }
        } catch {
            return self.fail(with: error, reject: reject)
        }
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
