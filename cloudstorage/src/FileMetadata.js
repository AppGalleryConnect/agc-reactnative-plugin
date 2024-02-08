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
const module = NativeModules.FileMetadata;
const isIOS = Platform.OS === 'ios';

import StorageReference from './StorageReference';

export default class FileMetadata {
    constructor(fileMetadataId = "", error = undefined) {
        this.fileMetadataId = fileMetadataId;
        if (error !== undefined) {
            this.error = error;
            return;
        }
    }

    static async create() {
        let fileMetadataId = await module.newFileMetadata();
        return new FileMetadata(fileMetadataId);
    }

    getFileMetadataId() {
        return this.fileMetadataId;
    }
    getError() {
        return this.error;
    }
    getBucket() {
        return module.getBucket(this.fileMetadataId);
    }
    getCTime() {
        return module.getCTime(this.fileMetadataId);
    }
    getMTime() {
        return module.getMTime(this.fileMetadataId);
    }
    getName() {
        return module.getName(this.fileMetadataId);
    }
    getPath() {
        return module.getPath(this.fileMetadataId);
    }
    getSize() {
        return module.getSize(this.fileMetadataId);
    }
    getSHA256Hash() {
        return module.getSHA256Hash(this.fileMetadataId);
    }
    getContentType() {
        return module.getContentType(this.fileMetadataId);
    }
    getContentEncoding() {
        if (isIOS) {
            console.log("This function is not available in iOS platforms.");
            return;
        }
        return module.getContentEncoding(this.fileMetadataId);
    }
    getCacheControl() {
        return module.getCacheControl(this.fileMetadataId);
    }
    getContentDisposition() {
        return module.getContentDisposition(this.fileMetadataId);
    }
    getContentLanguage() {
        return module.getContentLanguage(this.fileMetadataId);
    }
    getCustomMetadata() {
        return module.getCustomMetadata(this.fileMetadataId);
    }
    setSHA256Hash(sha256) {
        return module.setSHA256Hash(this.fileMetadataId, sha256);
    }
    setContentType(contentType) {
        return module.setContentType(this.fileMetadataId, contentType);
    }
    setCacheControl(cacheControl) {
        return module.setCacheControl(this.fileMetadataId, cacheControl);
    }
    setContentDisposition(contentDisposition) {
        return module.setContentDisposition(this.fileMetadataId, contentDisposition);
    }
    setContentEncoding(contentEncoding) {
        return module.setContentEncoding(this.fileMetadataId, contentEncoding);
    }
    setContentLanguage(contentLanguage) {
        return module.setContentLanguage(this.fileMetadataId, contentLanguage);
    }
    setCustomMetadata(customMetadata) {
        return module.setCustomMetadata(this.fileMetadataId, customMetadata);
    }
    getStorageReference() {
        return module.getStorageReference()
            .then(data => {
                return new StorageReference(data.storageReferenceId, data.storageManagementId);
            });
    }
}