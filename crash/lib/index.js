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

import {NativeModules, Platform} from 'react-native';

const {RNCrash} = NativeModules;
import tracking from 'promise/setimmediate/rejection-tracking';
import StackTrace from 'stacktrace-js';

export const LogLevel = {
    DEBUG: 2,
    INFO: 3,
    WARN: 4,
    ERROR: 5
}

export function once(fn, context) {
    let result;
    let flag = false;
    return function onceInner(...args) {
        if (!flag) {
            flag = true;
            result = fn.apply(context || this, args);
        }
        return result;
    };
}

export const setErrorHandler = once(() => {
    const originalHandler = ErrorUtils.getGlobalHandler();

    async function handler(error, fatal) {
        if (__DEV__) {
            return originalHandler(error, fatal);
        }
        try {
            const stackFrames = await StackTrace.fromError(error, {offline: true});
            await RNCrash.recordError(parseJsError(error, stackFrames, false));
            await RNCrash.recordFatalError(parseJsError(error, stackFrames, false));
        } catch (e) {
        }
        return originalHandler(error, fatal);
    }

    ErrorUtils.setGlobalHandler(handler);
    return handler;
});

export const setUnhandledPromiseHandler = once(() => {
    async function onUnhandled(id, error) {
        if (!__DEV__) {
            try {
                const stackFrames = await StackTrace.fromError(error, {offline: true});
                await RNCrash.recordError(parseJsError(error, stackFrames, true));
                await RNCrash.recordFatalError(parseJsError(error, stackFrames, true));
            } catch (e) {
            }
        }
    }

    tracking.enable({
        allRejections: true,
        onUnhandled,
    });
    return onUnhandled;
});

export function parseFileName(fileName) {
    let result = '<unknown>';
    if (fileName) {
        const len = fileName.indexOf('?');
        if (len < 0) {
            result = fileName;
        } else if (len > 0) {
            result = fileName.substring(0, len);
        }
    }
    return result;
}

export function parseJsError(error, stackFrames, isPromiseReject) {
    const native = {};
    native.message = error.message;
    native.isPromiseReject = isPromiseReject;
    native.frames = [];
    for (let i = 0; i < stackFrames.length; i++) {
        const {columnNumber, lineNumber, fileName, functionName, source} = stackFrames[i];
        var newFileName = parseFileName(fileName);
        native.frames.push({
            source: source,
            line: lineNumber || 0,
            col: columnNumber || 0,
            function: functionName || '<unknown>',
            file: `${newFileName}:${lineNumber || 0}:${columnNumber || 0}`,
        });
    }
    return native;
}

export default class AGCCrash {


    static init() {
        setErrorHandler();
        setUnhandledPromiseHandler();
    }

    /**
     * Creates a crash for debugging.
     * This crash is triggered in the main thread instead of the JavaScript thread.
     * @example
     * AGCCrash.testIt();
     **/
    static testIt() {
        RNCrash.testIt();
    }

    /**
     * Sets whether to enable the crash reporting function. This function is enabled by default for the SDK.
     *
     * @param {boolean} enable Indicates whether to collect and report app crashes. The default value is true.true: yes false: no
     * @example
     * AGCCrash.enableCrashCollection(true);
     */
    static enableCrashCollection(enable) {
        RNCrash.enableCrashCollection(enable);
    }

    /**
     * Sets a custom user ID.
     *
     * @param {string} userId The value of userId must be of the string type, and the size cannot exceed 1 KB. If the size exceeds 1 KB, the excessive part will be truncated.
     *
     * @example
     * AGCCrash.setUserId('id001');
     */
    static setUserId(userId) {
        RNCrash.setUserId(userId);
    }

    /**
     * Sets custom user status.
     * @param {string} key The key must be of the string type, and the size cannot exceed 1 KB. If the size exceeds 1 KB, the excessive part will be truncated.
     * @param {string | number | boolean} value The value can be of the string, boolean, or number type, and the size cannot exceed 1 KB. If the size exceeds 1 KB, the excessive part will be truncated.
     *
     * @example
     * AGCCrash.setCustomKey("key001","value001");
     */
    static setCustomKey(key, value) {
        if (typeof (value) === 'number' || typeof (value) === 'string' || typeof (value) === 'boolean') {
            RNCrash.setCustomKey(key, value.toString());
        }
    }

    /**
     * Records a custom log. The default log level is INFO.
     * @param {string} message Custom log. The value must be of the string type, and the log level is INFO.
     *
     * @example
     * AGCCrash.log("some log message");
     */
    static log(message) {
        RNCrash.log(message);
    }

    /**
     * Records a custom log.
     * @param {LogLevel} logLevel Custom log level. LogLevel.DEBUG: A log of the DEBUG level is recorded.LogLevel.INFO: A log of the INFO level is recorded. LogLevel.WARN: A log of the WARN level is recorded.LogLevel.ERROR: A log of the ERROR level is recorded.
     * @param {string} message Custom log content. The value must be of the string type.
     *
     * @example
     * AGCCrash.logWithLevel(LogLevel.DEBUG, "some log message");
     */
    static logWithLevel(logLevel, message) {
        if (Platform.OS === 'android') {
            logLevel = logLevel + 1;
        }
        RNCrash.logWithLevel(logLevel, message);
    }

    /**
     * Records a non-fatal exception.
     * @param error Exception details.
     *
     * @example
     * AGCCrash.recordError(err);
     */
    static recordError(error) {

        StackTrace.fromError(error, {offline: true}).then(stackFrames => {

            RNCrash.recordError(parseJsError(error, stackFrames, false));
        });
    }

    /**
     * Records a fatal exception.
     * @param error Exception details.
     *
     * @example AGCCrash.recordFatalError(err);
     */
    static recordFatalError(error) {

        StackTrace.fromError(error, {offline: true}).then(stackFrames => {

            RNCrash.recordFatalError(parseJsError(error, stackFrames, false));
        });
    }

}

AGCCrash.init();
