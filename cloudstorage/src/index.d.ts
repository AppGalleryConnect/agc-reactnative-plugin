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


export default class AGCStorage {
    getInstance(params?: StorageManagementParam_1 | StorageManagementParam_2 | StorageManagementParam_3 | StorageManagementParam_4 | StorageManagementParam_5): Promise<AGCStorageManagement>;
    get AGCRoutePolicy(): AGCRoutePolicy;
    get AGCReferenceName(): AGCReferenceName;
}

export class AGCStorageManagement {
    constructor(storageManagementId: string);
    getStorageManagementId(): string;
    getStorageReference(objectPath?: string): Promise<StorageReference>;
    getReferenceFromUrl(params: ReferenceFromUrlParam_1 | ReferenceFromUrlParam_2): Promise<StorageReference>;
    getMaxUploadTimeout(): Promise<string>;
    getMaxDownloadTimeout(): Promise<string>;
    getMaxRequestTimeout(): Promise<string>;
    getRetryTimes(): Promise<number>;
    getArea(): Promise<string>;
    setMaxUploadTimeout(maxUploadTimeout: string): Promise<void>;
    setMaxDownloadTimeout(maxDownloadTimeout: string): Promise<void>;
    setMaxRequestTimeout(maxRequestTimeout: string): Promise<void>;
    setRetryTimes(retryTimes: number): Promise<void>;
    static clearReference(referenceName: RefecenceName, referenceKey: string): Promise<boolean>;
}

export class StorageReference {

    constructor(id: string, storageManagementId: string);
    getId(): string;
    getStorageManagementId(): string;
    getStorage(): Promise<AGCStorageManagement>;
    child(objectPath: string): Promise<StorageReference>;
    getParent(): Promise<StorageReference>;
    getRoot(): Promise<StorageReference>;
    getBucket(): Promise<string>;
    getName(): Promise<string>;
    getPath(): Promise<string>;
    getFileMetadata(): Promise<TaskFileMetadata>;
    updateFileMetadata(fileMetadataId: string): Promise<TaskFileMetadata>;
    delete(): Promise<TaskDelete>;
    list(max: number, marker?: string): Promise<TaskListResult>;
    listAll(): Promise<TaskListResult>;
    putFile(srcFile: string, attribute?: FileMetadata, offset?: string): Promise<UploadTask>;
    putBytes(bytes: [], attribute: FileMetadata, offset?: string): Promise<UploadTask>;
    getFile(destFile?: string, destUri?: string): Promise<DownloadTask>;
    getStream(): Promise<StreamDownloadTask>;
    getBytes(maxDownloadBytes: string): Promise<TaskByte>;
    getDownloadUrl(): Promise<TaskUri>;
    getActiveUploadTasks(): Promise<UploadTask[]>;
    getActiveDownloadTasks(): Promise<DownloadTask[]>;
    compareTo(other: StorageReference): Promise<number>;
    toString(): Promise<string>;
    equals(other: StorageReference): Promise<boolean>;
    hashCode(): Promise<number>;
}

export class FileMetadata {
    constructor(fileMetadataId: string, error?: string | undefined);
    static create(): Promise<FileMetadata>;
    getFileMetadataId(): string;
    getError(): string | undefined;
    getBucket(): Promise<string>;
    getCTime(): Promise<string>;
    getMTime(): Promise<string>;
    getName(): Promise<string>;
    getPath(): Promise<string>;
    getSize(): Promise<string>;
    getSHA256Hash(): Promise<string>;
    getContentType(): Promise<string>;
    getCacheControl(): Promise<string>;
    getContentDisposition(): Promise<string>;
    getContentLanguage(): Promise<string>;
    getContentEncoding(): Promise<string>;
    getCustomMetadata(): Promise<CustomMetadata>;
    setSHA256Hash(sha256: string): Promise<void>;
    setContentType(contentType: string): Promise<void>;
    setCacheControl(cacheControl: string): Promise<void>;
    setContentDisposition(contentDisposition: string): Promise<void>;
    setContentEncoding(contentEncoding: string): Promise<void>;
    setContentLanguage(contentLanguage: string): Promise<void>;
    setCustomMetadata(metadata: CustomMetadata): Promise<void>;
    getStorageReference(): Promise<StorageReference>;
}

