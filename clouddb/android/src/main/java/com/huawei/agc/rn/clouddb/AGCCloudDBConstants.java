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

package com.huawei.agc.rn.clouddb;

import com.huawei.agconnect.cloud.database.AGConnectCloudDB;
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;

import java.util.HashMap;
import java.util.Map;

public class AGCCloudDBConstants {
    private static final Map<String, Object> CONSTANTS = new HashMap<>();

    static {
        CONSTANTS.put("CloudDBZoneSyncProperty", getCloudDBZoneSyncProperty());
        CONSTANTS.put("CloudDBZoneAccessProperty", getCloudDBZoneAccessProperty());
        CONSTANTS.put("CloudDBZoneQueryPolicy", getCloudDBZoneQueryPolicy());
        CONSTANTS.put("CloudDBEventType", getEventType());
    }

    public static Map<String, Object> getConstants() {
        return CONSTANTS;
    }

    private static Map<String, Object> getCloudDBZoneSyncProperty() {
        Map<String, Object> map = new HashMap<>();
        map.put("CLOUDDBZONE_CLOUD_CACHE", CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE.ordinal());
        map.put("CLOUDDBZONE_LOCAL_ONLY", CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_LOCAL_ONLY.ordinal());
        return map;
    }

    private static Map<String, Object> getCloudDBZoneAccessProperty() {
        Map<String, Object> map = new HashMap<>();
        map.put("CLOUDDBZONE_PUBLIC", CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC.ordinal());
        return map;
    }

    private static Map<String, Object> getCloudDBZoneQueryPolicy() {
        Map<String, Object> map = new HashMap<>();
        map.put("POLICY_QUERY_DEFAULT", CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT.ordinal());
        map.put("POLICY_QUERY_FROM_CLOUD_ONLY",
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY.ordinal());
        map.put("POLICY_QUERY_FROM_LOCAL_ONLY",
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_LOCAL_ONLY.ordinal());
        return map;
    }

    private static Map<String, Object> getEventType() {
        Map<String, Object> map = new HashMap<>();
        map.put("USER_KEY_CHANGED", AGConnectCloudDB.EventType.USER_KEY_CHANGED.ordinal());
        return map;
    }
}