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

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReactUtils {
    private static final String TAG = ReactUtils.class.getSimpleName();

    /**
     * Converts a JSONObject into a WritableMap.
     *
     * @param json: JSONObject to be converted.
     * @return WritableMap
     */
    public static WritableMap toWM(JSONObject json) {
        WritableMap map = Arguments.createMap();

        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            Object value = null;
            String key = iterator.next();

            try {
                value = json.get(key);
            } catch (JSONException ex) {
                Log.e(TAG, "JSONEx :: " + ex.getMessage());
            }

            if (value instanceof JSONObject) {
                map.putMap(key, toWM((JSONObject) value));
            } else if (value instanceof JSONArray) {
                map.putArray(key, toWA((JSONArray) value));
            } else if (value instanceof Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String) {
                map.putString(key, (String) value);
            } else {
                if (value != null) {
                    map.putString(key, value.toString());
                }
            }
        }
        return map;
    }

    /**
     * Converts a ReadableMap into a HashMap.
     *
     * @param readableMap: The ReadableMap to be converted.
     * @return A HashMap containing the data that was in the ReadableMap.
     */
    public static Map<String, Object> toMap(final ReadableMap readableMap) {
        Map<String, Object> map = new HashMap<>();
        if (readableMap == null) {
            return map;
        }
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = readableMap.getType(key);
            switch (type) {
                case Null:
                    map.put(key, null);
                    break;
                case Boolean:
                    map.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    map.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    String valueStr = readableMap.getString(key);
                    if (valueStr != null) {
                        map.put(key, valueStr);
                        break;
                    }
                    break;
                case Map:
                    map.put(key, toMap(readableMap.getMap(key)));
                    break;
                case Array:
                    map.put(key, toArray(readableMap.getArray(key)));
                    break;
                default:
                    break;
            }
        }
        return map;
    }

    // MARK: - Private Helper Methods

    /**
     * Converts a JSONArray into a WritableMap.
     *
     * @param json: JSONArray to be converted.
     * @return WritableArray
     */
    private static WritableArray toWA(JSONArray json) {
        WritableArray array = Arguments.createArray();

        for (int i = 0; i < json.length(); i++) {
            Object value = null;
            try {
                value = json.get(i);
            } catch (JSONException e) {
                Log.e(TAG, "JSONEx :: " + e.getMessage());
            }

            if (value instanceof JSONObject) {
                array.pushMap(toWM((JSONObject) value));
            } else if (value instanceof JSONArray) {
                array.pushArray(toWA((JSONArray) value));
            } else if (value instanceof Boolean) {
                array.pushBoolean((Boolean) value);
            } else if (value instanceof Integer) {
                array.pushInt((Integer) value);
            } else if (value instanceof Double) {
                array.pushDouble((Double) value);
            } else if (value instanceof String) {
                array.pushString((String) value);
            } else {
                if (value != null) {
                    array.pushString(value.toString());
                }
            }
        }
        return array;
    }

    /**
     * toArray converts a ReadableArray into a Object[].
     *
     * @param readableArray: The ReadableArray to be converted.
     * @return Object[]
     */
    public static Object[] toArray(final ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() == 0) {
            return new Object[0];
        }
        Object[] array = new Object[readableArray.size()];
        for (int i = 0; i < readableArray.size(); i++) {
            ReadableType type = readableArray.getType(i);

            switch (type) {
                case Null:
                    array[i] = null;
                    break;
                case Boolean:
                    array[i] = readableArray.getBoolean(i);
                    break;
                case Number:
                    array[i] = readableArray.getDouble(i);
                    break;
                case String:
                    array[i] = readableArray.getString(i);
                    break;
                case Map:
                    array[i] = toMap(readableArray.getMap(i));
                    break;
                case Array:
                    array[i] = toArray(readableArray.getArray(i));
                    break;
                default:
                    break;
            }
        }
        return array;
    }
}