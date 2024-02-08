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

import { NativeModules } from 'react-native';
const { AGCStorageManagementModule } = NativeModules;

import StorageReference from './StorageReference';
import AGCStorageException from './AGCStorageException';

export default class AGCStorageManagement {
    constructor(id) {
        this.storageManagementId = id;
    }

    getStorageManagementId() {
        return this.id;
    }
    getStorageReference(objectPath = "") {
        return AGCStorageManagementModule.getStorageReference(this.storageManagementId, objectPath).then((storageReferenceId) => {
            return new StorageReference(storageReferenceId, this.storageManagementId);
        }).catch((error) => {
            console.log(error);
            throw new AGCStorageException(error);
        });
    }
    getReferenceFromUrl(params) {
        if (params)
            params.storageManagementId = this.storageManagementId;
        return AGCStorageManagementModule.getReferenceFromUrl(params).then((storageReferenceId) => {
            return new StorageReference(storageReferenceId, this.storageManagementId);
        }).catch((error) => {
            console.log(error);
            throw new AGCStorageException(error);
        });
    }
    getMaxUploadTimeout() {
        return AGCStorageManagementModule.getMaxUploadTimeout(this.storageManagementId);
    }
    getMaxDownloadTimeout() {
        return AGCStorageManagementModule.getMaxDownloadTimeout(this.storageManagementId);
    }
    getMaxRequestTimeout() {
        return AGCStorageManagementModule.getMaxRequestTimeout(this.storageManagementId);
    }
    getRetryTimes() {
        return AGCStorageManagementModule.getRetryTimes(this.storageManagementId);
    }
    getArea() {
        return AGCStorageManagementModule.getArea(this.storageManagementId);
    }
    setMaxUploadTimeout(maxUploadTimeout) {
        return AGCStorageManagementModule.setMaxUploadTimeout(this.storageManagementId, maxUploadTimeout);
    }
    setMaxDownloadTimeout(maxDownloadTimeout) {
        return AGCStorageManagementModule.setMaxDownloadTimeout(this.storageManagementId, maxDownloadTimeout);
    }
    setMaxRequestTimeout(maxRequestTimeout) {
        return AGCStorageManagementModule.setMaxRequestTimeout(this.storageManagementId, maxRequestTimeout);
    }
    setRetryTimes(retryTimes) {
        return AGCStorageManagementModule.setRetryTimes(this.storageManagementId, retryTimes);
    }
    clearReference(referenceName, referenceKey) {
        return AGCStorageManagementModule.clearReference(referenceKey, referenceName);
    }
}