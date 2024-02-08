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

package com.huawei.agc.rn.storage;

import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_DELETE_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.FILE_METADATA_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.LIST_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STREAM_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_BYTE_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_FILE_METADATA_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_LIST_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_STREAM_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_UPLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_URI_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.UPLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.huawei.agc.rn.storage.utils.StorageUtils;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;
import com.huawei.agconnect.cloud.storage.core.ListResult;
import com.huawei.agconnect.cloud.storage.core.StreamDownloadTask;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;

public class TaskImpl extends ReactContextBaseJavaModule {
    private final String TASK_FILE_METADATA = "TaskFileMetadata";
    private final String TASK_LIST_RESULT = "TaskListResult";
    private final String TASK_BYTE = "TaskByte";
    private final String TASK_DELETE = "TaskDelete";
    private final String TASK_UPLOAD_RESULT = "TaskUploadResult";
    private final String TASK_DOWNLOAD_RESULT = "TaskDownloadResult";
    private final String TASK_URI = "TaskUri";
    private final String TASK_STREAM_DOWNLOAD_RESULT = "TaskStreamDownloadResult";

    private static String TAG = TaskImpl.class.getSimpleName();

    private ReactApplicationContext reactContext;

    public TaskImpl(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Nonnull
    @Override
    public String getName() {
        return "Task";
    }

    private void sendEvent(String event, String className, String id) {
        WritableMap wm = Arguments.createMap();
        wm.putString(className, id);
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, wm);
    }