export class StorageTask {

    constructor(className: string, storageTaskId: string);
    getClassName(): string;
    getStorageTaskId(): string;
    addOnCanceledListener(callback: () => void): this;
    addOnCompleteListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    addOnFailureListener(callback: (res: StorageException) => void): this;
    addOnSuccessListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    addOnPausedListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    addOnProgressListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    cancel(): Promise<boolean>;
    isCanceled(): Promise<boolean>;
    isComplete(): Promise<boolean>;
    isSuccessful(): Promise<boolean>;
    isInProgress(): Promise<boolean>;
    isPaused(): Promise<boolean>;
    pause(): Promise<boolean>;
    resume(): Promise<boolean>;
    removeOnCanceledListener(callback: () => void): this;
    removeOnCompleteListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    removeOnFailureListener(callback: (res: StorageException) => void): this;
    removeOnSuccessListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    removeOnPausedListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;
    removeOnProgressListener(callback: (res: IntDownloadResult | IntUploadResult | IntStreamDownloadResult) => void): this;

}

export class DownloadTask extends StorageTask {
    constructor(downloadTaskId: string);
    getDownloadTaskId(): string;
}

export class UploadTask extends StorageTask {
    constructor(uploadTaskId: string);
    getUploadTaskId(): string;
}

export class StreamDownloadTask extends StorageTask<StreamDownloadResult, TaskStreamDownloadResult> {
    constructor(streamDownloadTaskId: string);
    getStreamDownloadTaskId(): string;
}

export class TaskDelete extends Task {
    constructor(taskDeleteId: string);
    getTaskDeleteId(): string;
}

export class ListResult {
    constructor(listResultId: string, error?: string | undefined);
    getListResultId(): string;
    getError(): string | undefined;
    getFileList(): Promise<StorageReference[]>;
    getDirList(): Promise<StorageReference[]>;
    getPageMarker(): Promise<string>;
}

export class StorageResult {
    constructor(className: string, storageResultId: string, bytesTransferred: number, totalByteCount: number);
    getClassName(): string;
    getStorageResultId(): string;
    getBytesTransferred(): number;
    getTotalByteCount(): number;
}

export class DownloadResult extends StorageResult {
    constructor(bytesTransferred: number, totalByteCount: number, downloadResultId?: string, error?: StorageException | undefined);
    getDownloadResultId(): string;
    getError(): StorageException | undefined;
}

export class UploadResult extends StorageResult {
    constructor(bytesTransferred: number, totalByteCount: number, uploadResultId?: string, error?: StorageException | undefined);
    getUploadResultId(): string;
    getError(): StorageException | undefined;
    getMetadata(): Promise<FileMetadata>;
}

export class StreamDownloadResult extends StorageResult {
    constructor(bytesTransferred: number, totalByteCount: number, streamDownloadResultId?: string, error?: StorageException | undefined);
    getStreamDownloadResultId(): string;
    getError(): StorageException | undefined;
    getStream(): Promise<[]>;
}

export class Task {
    constructor(className: string, taskId: string);
    getClassName(): string;
    getTaskId(): string;
    isComplete(): Promise<boolean>;
    isSuccessful(): Promise<boolean>;
    isCanceled(): Promise<boolean>;
    addOnSuccessListener(callback: (res: string) => void): this;
    addOnFailureListener(callback: (res: StorageException) => void): this;
    addOnCompleteListener(callback: (res: string) => void): this;
    addOnCanceledListener(callback: () => void): this;
}


export class TaskFileMetadata extends Task {
    constructor(taskFileMetadataId: string);
    getTaskFileMetadataId(): string;
}

export class TaskListResult extends Task<ListResult, TaskListResult> {
    constructor(taskListResultId: string);
    getTaskListResultId(): string;
}


export class TaskDownloadResult extends Task<DownloadResult, TaskDownloadResult> {
    constructor(taskDownloadResultId: string);
    getTaskDownloadResultId(): string;
}

export class TaskUploadResult extends Task<UploadResult, TaskUploadResult> {
    constructor(taskUploadResultId: string);
    getTaskUploadResultId(): string;
}

