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

export default class AGCCloudDBQuery {
    static CloudDBZoneQueryPolicy = {
        POLICY_QUERY_DEFAULT: AGCCloudDBModule.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT,
        POLICY_QUERY_FROM_CLOUD_ONLY: AGCCloudDBModule.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY,
        POLICY_QUERY_FROM_LOCAL_ONLY: AGCCloudDBModule.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_LOCAL_ONLY,
    }

    constructor(className) {
        this.queryMap = {
            className: className,
            queryElements: [],
        }
    }

    static where(className) {
        return new AGCCloudDBQuery(className);
    }

    equalTo(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.equalTo.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    notEqualTo(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.notEqualTo.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    greaterThan(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.greaterThan.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    greaterThanOrEqualTo(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.greaterThanOrEqualTo.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    lessThan(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.lessThan.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    lessThanOrEqualTo(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.lessThanOrEqualTo.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    in(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.in.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    beginsWith(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.beginsWith.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    endsWith(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.endsWith.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    contains(fieldName, value) {
        this.queryMap.queryElements.push({
            operation: this.contains.name,
            fieldName: fieldName,
            value: value,
        })
        return this
    }

    isNull(fieldName) {
        this.queryMap.queryElements.push({
            operation: this.isNull.name,
            fieldName: fieldName,
        })
        return this
    }

    isNotNull(fieldName) {
        this.queryMap.queryElements.push({
            operation: this.isNotNull.name,
            fieldName: fieldName,
        })
        return this
    }

    orderByAsc(fieldName) {
        this.queryMap.queryElements.push({
            operation: this.orderByAsc.name,
            fieldName: fieldName,
        })
        return this
    }

    orderByDesc(fieldName) {
        this.queryMap.queryElements.push({
            operation: this.orderByDesc.name,
            fieldName: fieldName,
        })
        return this
    }

    limit(count, offset) {
        if (offset) {
            this.queryMap.queryElements.push({
                operation: this.limit.name,
                value: count,
                offset: offset
            })
            return this
        } else {
            this.queryMap.queryElements.push({
                operation: this.limit.name,
                value: count,
            })
            return this
        }
    }

    startAt(object) {
        this.queryMap.queryElements.push({
            operation: this.startAt.name,
            value: object
        })
        return this
    }

    startAfter(object) {
        this.queryMap.queryElements.push({
            operation: this.startAfter.name,
            value: object
        })
        return this
    }

    endAt(object) {
        this.queryMap.queryElements.push({
            operation: this.endAt.name,
            value: object
        })
        return this
    }

    endBefore(object) {
        this.queryMap.queryElements.push({
            operation: this.endBefore.name,
            value: object
        })
        return this
    }

    endGroup() {
        if (Platform.OS === "android") {
            this.queryMap.queryElements.push({
                operation: this.endGroup.name
            })
            return this
        } else {
            throw new Error('`endGroup()` is not supported on IOS platform.');
        }
    }

    beginGroup() {
        if (Platform.OS === "android") {
            this.queryMap.queryElements.push({
                operation: this.beginGroup.name
            })
            return this
        } else {
            throw new Error('`beginGroup()` is not supported on IOS platform.');
        }
    }

    and() {
        if (Platform.OS === "android") {
            this.queryMap.queryElements.push({
                operation: this.and.name
            })
            return this
        } else {
            throw new Error('`and()` is not supported on IOS platform.');
        }
    }

    or() {
        if (Platform.OS === "android") {
            this.queryMap.queryElements.push({
                operation: this.or.name
            })
            return this
        } else {
            throw new Error('`or()` is not supported on IOS platform.');
        }
    }

    build() {
        Object.freeze(this);
        return this;
    }
}