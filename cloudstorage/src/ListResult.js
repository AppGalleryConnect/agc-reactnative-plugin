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

const listResultModule = NativeModules.ListResult;

import StorageReference from './StorageReference';
import AGCStorageException from './AGCStorageException';

export default class ListResult {
    constructor(listResultId = "", error = undefined) {
        this.listResultId = listResultId;
        if (error !== undefined) {
            this.error = error;
            return;
        }
    }

    getListResultId() {
        return this.listResultId;
    }
    getError() {
        return this.error;
    }
    getFileList() {
        return listResultModule.getFileList(this.listResultId)
            .then((data) => {
                let storageReferenceIdArray = data["storageReferenceIdArray"];
                let storageManagementId = data["storageManagementId"];
                let storageReferenceArray = [];
                for (let i = 0; i < storageReferenceIdArray.length; i++) {
                    storageReferenceArray[i] = new StorageReference(storageReferenceIdArray[i], storageManagementId);
                }
                return storageReferenceArray;
            })
            .catch((error) => {
                throw new AGCStorageException(error);
            });
    }
    getDirList() {
        return listResultModule.getDirList(this.listResultId).then((data) => {
            let storageReferenceIdArray = data["storageReferenceIdArray"];
            let storageManagementId = data["storageManagementId"];
            let storageReferenceArray = [];
            for (let i = 0; i < storageReferenceIdArray.length; i++) {
                storageReferenceArray[i] = new StorageReference(storageReferenceIdArray[i], storageManagementId);
            }
            return storageReferenceArray;
        }).catch((error) => {
            throw new AGCStorageException(error);
        });
    }
    getPageMarker() {
        return listResultModule.getPageMarker(this.listResultId);
    }
}