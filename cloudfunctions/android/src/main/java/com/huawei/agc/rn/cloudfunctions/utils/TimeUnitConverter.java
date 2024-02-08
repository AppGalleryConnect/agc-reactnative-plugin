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

package com.huawei.agc.rn.cloudfunctions.utils;

import java.util.concurrent.TimeUnit;

public class TimeUnitConverter {
    /**
     * Converts int value to TimeUnit Object.
     *
     * @param timeUnitInterval: int value to be converted.
     * @return TimeUnit Object.
     */
    public static TimeUnit getTimeUnit(int timeUnitInterval) {
        switch (timeUnitInterval) {
            case 0:
                return TimeUnit.NANOSECONDS;
            case 1:
                return TimeUnit.MICROSECONDS;
            case 2:
                return TimeUnit.MILLISECONDS;
            case 3:
                return TimeUnit.SECONDS;
            case 4:
                return TimeUnit.MINUTES;
            case 5:
                return TimeUnit.HOURS;
            case 6:
                return TimeUnit.DAYS;
            default:
                return TimeUnit.MILLISECONDS;
        }
    }
}