    @ReactMethod
    public void isComplete(String taskName, String taskId, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                promise.resolve(TASK_FILE_METADATA_MAP.get(taskId).isComplete());
                break;
            case TASK_LIST_RESULT:
                promise.resolve(TASK_LIST_RESULT_MAP.get(taskId).isComplete());
                break;
            case TASK_BYTE:
                promise.resolve(TASK_BYTE_MAP.get(taskId).isComplete());
                break;
            case TASK_DELETE:
                promise.resolve(TASK_DELETE_MAP.get(taskId).isComplete());
                break;
            case TASK_UPLOAD_RESULT:
                promise.resolve(TASK_UPLOAD_RESULT_MAP.get(taskId).isComplete());
                break;
            case TASK_DOWNLOAD_RESULT:
                promise.resolve(TASK_DOWNLOAD_RESULT_MAP.get(taskId).isComplete());
                break;
            case TASK_URI:
                promise.resolve(TASK_URI_MAP.get(taskId).isComplete());
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                promise.resolve(TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).isComplete());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task : " + taskName), promise);
        }
    }

    @ReactMethod
    public void isSuccessful(String taskName, String taskId, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                promise.resolve(TASK_FILE_METADATA_MAP.get(taskId).isSuccessful());
                break;
            case TASK_LIST_RESULT:
                promise.resolve(TASK_LIST_RESULT_MAP.get(taskId).isSuccessful());
                break;
            case TASK_BYTE:
                promise.resolve(TASK_BYTE_MAP.get(taskId).isSuccessful());
                break;
            case TASK_DELETE:
                promise.resolve(TASK_DELETE_MAP.get(taskId).isSuccessful());
                break;
            case TASK_UPLOAD_RESULT:
                promise.resolve(TASK_UPLOAD_RESULT_MAP.get(taskId).isSuccessful());
                break;
            case TASK_DOWNLOAD_RESULT:
                promise.resolve(TASK_DOWNLOAD_RESULT_MAP.get(taskId).isSuccessful());
                break;
            case TASK_URI:
                promise.resolve(TASK_URI_MAP.get(taskId).isSuccessful());
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                promise.resolve(TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).isSuccessful());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task"), promise);
        }
    }

    @ReactMethod
    public void isCanceled(String taskName, String taskId, final Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                promise.resolve(TASK_FILE_METADATA_MAP.get(taskId).isCanceled());
                break;
            case TASK_LIST_RESULT:
                promise.resolve(TASK_LIST_RESULT_MAP.get(taskId).isCanceled());
                break;
            case TASK_BYTE:
                promise.resolve(TASK_BYTE_MAP.get(taskId).isCanceled());
                break;
            case TASK_DELETE:
                promise.resolve(TASK_DELETE_MAP.get(taskId).isCanceled());
                break;
            case TASK_UPLOAD_RESULT:
                promise.resolve(TASK_UPLOAD_RESULT_MAP.get(taskId).isCanceled());
                break;
            case TASK_DOWNLOAD_RESULT:
                promise.resolve(TASK_DOWNLOAD_RESULT_MAP.get(taskId).isCanceled());
                break;
            case TASK_URI:
                promise.resolve(TASK_URI_MAP.get(taskId).isCanceled());
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                promise.resolve(TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).isCanceled());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task"), promise);
        }
    }

    @ReactMethod
    public void addOnSuccessListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                String fileMetadataId = getUUID(FILE_METADATA_MAP.keySet());
                TASK_FILE_METADATA_MAP.get(taskId).addOnSuccessListener((FileMetadata fileMetadata) -> {
                    FILE_METADATA_MAP.put(fileMetadataId, fileMetadata);
                    sendEvent(eventKey, "fileMetadataId", fileMetadataId);
                });
                break;
            case TASK_LIST_RESULT:
                String listResultId = getUUID(LIST_RESULT_MAP.keySet());
                TASK_LIST_RESULT_MAP.get(taskId).addOnSuccessListener((ListResult listResult) -> {
                    LIST_RESULT_MAP.put(listResultId, listResult);
                    sendEvent(eventKey, "listResultId", listResultId);
                });
                break;
            case TASK_BYTE:
                TASK_BYTE_MAP.get(taskId).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, new String(bytes, Charset.defaultCharset()));
                    }
                });
                break;
            case TASK_DELETE:
                TASK_DELETE_MAP.get(taskId).addOnSuccessListener((Void) -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_UPLOAD_RESULT:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                TASK_UPLOAD_RESULT_MAP.get(taskId).addOnSuccessListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEvent(eventKey, "uploadResultId", uploadResultId);
                });
                break;
            case TASK_DOWNLOAD_RESULT:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                TASK_DOWNLOAD_RESULT_MAP.get(taskId).addOnSuccessListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEvent(eventKey, "downloadResultId", downloadResultId);
                });
                break;
            case TASK_URI:
                TASK_URI_MAP.get(taskId).addOnSuccessListener(uri -> {
                    sendEvent(eventKey, "uri", uri.toString());
                });
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).addOnSuccessListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEvent(eventKey, "streamDownloadResultId", streamDownloadResultId);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task"), promise);
                return;
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void addOnFailureListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                TASK_FILE_METADATA_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_LIST_RESULT:
                TASK_LIST_RESULT_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_BYTE:
                TASK_BYTE_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_DELETE:
                TASK_DELETE_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_UPLOAD_RESULT:
                TASK_UPLOAD_RESULT_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_DOWNLOAD_RESULT:
                TASK_DOWNLOAD_RESULT_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_URI:
                TASK_URI_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).addOnFailureListener(e -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task"), promise);
                return;
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void addOnCompleteListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                String taskFileMetadataId = getUUID(TASK_FILE_METADATA_MAP.keySet());
                TASK_FILE_METADATA_MAP.get(taskId).addOnCompleteListener((Task<FileMetadata> task) -> {
                    TASK_FILE_METADATA_MAP.put(taskFileMetadataId, task);
                    sendEvent(eventKey, "taskFileMetadataId", taskFileMetadataId);
                });
                break;
            case TASK_LIST_RESULT:
                String taskListResultId = getUUID(TASK_LIST_RESULT_MAP.keySet());
                TASK_LIST_RESULT_MAP.get(taskId).addOnCompleteListener((Task<ListResult> task) -> {
                    TASK_LIST_RESULT_MAP.put(taskListResultId, task);
                    sendEvent(eventKey, "taskListResultId", taskListResultId);

                });
                break;
            case TASK_BYTE:
                String taskByteId = getUUID(TASK_LIST_RESULT_MAP.keySet());
                TASK_BYTE_MAP.get(taskId).addOnCompleteListener((Task<byte[]> task) -> {
                    TASK_BYTE_MAP.put(taskByteId, task);
                    sendEvent(eventKey, "taskByteId", taskByteId);

                });
                break;
            case TASK_DELETE:
                TASK_DELETE_MAP.get(taskId).addOnCompleteListener((Void) -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_UPLOAD_RESULT:
                String taskUploadResultId = getUUID(TASK_UPLOAD_RESULT_MAP.keySet());
                TASK_UPLOAD_RESULT_MAP.get(taskId).addOnCompleteListener((Task<UploadTask.UploadResult> task) -> {
                    TASK_UPLOAD_RESULT_MAP.put(taskUploadResultId, task);
                    sendEvent(eventKey, "taskUploadResultId", taskUploadResultId);

                });
                break;
            case TASK_DOWNLOAD_RESULT:
                String taskDownloadResultId = getUUID(TASK_DOWNLOAD_RESULT_MAP.keySet());
                TASK_DOWNLOAD_RESULT_MAP.get(taskId).addOnCompleteListener((Task<DownloadTask.DownloadResult> task) -> {
                    TASK_DOWNLOAD_RESULT_MAP.put(taskDownloadResultId, task);
                    sendEvent(eventKey, "taskDownloadResultId", taskDownloadResultId);

                });
                break;
            case TASK_URI:
                String taskUriId = getUUID(TASK_URI_MAP.keySet());
                TASK_URI_MAP.get(taskId).addOnCompleteListener((Task<Uri> task) -> {
                    TASK_URI_MAP.put(taskUriId, task);
                    sendEvent(eventKey, "taskUriId", taskUriId);

                });
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                String taskStreamDownloadResultId = getUUID(TASK_STREAM_DOWNLOAD_RESULT_MAP.keySet());
                TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).addOnCompleteListener((Task<StreamDownloadTask.StreamDownloadResult> task) -> {
                    TASK_STREAM_DOWNLOAD_RESULT_MAP.put(taskStreamDownloadResultId, task);
                    sendEvent(eventKey, "streamDownloadResultId", taskStreamDownloadResultId);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task"), promise);
                return;
        }
        promise.resolve(null);
    }

    @ReactMethod
    public void addOnCanceledListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case TASK_FILE_METADATA:
                TASK_FILE_METADATA_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_LIST_RESULT:
                TASK_LIST_RESULT_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_BYTE:
                TASK_BYTE_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_DELETE:
                TASK_DELETE_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_UPLOAD_RESULT:
                TASK_UPLOAD_RESULT_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_DOWNLOAD_RESULT:
                TASK_DOWNLOAD_RESULT_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_URI:
                TASK_URI_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                TASK_STREAM_DOWNLOAD_RESULT_MAP.get(taskId).addOnCanceledListener(() -> {
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventKey, null);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not Found Task"), promise);
                return;
        }
        promise.resolve(null);
    }

    private boolean isExistTask(String taskName, String taskId, Promise promise) {
        boolean isExist = false;
        switch (taskName) {
            case TASK_FILE_METADATA:
                isExist = TASK_FILE_METADATA_MAP.containsKey(taskId);
                break;
            case TASK_LIST_RESULT:
                isExist = TASK_LIST_RESULT_MAP.containsKey(taskId);
                break;
            case TASK_BYTE:
                isExist = TASK_BYTE_MAP.containsKey(taskId);
                break;
            case TASK_DELETE:
                isExist = TASK_DELETE_MAP.containsKey(taskId);
                break;
            case TASK_UPLOAD_RESULT:
                isExist = TASK_UPLOAD_RESULT_MAP.containsKey(taskId);
                break;
            case TASK_DOWNLOAD_RESULT:
                isExist = TASK_DOWNLOAD_RESULT_MAP.containsKey(taskId);
                break;
            case TASK_URI:
                isExist = TASK_URI_MAP.containsKey(taskId);
                break;
            case TASK_STREAM_DOWNLOAD_RESULT:
                isExist = TASK_STREAM_DOWNLOAD_RESULT_MAP.containsKey(taskId);
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                break;
        }
        return isExist;
    }

    @ReactMethod
    public void addListener(String eventName) {
    }

    @ReactMethod
    public void removeListeners(Integer count) {
    }
}
