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

import AGCStorage from "./AGCStorage";
import AGCStorageManagement from "./AGCStorageManagement";
import StorageReference from "./StorageReference";
import ListResult from './ListResult';
import FileMetadata from './FileMetadata';
import TaskFileMetadata from './TaskFileMetadata';
import TaskListResult from './TaskListResult';
import UploadResult from './UploadResult';
import DownloadResult from './DownloadResult';
import DownloadTask from './DownloadTask';
import TaskUploadResult from './TaskUploadResult';
import TaskDownloadResult from './TaskDownloadResult';
import TaskDelete from './TaskDelete';
import UploadTask from './UploadTask';
import StorageResult from './StorageResult';
import StorageTask from './StorageTask';
import Task from './Task';
import TaskByte from './TaskByte';
import TaskUri from './TaskUri';
import TaskStreamDownloadResult from './TaskStreamDownloadResult';
import StreamDownloadTask from './StreamDownloadTask';
import StreamDownloadResult from './StreamDownloadResult';

export { ListResult, AGCStorageManagement, StorageReference, FileMetadata, TaskFileMetadata, TaskListResult, UploadResult, DownloadResult, TaskUploadResult, TaskDownloadResult, TaskDelete, DownloadTask, StorageResult, StorageTask, Task, TaskByte, TaskUri, UploadTask, TaskStreamDownloadResult, StreamDownloadTask, StreamDownloadResult };

export default new AGCStorage();

