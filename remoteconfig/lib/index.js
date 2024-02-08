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

import { NativeModules, Platform } from 'react-native';
const { RNRemoteConfig } = NativeModules;
import {ConfigError} from './ConfigError';

export const SOURCE = {
    STATIC: 0,
    DEFAULT: 1,
    REMOTE: 2
}

export default class AGCRemoteConfig {
    static applyDefault(map) {
        if (map != null && map.size > 0) {
            let data = new Object();
            map.forEach(function (value, key) {
                if (typeof (value) === 'number' || typeof (value) === 'string'
                    || typeof (value) === 'boolean') {
                    data[key] = value.toString();
                }
            });
            RNRemoteConfig.applyDefault(data);
        }
    }

    static applyLastFetched() {
        RNRemoteConfig.applyLastFetched();
    }

    static fetch(intervalSeconds) {  
        if (intervalSeconds == undefined) {
            return new Promise(function(resolve, reject){
                RNRemoteConfig.fetchDefault().then(()=>{
                    resolve();
                })
                .catch ((err)=>{
                    if(err.userInfo != null && err.userInfo.throttleEndTime != null) {
                        reject(new ConfigError(err.code, err.message, err.userInfo.throttleEndTime));
                    } else {
                        reject(new ConfigError(err.code, err.message, 0));
                    }     
                });
            });
        } else {
            return new Promise(function(resolve, reject){
                RNRemoteConfig.fetch(intervalSeconds).then(()=>{
                    resolve();
                })
                .catch ((err)=>{
                    if(err.userInfo != null && err.userInfo.throttleEndTime != null) {
                        reject(new ConfigError(err.code, err.message, err.userInfo.throttleEndTime));
                    } else {
                        reject(new ConfigError(err.code, err.message, 0));
                    }     
                });
            });
        }
    }

    static getValue(key) {
        return RNRemoteConfig.getValue(key);
    }

    static getSource(key) {
        return new Promise(function (resolve) {
            RNRemoteConfig.getSource(key).then((source) => {
                switch (source) {
                    case 0:
                        resolve(SOURCE.STATIC);
                        break;
                    case 1:
                        resolve(SOURCE.DEFAULT);
                        break;
                    case 2:
                        resolve(SOURCE.REMOTE);
                        break;
                    default:
                        resolve(SOURCE.STATIC);
                        break;
                }
            });
        });
    }

    static getMergedAll() {
        return new Promise(function (resolve) {
            RNRemoteConfig.getMergedAll().then((result) => {
                let map = new Map();
                for (var key in result) {
                    map.set(key, result[key]);
                }
                resolve(map);
            })
        });
    }

    static clearAll() {
        RNRemoteConfig.clearAll();
    }

    static setDeveloperMode(isDeveloperMode) {
        if (Platform.OS === 'android') {
            RNRemoteConfig.setDeveloperMode(isDeveloperMode);
        } else{
            console.warn('The setDeveloperMode method only supports Android, please refer to the development guide to set the developer mode on iOS.');
        }
    }

    static getCustomAttributes() {
        return new Promise(function (resolve) {
            RNRemoteConfig.getCustomAttributes().then((result) => {
                let map = new Map();
                for (var key in result) {
                    map.set(key, result[key]);
                }
                resolve(map);
            })
        });
    }

    static setCustomAttributes(customAttributes) {
        if (customAttributes != null && customAttributes.size > 0) {
            let data = new Object();
            customAttributes.forEach(function (value, key) {
                data[key] = value.toString();
            });
            RNRemoteConfig.setCustomAttributes(data);
        }
    }
}

