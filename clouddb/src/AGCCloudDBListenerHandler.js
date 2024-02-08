/*
 * Copyright 2021-2023. Huawei Technologies Co., Ltd. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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
const { AGCCloudDBModule } = NativeModules;
import AGCCloudDBException from '@hw-agconnect/react-native-clouddb/src/AGCCloudDBException';

export default class AGCCloudDBListenerHandler {
    constructor(zoneId, listenerId, eventListener) {
        this.listenerId = listenerId;
        this.zoneId = zoneId;
        this.eventListener = eventListener;
    }

    remove() {
        return AGCCloudDBModule.removeSubscription(this.zoneId, this.listenerId)
            .then(response => {
                this.eventListener.remove();
                return response;
            }).catch(error => {
                throw new AGCCloudDBException(error);
            });
    }
}