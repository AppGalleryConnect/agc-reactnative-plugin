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

import { NativeModules } from "react-native";
import AGCStorageManagement from "./AGCStorageManagement";
import AGCStorageException from './AGCStorageException';
const AGCStorageModule = NativeModules.AGCStorageModule;

const AGCRoutePolicy = {
  UNKNOWN: 0,
  CHINA: 1,
  GERMANY: 2,
  RUSSIA: 3,
  SINGAPORE: 4,
};

const AGCReferenceName = {
  STORAGE_MANAGEMENT_MAP: "AGCStorageManagement",
  STORAGE_REFERENCE_MAP: 'StorageReference',
  FILE_METADATA_MAP: "FILE_METADATA_MAP",
  LIST_RESULT_MAP: "LIST_RESULT_MAP",
  UPLOAD_RESULT_MAP: "UPLOAD_RESULT_MAP",
  DOWNLOAD_RESULT_MAP: "DOWNLOAD_RESULT_MAP",
  UPLOAD_TASK_MAP: "UPLOAD_TASK_MAP",
  DOWNLOAD_TASK_MAP: "DOWNLOAD_TASK_MAP",
  DELETE_TASK_MAP: "DELETE_TASK_MAP",
  TASK_LIST_RESULT_MAP: "TASK_LIST_RESULT_MAP",
  TASK_FILE_METADATA_MAP: "TASK_FILE_METADATA_MAP",
  TASK_DOWNLOAD_RESULT_MAP: "TASK_DOWNLOAD_RESULT_MAP",
  TASK_UPLOAD_RESULT_MAP: "TASK_UPLOAD_RESULT_MAP",
  TASK_BYTE_MAP: "TASK_BYTE_MAP",
  TASK_URI_MAP: "TASK_URI_MAP",
};

class AGCStorage {
  constructor() {
    this.AGCRoutePolicy = AGCRoutePolicy;
    this.AGCReferenceName = AGCReferenceName;
  }

  static get AGCRoutePolicy() {
    return AGCRoutePolicy;
  }

  static get AGCReferenceName() {
    return AGCReferenceName;
  }

  getInstance(params) {
    return AGCStorageModule.getInstance(params)
      .then((response) => {
        return new AGCStorageManagement(response);
      })
      .catch((error) => {
        console.log(error);
        throw new AGCStorageException(error);
      });
  }
}

export default AGCStorage;
