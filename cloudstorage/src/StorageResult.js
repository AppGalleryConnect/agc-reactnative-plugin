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
const module = NativeModules.StorageResult;

export default class StorageResult {
    constructor(className, storageResultId, bytesTransferred, totalByteCount) {
        this.className = className;
        this.storageResultId = storageResultId;
        this.bytesTransferred = bytesTransferred;
        this.totalByteCount = totalByteCount;
    }
    getClassName() {
        return this.className;
    }
    getStorageResultId() {
        return this.storageResultId;
    }
    getBytesTransferred() {
        let value = Number(this.bytesTransferred);
        return value;
    }
    getTotalByteCount() {
        let value = Number(this.totalByteCount);
        return value;
    }
}