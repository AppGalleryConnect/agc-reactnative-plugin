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

package com.huawei.agc.rn.clouddb.utils;

import com.huawei.agconnect.cloud.database.CloudDBZone;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;

public class CloudDBTaskParameters {
    private final CloudDBZone zone;

    private final CloudDBZoneQuery<?> query;

    private final CloudDBZoneQuery.CloudDBZoneQueryPolicy policy;

    public CloudDBTaskParameters(CloudDBZone zone, CloudDBZoneQuery<?> query,
        CloudDBZoneQuery.CloudDBZoneQueryPolicy policy) {
        this.zone = zone;
        this.query = query;
        this.policy = policy;
    }

    public CloudDBZone getZone() {
        return zone;
    }

    public CloudDBZoneQuery.CloudDBZoneQueryPolicy getPolicy() {
        return policy;
    }

    public CloudDBZoneQuery<?> getQuery() {
        return query;
    }
}