export class TaskStreamDownloadResult extends Task<StreamDownloadResult, TaskStreamDownloadResult> {
    constructor(taskStreamDownloadResultId: string);
    getTaskStreamDownloadResultId(): string;
}

export class TaskUri extends Task<string, TaskUri> {
    constructor(taskUriId: string);
    getTaskUriId(): string;
}

export class TaskByte extends Task<[], void> {
    constructor(taskByteId: string);
    getTaskByteId(): string;
}

export interface StorageManagementParam_1 {
    bucketName: string;
}
export interface StorageManagementParam_2 {
    routePolicy: AGCRoutePolicy;
    bucketName: string;
}

export interface ReferenceFromUrlParam_1 {
    fullUrl: string;
}
export interface ReferenceFromUrlParam_2 {
    routePolicy: AGCRoutePolicy;
    fullUrl: string;
}

interface GenericObject {
    [key: string]: any;
}
export type CustomMetadata = GenericObject;

export interface StorageException {
    message: string;
    code: number;
}

export interface IntUploadResult {
    bytesTransferred: string;
    totalByteCount: string;
    uploadResultId: string;
}

export interface IntDownloadResult {
    bytesTransferred: string;
    totalByteCount: string;
    downloadResultId: string;
}

export interface IntStreamDownloadResult {
    bytesTransferred: string;
    totalByteCount: string;
    streamDownloadResultId: string;
}

export enum AGCRoutePolicy {
    UNKNOWN = 0,
    CHINA = 1,
    GERMANY = 2,
    RUSSIA = 3,
    SINGAPORE = 4
}
export enum RefecenceName {
    AGCStorageManagement = "STORAGE_MANAGEMENT_MAP",
    StorageReference = "STORAGE_REFERENCE_MAP",
    FileMetadata = "FILE_METADATA_MAP",
    ListResult = "LIST_RESULT_MAP",
    UploadResult = "UPLOAD_RESULT_MAP",
    DownloadResult = "DOWNLOAD_RESULT_MAP",
    StreamDownloadResult = "STREAM_DOWNLOAD_RESULT_MAP",
    UploadTask = "UPLOAD_TASK_MAP",
    DownloadTask = "DOWNLOAD_TASK_MAP",
    StreamDownloadTask = "STREAM_DOWNLOAD_TASK_MAP",
    TaskDelete = "TASK_DELETE_MAP",
    TaskListResult = "TASK_LIST_RESULT_MAP",
    TaskFileMetadata = "TASK_FILE_METADATA_MAP",
    TaskDownloadResult = "TASK_DOWNLOAD_RESULT_MAP",
    TaskStreamDownloadResult = "TASK_STREAM_DOWNLOAD_RESULT_MAP",
    TaskUploadResult = "TASK_UPLOAD_RESULT_MAP",
    TaskByte = "TASK_BYTE_MAP",
    TaskUri = "TASK_URI_MAP"
}

export declare enum AGCStorageEvent {
    ADD_ON_SUCCESS_LISTENER = "ADD_ON_SUCCESS_LISTENER",
    ADD_ON_FAILURE_LISTENER = "ADD_ON_FAILURE_LISTENER",
    ADD_ON_PROGRESS_LISTENE = "ADD_ON_PROGRESS_LISTENE",
    ADD_ON_PAUSED_LISTENE = "ADD_ON_PAUSED_LISTENE",
    ADD_ON_CANCELED_LISTENER = "ADD_ON_CANCELED_LISTENER",
    ADD_ON_COMPLETE_LISTENER = "ADD_ON_COMPLETE_LISTENER",
    REMOVE_ON_SUCCESS_LISTENER = "REMOVE_ON_SUCCESS_LISTENER",
    REMOVE_ON_FAILURE_LISTENER = "REMOVE_ON_FAILURE_LISTENER",
    REMOVE_ON_PROGRESS_LISTENER = "REMOVE_ON_PROGRESS_LISTENER",
    REMOVE_ON_PAUSED_LISTENE = "REMOVE_ON_PAUSED_LISTENE",
    REMOVE_ON_CANCELED_LISTENER = "REMOVE_ON_CANCELED_LISTENER",
    REMOVE_ON_COMPLETE_LISTENER = "REMOVE_ON_COMPLETE_LISTENER",
};
