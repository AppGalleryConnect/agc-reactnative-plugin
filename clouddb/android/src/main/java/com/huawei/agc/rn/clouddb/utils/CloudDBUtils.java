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

import com.huawei.agconnect.cloud.database.CloudDBZoneConfig;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.ServerStatus;
import com.huawei.agconnect.cloud.database.Text;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.agc.rn.clouddb.model.ObjectTypeInfoHelper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Date;
import java.util.Map;

public class CloudDBUtils {
    public static WritableMap toWM(CloudDBZoneObject instance) throws NoSuchFieldException, IllegalAccessException {
        WritableMap wm = Arguments.createMap();
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            Field var1 = instance.getClass().getDeclaredField(fieldName);
            AccessController.doPrivileged((PrivilegedAction<Field>) () -> {
                var1.setAccessible(true);
                return null;
            });
            if (var1.get(instance) == null) {
                wm.putNull(fieldName);
            } else if (var1.getType() == String.class) {
                wm.putString(fieldName, (String) var1.get(instance));
            } else if (var1.getType() == Boolean.class) {
                wm.putBoolean(fieldName, (Boolean) var1.get(instance));
            } else if (var1.getType() == Integer.class) {
                wm.putInt(fieldName, (Integer) var1.get(instance));
            } else if (var1.getType() == Double.class) {
                wm.putDouble(fieldName, (Double) var1.get(instance));
            } else if (var1.getType() == Float.class) {
                wm.putDouble(fieldName, (Float) var1.get(instance));
            } else if (var1.getType() == Long.class) {
                wm.putString(fieldName, var1.get(instance).toString());
            } else if (var1.getType() == Short.class) {
                wm.putDouble(fieldName, (Short) var1.get(instance));
            } else if (var1.getType() == Text.class) {
                wm.putString(fieldName, ((Text) var1.get(instance)).toString());
            } else if (var1.getType() == Byte.class) {
                wm.putInt(fieldName, ((byte) var1.get(instance)));
            } else if (var1.getType() == Date.class) {
                Date date = (Date) var1.get(instance);
                wm.putDouble(fieldName, date.getTime());
            } else if (var1.getType() == byte[].class) {
                byte[] bytes = (byte[]) var1.get(instance);
                WritableArray wa = Arguments.createArray();
                for (byte aByte : bytes) {
                    wa.pushDouble(aByte);
                }
                wm.putArray(var1.getName(), wa);
            }
        }
        return wm;
    }

    public static CloudDBZoneObject mapToObject(String className, Map<String, ?> map)
        throws IllegalAccessException, InstantiationException, NoSuchFieldException, AGConnectCloudDBException {
        CloudDBZoneObject instance = getInstance(className);
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (map.containsKey(fieldName)) {
                Field var1 = instance.getClass().getDeclaredField(fieldName);
                AccessController.doPrivileged((PrivilegedAction<Field>) () -> {
                    var1.setAccessible(true);
                    return null;
                });
                if (map.get(fieldName) != null) {
                    if (var1.getType() == Double.class) {
                        double doubleValue = Double.parseDouble(map.get(fieldName).toString());
                        var1.set(instance, doubleValue);
                    } else if (var1.getType() == Float.class) {
                        double doubleValue = Double.parseDouble(map.get(fieldName).toString());
                        var1.set(instance, (float) doubleValue);
                    } else if (var1.getType() == Integer.class) {
                        double doubleValue = Double.parseDouble(map.get(fieldName).toString());
                        var1.set(instance, (int) doubleValue);
                    } else if (var1.getType() == Short.class) {
                        double doubleValue = Double.parseDouble(map.get(fieldName).toString());
                        var1.set(instance, (short) doubleValue);
                    } else if (var1.getType() == Long.class) {
                        long longValue = Long.parseLong(map.get(fieldName).toString());
                        var1.set(instance, longValue);
                    } else if (var1.getType() == Text.class) {
                        Text textValue = new Text(map.get(fieldName).toString());
                        var1.set(instance, textValue);
                    } else if (var1.getType() == Date.class) {
                        double doubleValue = Double.parseDouble(map.get(fieldName).toString());
                        Date date = new Date((long) doubleValue);
                        var1.set(instance, date);
                    } else if (var1.getType() == byte[].class) {
                        Object[] byteArray = (Object[]) map.get(fieldName);
                        byte[] bArray = new byte[byteArray.length];
                        for (int i = 0; i < byteArray.length; i++) {
                            bArray[i] = ((Double) byteArray[i]).byteValue();
                        }
                        var1.set(instance, bArray);
                    } else if (var1.getType() == Byte.class) {
                        double doubleValue = Double.parseDouble(map.get(fieldName).toString());
                        var1.set(instance, (byte) doubleValue);
                    } else {
                        var1.set(instance, map.get(fieldName));
                    }
                }
            }
        }
        return instance;
    }

    public static WritableMap configToWM(CloudDBZoneConfig config) {
        WritableMap wm = Arguments.createMap();
        wm.putInt("accessProperty", config.getAccessProperty().ordinal());
        String capacity = Long.toString(config.getCapacity());
        wm.putString("capacity", capacity);
        wm.putString("cloudDBZoneName", config.getCloudDBZoneName());
        wm.putBoolean("isPersistenceAvailable", config.getPersistenceEnabled());
        wm.putInt("syncProperty", config.getSyncProperty().ordinal());
        wm.putBoolean("isEncrypted", config.isEncrypted());
        return wm;
    }
    public static WritableMap serverStatusToWM(ServerStatus serverStatus) {
        WritableMap wm = Arguments.createMap();
        wm.putDouble("serverTimestamp", serverStatus.getServerTimestamp());
        return wm;
    }

    public static Class<? extends CloudDBZoneObject> getClass(String className) throws AGConnectCloudDBException {
        for (Class<? extends CloudDBZoneObject> myClass : ObjectTypeInfoHelper.getObjectTypeInfo().getObjectTypes()) {
            if (className.equals(myClass.getSimpleName())) {
                return myClass;
            }
        }
        throw new AGConnectCloudDBException("Class cannot be found.", 9);
    }

    public static CloudDBZoneObject getInstance(String className)
        throws InstantiationException, IllegalAccessException, AGConnectCloudDBException {
        Class<? extends CloudDBZoneObject> clazz = getClass(className);
        return clazz.newInstance();
    }

    public static CloudDBZoneQuery.CloudDBZoneQueryPolicy queryPolicyValueOf(int value)
        throws AGConnectCloudDBException {
        if (value == CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY.ordinal()) {
            return CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY;
        } else if (value == CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_LOCAL_ONLY.ordinal()) {
            return CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_LOCAL_ONLY;
        } else if (value == CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT.ordinal()) {
            return CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_DEFAULT;
        } else {
            throw new AGConnectCloudDBException("Query from the local or cloud database failed.", 5);
        }
    }

    public static CloudDBZoneConfig.CloudDBZoneSyncProperty syncPropertyValueOf(int value)
        throws AGConnectCloudDBException {
        if (value == CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_LOCAL_ONLY.ordinal()) {
            return CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_LOCAL_ONLY;
        } else if (value == CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE.ordinal()) {
            return CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE;
        } else {
            throw new AGConnectCloudDBException("Failed to create or open a cloudDBZone.", 3);
        }
    }

    public static CloudDBZoneConfig.CloudDBZoneAccessProperty accessPropertyValueOf(int value) {
        return CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC;
    }
}