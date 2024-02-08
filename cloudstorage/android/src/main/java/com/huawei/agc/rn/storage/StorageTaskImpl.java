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

import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.DOWNLOAD_TASK_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STREAM_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.STREAM_DOWNLOAD_TASK_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_STREAM_DOWNLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.TASK_UPLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.UPLOAD_RESULT_MAP;
import static com.huawei.agc.rn.storage.AGCStorageManagementImpl.UPLOAD_TASK_MAP;
import static com.huawei.agc.rn.storage.utils.StorageUtils.getUUID;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.huawei.agc.rn.storage.utils.StorageUtils;
import com.huawei.agconnect.cloud.storage.core.DownloadTask;
import com.huawei.agconnect.cloud.storage.core.StreamDownloadTask;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StorageTaskImpl extends ReactContextBaseJavaModule {
    private final String UPLOAD_TASK = "UploadTask";
    private final String DOWNLOAD_TASK = "DownloadTask";
    private final String STREAM_DOWNLOAD_TASK = "StreamDownloadTask";

    private static String TAG = StorageTaskImpl.class.getSimpleName();


    private ReactApplicationContext reactContext;

    public StorageTaskImpl(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    private void sendEventWithWm(String event, @Nullable WritableMap wm) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, wm);
    }

    private void sendEvent(String event, String className, String id) {
        WritableMap wm = Arguments.createMap();
        wm.putString(className, id);
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(event, wm);
    }

    @NonNull
    @Override
    public String getName() {
        return "StorageTask";
    }

    @ReactMethod
    public void addOnCanceledListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                UPLOAD_TASK_MAP.get(taskId).addOnCanceledListener(() -> {
                    sendEventWithWm(eventKey, null);
                });
                break;
            case DOWNLOAD_TASK:
                DOWNLOAD_TASK_MAP.get(taskId).addOnCanceledListener(() -> {
                    sendEventWithWm(eventKey, null);
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).addOnCanceledListener(() -> {
                    sendEventWithWm(eventKey, null);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("addOnCanceledListener");
    }

    @ReactMethod
    public void addOnCompleteListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String taskUploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).addOnCompleteListener((Task<UploadTask.UploadResult> task) -> {
                    sendEvent(eventKey, "taskUploadResultId", taskUploadResultId);

                });
                break;
            case DOWNLOAD_TASK:
                String taskDownloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).addOnCompleteListener((Task<DownloadTask.DownloadResult> task) -> {
                    TASK_DOWNLOAD_RESULT_MAP.put(taskDownloadResultId, task);
                    sendEvent(eventKey, "taskDownloadResultId", taskDownloadResultId);
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String taskStreamDownloadResultId = getUUID(TASK_STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).addOnCompleteListener((Task<StreamDownloadTask.StreamDownloadResult> task) -> {
                    TASK_STREAM_DOWNLOAD_RESULT_MAP.put(taskStreamDownloadResultId, task);
                    sendEvent(eventKey, "taskStreamDownloadResultId", taskStreamDownloadResultId);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("addOnCompleteListener");
    }

    @ReactMethod
    public void addOnFailureListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                UPLOAD_TASK_MAP.get(taskId).addOnFailureListener(e -> {
                    sendEventWithWm(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case DOWNLOAD_TASK:
                DOWNLOAD_TASK_MAP.get(taskId).addOnFailureListener(e -> {
                    sendEventWithWm(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).addOnFailureListener(e -> {
                    sendEventWithWm(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("addOnFailureListener");
    }

    @ReactMethod
    public void addOnSuccessListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).addOnSuccessListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEventWithWm(eventKey, StorageUtils.uploadResultToWm(uploadResult, uploadResultId));
                });
                break;
            case DOWNLOAD_TASK:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).addOnSuccessListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEventWithWm(eventKey, StorageUtils.downloadResultToWm(downloadResult, downloadResultId));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).addOnSuccessListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEventWithWm(eventKey, StorageUtils.streamDownloadResultToWm(streamDownloadResult, streamDownloadResultId));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("addOnSuccessListener");
    }

    @ReactMethod
    public void addOnPausedListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).addOnPausedListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEventWithWm(eventKey, StorageUtils.uploadResultToWm(uploadResult, uploadResultId));
                });
                break;
            case DOWNLOAD_TASK:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).addOnPausedListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEventWithWm(eventKey, StorageUtils.downloadResultToWm(downloadResult, downloadResultId));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).addOnPausedListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEventWithWm(eventKey, StorageUtils.streamDownloadResultToWm(streamDownloadResult, streamDownloadResultId));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("addOnPausedListener");
    }

    @ReactMethod
    public void addOnProgressListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).addOnProgressListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEventWithWm(eventKey, StorageUtils.uploadResultToWm(uploadResult, uploadResultId));

                });
                break;
            case DOWNLOAD_TASK:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).addOnProgressListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEventWithWm(eventKey, StorageUtils.downloadResultToWm(downloadResult, downloadResultId));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).addOnProgressListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEventWithWm(eventKey, StorageUtils.streamDownloadResultToWm(streamDownloadResult, streamDownloadResultId));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("addOnProgressListener");
    }

    @ReactMethod
    public void cancel(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).cancel());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).cancel());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).cancel());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }
    }

    @ReactMethod
    public void isCanceled(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).isCanceled());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).isCanceled());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).isCanceled());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void isComplete(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).isComplete());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).isComplete());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).isComplete());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void isSuccessful(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).isSuccessful());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).isSuccessful());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).isSuccessful());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void isInProgress(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).isInProgress());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).isInProgress());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).isInProgress());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void isPaused(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).isPaused());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).isPaused());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).isPaused());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void pause(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).pause());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).pause());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).pause());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void resume(String taskName, String taskId, Promise promise) {
        switch (taskName) {
            case UPLOAD_TASK:
                promise.resolve(UPLOAD_TASK_MAP.get(taskId).resume());
                break;
            case DOWNLOAD_TASK:
                promise.resolve(DOWNLOAD_TASK_MAP.get(taskId).resume());
                break;
            case STREAM_DOWNLOAD_TASK:
                promise.resolve(STREAM_DOWNLOAD_TASK_MAP.get(taskId).resume());
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
        }

    }

    @ReactMethod
    public void removeOnCanceledListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                UPLOAD_TASK_MAP.get(taskId).removeOnCanceledListener(() -> {
                    sendEventWithWm(eventKey, null);

                });
                break;
            case DOWNLOAD_TASK:
                DOWNLOAD_TASK_MAP.get(taskId).removeOnCanceledListener(() -> {
                    sendEventWithWm(eventKey, null);
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).removeOnCanceledListener(() -> {
                    sendEventWithWm(eventKey, null);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("removeOnCanceledListener");
    }


    @ReactMethod
    public void removeOnCompleteListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String taskUploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).removeOnCompleteListener((Task<UploadTask.UploadResult> task) -> {
                    TASK_UPLOAD_RESULT_MAP.put(taskUploadResultId, task);
                    sendEvent(eventKey, "taskUploadResultId", taskUploadResultId);
                });
                break;
            case DOWNLOAD_TASK:
                String taskDownloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).removeOnCompleteListener((Task<DownloadTask.DownloadResult> task) -> {
                    TASK_DOWNLOAD_RESULT_MAP.put(taskDownloadResultId, task);
                    sendEvent(eventKey, "taskDownloadResultId", taskDownloadResultId);
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String taskStreamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).removeOnCompleteListener((Task<StreamDownloadTask.StreamDownloadResult> task) -> {
                    TASK_STREAM_DOWNLOAD_RESULT_MAP.put(taskStreamDownloadResultId, task);
                    sendEvent(eventKey, "taskStreamDownloadResultId", taskStreamDownloadResultId);
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("removeOnCompleteListener");
    }

    @ReactMethod
    public void removeOnFailureListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                UPLOAD_TASK_MAP.get(taskId).removeOnFailureListener(e -> {
                    sendEventWithWm(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case DOWNLOAD_TASK:
                DOWNLOAD_TASK_MAP.get(taskId).removeOnFailureListener(e -> {
                    sendEventWithWm(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).removeOnFailureListener(e -> {
                    sendEventWithWm(eventKey, StorageUtils.errorHandlerWithoutPromise(e));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("removeOnFailureListener");
    }

    @ReactMethod
    public void removeOnSuccessListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).removeOnSuccessListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEventWithWm(eventKey, StorageUtils.uploadResultToWm(uploadResult, uploadResultId));
                });
                break;
            case DOWNLOAD_TASK:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).removeOnSuccessListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEventWithWm(eventKey, StorageUtils.downloadResultToWm(downloadResult, downloadResultId));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).removeOnSuccessListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEventWithWm(eventKey, StorageUtils.streamDownloadResultToWm(streamDownloadResult, streamDownloadResultId));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("removeOnSuccessListener");
    }

    @ReactMethod
    public void removeOnPausedListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).removeOnPausedListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEventWithWm(eventKey, StorageUtils.uploadResultToWm(uploadResult, uploadResultId));
                });
                break;
            case DOWNLOAD_TASK:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).removeOnPausedListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEventWithWm(eventKey, StorageUtils.downloadResultToWm(downloadResult, downloadResultId));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).removeOnPausedListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEventWithWm(eventKey, StorageUtils.streamDownloadResultToWm(streamDownloadResult, streamDownloadResultId));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("removeOnPausedListener");
    }

    @ReactMethod
    public void removeOnProgressListener(String taskName, String taskId, String eventKey, Promise promise) {
        if (!isExistTask(taskName, taskId, promise)) {
            return;
        }
        switch (taskName) {
            case UPLOAD_TASK:
                String uploadResultId = getUUID(UPLOAD_RESULT_MAP.keySet());
                UPLOAD_TASK_MAP.get(taskId).removeOnProgressListener((UploadTask.UploadResult uploadResult) -> {
                    UPLOAD_RESULT_MAP.put(uploadResultId, uploadResult);
                    sendEventWithWm(eventKey, StorageUtils.uploadResultToWm(uploadResult, uploadResultId));
                });
                break;
            case DOWNLOAD_TASK:
                String downloadResultId = getUUID(DOWNLOAD_RESULT_MAP.keySet());
                DOWNLOAD_TASK_MAP.get(taskId).removeOnProgressListener((DownloadTask.DownloadResult downloadResult) -> {
                    DOWNLOAD_RESULT_MAP.put(downloadResultId, downloadResult);
                    sendEventWithWm(eventKey, StorageUtils.downloadResultToWm(downloadResult, downloadResultId));
                });
                break;
            case STREAM_DOWNLOAD_TASK:
                String streamDownloadResultId = getUUID(STREAM_DOWNLOAD_RESULT_MAP.keySet());
                STREAM_DOWNLOAD_TASK_MAP.get(taskId).removeOnProgressListener((StreamDownloadTask.StreamDownloadResult streamDownloadResult) -> {
                    STREAM_DOWNLOAD_RESULT_MAP.put(streamDownloadResultId, streamDownloadResult);
                    sendEventWithWm(eventKey, StorageUtils.streamDownloadResultToWm(streamDownloadResult, streamDownloadResultId));
                });
                break;
            default:
                StorageUtils.errorHandler(new Exception("Not found task : " + taskName), promise);
                return;
        }
        promise.resolve("removeOnProgressListener");
    }

    private boolean isExistTask(String taskName, String taskId, Promise promise) {
        boolean isExist = false;
        switch (taskName) {
            case UPLOAD_TASK:
                isExist = UPLOAD_TASK_MAP.containsKey(taskId);
                break;
            case DOWNLOAD_TASK:
                isExist = DOWNLOAD_TASK_MAP.containsKey(taskId);
                break;
            case STREAM_DOWNLOAD_TASK:
                isExist = STREAM_DOWNLOAD_TASK_MAP.containsKey(taskId);
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

