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

import FileMetadata from './FileMetadata';
import StorageResult from './StorageResult';

import { NativeModules } from 'react-native';
const storageResultModule = NativeModules.StorageResult;

export default class UploadResult extends StorageResult {
    constructor(uploadResultId = "", error = undefined) {
        super("UploadResult", uploadResultId);
        this.uploadResultId = "";
        if (error !== undefined) {
            this.error = error;
            return;
        }
        this.uploadResultId = uploadResultId;
    }
    getUploadResultId() {
        return this.uploadResultId;
    }
    getError() {
        return this.error;
    }
    getMetadata() {
        return storageResultModule.getMetadata(this.className, this.storageResultId).then((fileMetadataId) => {
            return new FileMetadata(true, fileMetadataId);
        })
    }
}
