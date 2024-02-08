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
import { NativeEventEmitter, NativeModules, Platform } from 'react-native';
import { AGCStorageEvent } from "./AGCStorageEvent";

const storeTaskModule = NativeModules.StorageTask;

export default class StorageTask {
    constructor(className, storageTaskId) {
        this.className = className;
        this.storageTaskId = storageTaskId;
        this.listenerMap = new Map();
    }

    getClassName() {
        return this.className;
    }
    getStorageTaskId() {
        return this.storageTaskId;
    }
    addOnCanceledListener(result) {
        let eventEmitter = new NativeEventEmitter(storeTaskModule);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_CANCELED_LISTENER).concat(this.storageTaskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey);
            const subscription = eventEmitter.addListener(eventKey, result);
            this.listenerMap.set(eventKey, subscription);
            return storeTaskModule.addOnCanceledListener(this.className, this.storageTaskId, eventKey);
        }
    }
    addOnCompleteListener(result) {
        let eventEmitter = new NativeEventEmitter(storeTaskModule);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_COMPLETE_LISTENER).concat(this.storageTaskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey);
            const subscription = eventEmitter.addListener(eventKey, result);
            this.listenerMap.set(eventKey, subscription);
            return storeTaskModule.addOnCompleteListener(this.className, this.storageTaskId, eventKey);
        }
    }
    addOnFailureListener(result) {
        let eventEmitter = new NativeEventEmitter(storeTaskModule);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_FAILURE_LISTENER).concat(this.storageTaskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey);
            const subscription = eventEmitter.addListener(eventKey, result);
            this.listenerMap.set(eventKey, subscription);
            return storeTaskModule.addOnFailureListener(this.className, this.storageTaskId, eventKey);
        }
    }
    addOnSuccessListener(result) {
        let eventEmitter = new NativeEventEmitter(storeTaskModule);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_SUCCESS_LISTENER).concat(this.storageTaskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey);
            const subscription = eventEmitter.addListener(eventKey, result);
            this.listenerMap.set(eventKey, subscription);
            return storeTaskModule.addOnSuccessListener(this.className, this.storageTaskId, eventKey);
        }
    }
    addOnPausedListener(result) {
        let eventEmitter = new NativeEventEmitter(storeTaskModule);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_PAUSED_LISTENER).concat(this.storageTaskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey);
            const subscription = eventEmitter.addListener(eventKey, result);
            this.listenerMap.set(eventKey, subscription);
            return storeTaskModule.addOnPausedListener(this.className, this.storageTaskId, eventKey);
        }
    }
    addOnProgressListener(result) {
        let eventEmitter = new NativeEventEmitter(storeTaskModule);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_PROGRESS_LISTENER).concat(this.storageTaskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey);
            const subscription = eventEmitter.addListener(eventKey, result);
            this.listenerMap.set(eventKey, subscription);
            return storeTaskModule.addOnProgressListener(this.className, this.storageTaskId, eventKey);
        }
    }
    cancel() {
        return storeTaskModule.cancel(this.className, this.storageTaskId);
    }
    isCanceled() {
        return storeTaskModule.isCanceled(this.className, this.storageTaskId);
    }
    isComplete() {
        return storeTaskModule.isComplete(this.className, this.storageTaskId);
    }
    isSuccessful() {
        return storeTaskModule.isSuccessful(this.className, this.storageTaskId);
    }
    isInProgress() {
        return storeTaskModule.isInProgress(this.className, this.storageTaskId);
    }
    isPaused() {
        return storeTaskModule.isPaused(this.className, this.storageTaskId);
    }
    pause() {
        return storeTaskModule.pause(this.className, this.storageTaskId);
    }
    resume() {
        return storeTaskModule.resume(this.className, this.storageTaskId);
    }
    removeOnCanceledListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let deletedEventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_CANCELED_LISTENER).concat(this.storageTaskId);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.REMOVE_ON_CANCELED_LISTENER).concat(this.storageTaskId);
        this.removeListener(deletedEventKey);
        eventEmitter.addListener(eventKey, result);
        removeSupportedEvent(eventKey);
        return storeTaskModule.removeOnCanceledListener(this.className, this.storageTaskId, eventKey);
    }
    removeOnCompleteListener() {
        let eventEmitter = new NativeEventEmitter(module);
        let deletedEventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_COMPLETE_LISTENER).concat(this.storageTaskId);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.REMOVE_ON_COMPLETE_LISTENER).concat(this.storageTaskId);
        this.removeListener(deletedEventKey);
        eventEmitter.addListener(eventKey, result);
        removeSupportedEvent(eventKey);
        return storeTaskModule.removeOnCompleteListener(this.className, this.storageTaskId, eventKey);
    }
    removeOnFailureListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let deletedEventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_FAILURE_LISTENER).concat(this.storageTaskId);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.REMOVE_ON_FAILURE_LISTENER).concat(this.storageTaskId);
        this.removeListener(deletedEventKey);
        eventEmitter.addListener(eventKey, result);
        removeSupportedEvent(eventKey);
        return storeTaskModule.removeOnFailureListener(this.className, this.storageTaskId, eventKey);

    }
    removeOnSuccessListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let deletedEventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_SUCCESS_LISTENER).concat(this.storageTaskId);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.REMOVE_ON_SUCCESS_LISTENER).concat(this.storageTaskId);
        this.removeListener(deletedEventKey);
        eventEmitter.addListener(eventKey, result);
        removeSupportedEvent(eventKey);
        return storeTaskModule.removeOnSuccessListener(this.className, this.storageTaskId, eventKey);
    }
    removeOnPausedListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let deletedEventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_PAUSED_LISTENER).concat(this.storageTaskId);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.REMOVE_ON_PAUSED_LISTENER).concat(this.storageTaskId);
        this.removeListener(deletedEventKey);
        eventEmitter.addListener(eventKey, result);
        removeSupportedEvent(eventKey);
        return storeTaskModule.removeOnPausedListener(this.className, this.storageTaskId, eventKey);
    }
    removeOnProgressListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let deletedEventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.ADD_ON_PROGRESS_LISTENER).concat(this.storageTaskId);
        let eventKey = "storageTask_".concat(this.className).concat(AGCStorageEvent.REMOVE_ON_PROGRESS_LISTENER).concat(this.storageTaskId);
        this.removeListener(deletedEventKey);
        eventEmitter.addListener(eventKey, result);
        removeSupportedEvent(eventKey);
        return storeTaskModule.removeOnProgressListener(this.className, this.storageTaskId, eventKey);
    }

    addSupportedEvent(eventKey) {
        if (Platform.OS == "ios") {
            storeTaskModule.addSupportedEvent(eventKey);
        }
    }

    removeSupportedEvent(eventKey) {
        if (Platform.OS == "ios") {
            storeTaskModule.removeSupportedEvent(eventKey);
        }
    }

    removeListener(deletedEventKey) {
        const subscription = this.listenerMap.get(deletedEventKey);
        if (subscription) {
            this.listenerMap.delete(deletedEventKey);
            subscription.remove();
        }
    }
}
