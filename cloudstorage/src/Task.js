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
const module = NativeModules.Task;

import { AGCStorageEvent } from "./AGCStorageEvent";

export default class Task {
    constructor(className, taskId) {
        this.className = className;
        this.taskId = taskId;
        this.listenerMap = new Map();
    }
    getClassName() {
        return this.className;
    }
    getTaskId() {
        return this.taskId;
    }
    isComplete() {
        return module.isComplete(this.className, this.taskId);
    }
    isSuccessful() {
        return module.isSuccessful(this.className, this.taskId);
    }
    isCanceled() {
        return module.isCanceled(this.className, this.taskId);
    }
    addOnSuccessListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let eventKey = "task_".concat(AGCStorageEvent.ADD_ON_SUCCESS_LISTENER).concat("_").concat(this.taskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey)
            const subscription = eventEmitter.addListener(eventKey, result)
            this.listenerMap.set(eventKey, subscription);
            return module.addOnSuccessListener(this.className, this.taskId, eventKey);
        }
    }
    addOnFailureListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let eventKey = "task_".concat(AGCStorageEvent.ADD_ON_FAILURE_LISTENER).concat("_").concat(this.taskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey)
            const subscription = eventEmitter.addListener(eventKey, result)
            this.listenerMap.set(eventKey, subscription);
            return module.addOnFailureListener(this.className, this.taskId, eventKey);
        }
    }
    addOnCompleteListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let eventKey = "task_".concat(AGCStorageEvent.ADD_ON_COMPLETE_LISTENER).concat("_").concat(this.taskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey)
            const subscription = eventEmitter.addListener(eventKey, result)
            this.listenerMap.set(eventKey, subscription);
            return module.addOnCompleteListener(this.className, this.taskId, eventKey);
        }
    }
    addOnCanceledListener(result) {
        let eventEmitter = new NativeEventEmitter(module);
        let eventKey = "task_".concat(AGCStorageEvent.ADD_ON_CANCELED_LISTENER).concat("_").concat(this.taskId);
        if (!this.listenerMap.get(eventKey)) {
            this.addSupportedEvent(eventKey)
            const subscription = eventEmitter.addListener(eventKey, result)
            this.listenerMap.set(eventKey, subscription);
            return module.addOnCanceledListener(this.className, this.taskId, eventKey);
        }
    }

    addSupportedEvent(eventKey) {
        if (Platform.OS == "ios") {
            module.addSupportedEvent(eventKey);
        }
    }
}
