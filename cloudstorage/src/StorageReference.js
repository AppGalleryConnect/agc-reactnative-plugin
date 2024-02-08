


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

import { NativeModules, Platform } from 'react-native';
const module = NativeModules.StorageReferenceModule;
const isIOS = Platform.OS === 'ios';

import UploadTask from './UploadTask';
import DownloadTask from './DownloadTask';
import TaskByte from './TaskByte';
import TaskUri from './TaskUri';
import TaskListResult from './TaskListResult';
import TaskFileMetadata from './TaskFileMetadata';
import TaskDelete from './TaskDelete';
import AGCStorageManagement from './AGCStorageManagement';
import StreamDownloadTask from './StreamDownloadTask';

export default class StorageReference {
    constructor(id, storageManagementId) {
        this.id = id;
        this.storageManagementId = storageManagementId;
    }
    getId() {
        return this.id;
    }
    getStorageManagementId() {
        return this.storageManagementId;
    }
    getStorage() {
        return new AGCStorageManagement(this.storageManagementId);
    }
    child(objectPath) {
        return module.child(this.id, objectPath).then((childId) => {
            return new StorageReference(childId, this.storageManagementId);
        });
    }
    getParent() {
        return module.getParent(this.id).then((parentId) => {
            return new StorageReference(parentId, this.storageManagementId);
        });
    }
    getRoot() {
        return module.getParent(this.id).then((rootId) => {
            return new StorageReference(rootId, this.storageManagementId);
        });
    }
    getBucket() {
        return module.getBucket(this.id);
    }
    getName() {
        return module.getName(this.id);
    }
    getPath() {
        return module.getPath(this.id);
    }
    getFileMetadata() {
        return module.getFileMetadata(this.id).then((taskListResultId) => {
            return new TaskFileMetadata(taskListResultId);
        });
    }
    updateFileMetadata(fileMetadataId) {
        return module.updateFileMetadata(this.id, fileMetadataId).then((taskListResultId) => {
            return new TaskFileMetadata(taskListResultId);
        });
    }
    delete() {
        return module.delete(this.id).then((taskDeleteId) => {
            return new TaskDelete(taskDeleteId);
        });
    }
    list(max = 10, marker = "") {
        return module.list(this.id, max, marker).then((taskListResultId) => {
            return new TaskListResult(taskListResultId);
        });
    }
    listAll() {
        return module.listAll(this.id).then((taskListResultId) => {
            return new TaskListResult(taskListResultId);
        });
    }
    putFile(srcFile, attribute = "", offset = "") {
        return module.putFile(this.id, srcFile, attribute?.fileMetadataId, offset).then((uploadTaskId) => {
            return new UploadTask(uploadTaskId);
        });
    }
    putBytes(bytes, attribute = "", offset = "") {
        return module.putBytes(this.id, bytes, attribute?.fileMetadataId, offset).then((uploadTaskId) => {
            return new UploadTask(uploadTaskId);
        });
    }
    getFile(destFile = "", destUri = "") {
        return module.getFile(this.id, destFile, destUri).then((DownloadTaskId) => {
            return new DownloadTask(DownloadTaskId);
        });
    }

    getStream() {
        if (isIOS) {
            console.log("This function is not available in iOS platforms.");
            return;
        }
        return module.getStream(this.id).then((streamDownloadTaskId) => {
            return new StreamDownloadTask(streamDownloadTaskId);
        });
    }

    getBytes(maxDownloadBytes) {
        return module.getBytes(this.id, maxDownloadBytes).then((taskByteId) => {
            return new TaskByte(taskByteId);
        });
    }
    getDownloadUrl() {
        return module.getDownloadUrl(this.id).then((taskUriId) => {
            return new TaskUri(taskUriId);
        });
    }
    getActiveUploadTasks() {
        return module.getActiveUploadTasks(this.id).then((uploadTaskIdList) => {
            let uploadTaskArray = [];
            uploadTaskIdList.forEach(uploadTaskId => {
                let uploadTask = new UploadTask(uploadTaskId);
                uploadTaskArray.push(uploadTask);
            });
            return uploadTaskArray;
        });
    }
    getActiveDownloadTasks() {
        return module.getActiveDownloadTasks(this.id).then((downloadTaskIdList) => {
            let downloadTaskArray = [];
            downloadTaskIdList.forEach(downloadTaskId => {
                let downloadTask = new DownloadTask(downloadTaskId);
                downloadTaskArray.push(downloadTask);
            });
            return downloadTaskArray;
        });
    }
    compareTo(other) {
        if (isIOS) {
            console.log("This function is not available in iOS platforms.");
            return;
        }
        return module.compareTo(this.id, other.id);
    }
    toString() {
        if (isIOS) {
            console.log("This function is not available in iOS platforms.");
            return;
        }
        return module.toString(this.id);
    }
    equals(other) {
        return module.equals(this.id, other.id);
    }
    hashCode() {
        return module.hashCode(this.id);
    }
}