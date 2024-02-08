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

@objc(AGCCloudStorageMaps) class AGCCloudStorageMaps : NSObject  {
    
    static var STORAGE_MANAGEMENT_MAP = ConcurrentHashMap<String, AGCStorage>()
    static  var STORAGE_REFERENCE_MAP = ConcurrentHashMap<String, AGCStorageReference>()
    static  var FILE_METADATA_MAP = ConcurrentHashMap<String, AGCStorageMetadata>()
    static  var LIST_RESULT_MAP = ConcurrentHashMap<String, AGCStorageListResult>()
    
    static  var UPLOAD_RESULT_MAP = ConcurrentHashMap<String, AGCStorageUploadResult>()
    static  var DOWNLOAD_RESULT_MAP = ConcurrentHashMap<String, AGCStorageDownloadResult>()
    
    static  var UPLOAD_TASK_MAP = ConcurrentHashMap<String, AGCStorageUploadTask>()
    static  var DOWNLOAD_TASK_MAP = ConcurrentHashMap<String, AGCStorageDownloadTask>()
    
    static  var TASK_LIST_RESULT_MAP = ConcurrentHashMap<String, Task<AGCStorageListResult>>()
    static  var TASK_FILE_METADATA_MAP = ConcurrentHashMap<String, Task<AGCStorageMetadata>>()
    static  var TASK_DOWNLOAD_RESULT_MAP = ConcurrentHashMap<String, AGCStorageTask<AGCStorageDownloadResult>>()
    static  var TASK_UPLOAD_RESULT_MAP = ConcurrentHashMap<String, AGCStorageTask<AGCStorageUploadResult>>()
    static  var TASK_BYTE_MAP = ConcurrentHashMap<String, Task<NSData>>()
    static  var TASK_URI_MAP = ConcurrentHashMap<String, Task<NSURL>>()
    static  var TASK_DELETE_MAP = ConcurrentHashMap<String, Task<AnyObject>>()
    
    
    static func getErrorMessage(_ error: NSError) -> [String: Any] {
           var errorDict = Dictionary<String, Any>()
           errorDict["code"] = error.code
           errorDict["message"] = error.userInfo["NSLocalizedDescription"] ?? error.domain
           return errorDict
       }
}
