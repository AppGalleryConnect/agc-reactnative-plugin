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

import com.huawei.agc.rn.clouddb.utils.CloudDBUtils;
import com.huawei.agc.rn.clouddb.utils.ReactUtils;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.Text;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class RNCloudDBQuery {
    private static final String ORDER_BY_ASC = "orderByAsc";

    private static final String ORDER_BY_DESC = "orderByDesc";

    private static final String BEGINS_WITH = "beginsWith";
    private static final String BEGIN_GROUP = "beginGroup";

    private static final String CONTAINS = "contains";

    private static final String ENDS_WITH = "endsWith";
    private static final String END_GROUP = "endGroup";

    private static final String EQUAL_TO = "equalTo";

    private static final String NOT_EQUAL_TO = "notEqualTo";

    private static final String GREATER_THAN = "greaterThan";

    private static final String GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";

    private static final String LESS_THAN = "lessThan";

    private static final String LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";

    private static final String IN = "_in";
    private static final String AND = "and";
    private static final String OR = "or";

    private static final String IS_NOT_NULL = "isNotNull";

    private static final String IS_NULL = "isNull";

    private static final String LIMIT_CONDITION = "limit";

    private static final String START_AT = "startAt";

    private static final String START_AFTER = "startAfter";

    private static final String END_AT = "endAt";

    private static final String END_BEFORE = "endBefore";

    private static final String QUERY_ELEMENTS = "queryElements";

    private static final String FIELD_NAME = "fieldName";

    private static final String OPERATION = "operation";

    private static final String VALUE = "value";


    CloudDBZoneQuery<?> query;

    Class<? extends CloudDBZoneObject> clazz;

    public RNCloudDBQuery(Class<? extends CloudDBZoneObject> clazz) {
        this.clazz = clazz;
        this.query = CloudDBZoneQuery.where(clazz);
    }

    public CloudDBZoneQuery<?> buildQuery(ReadableMap map)
        throws NoSuchFieldException, InstantiationException, IllegalAccessException, AGConnectCloudDBException {
        if (map.hasKey("queryElements")) {
            ReadableArray queryElements = map.getArray(QUERY_ELEMENTS);
            label:
            for (int i = 0; i < queryElements.size(); i++) {
                Map<String, ?> queryElement = ReactUtils.toMap(queryElements.getMap(i));
                String operation = (String) queryElement.get(OPERATION);
                if(operation ==LIMIT_CONDITION){
                    limit(query, queryElement);
                    break label;
                }else if(operation ==START_AT){
                    startAt(query, (Map<String, ?>) queryElement.get(VALUE));
                    break label;
                }else if(operation ==START_AFTER){
                    startAfter(query, (Map<String, ?>) queryElement.get(VALUE));
                    break label;
                }else if(operation ==END_AT){
                    endAt(query, (Map<String, ?>) queryElement.get(VALUE));
                    break label;
                }else if(operation ==END_BEFORE){
                    endBefore(query, (Map<String, ?>) queryElement.get(VALUE));
                    break label;
                }else if(operation.equals(BEGIN_GROUP)){
                    beginGroup(query);
                   continue;
                }else if(operation.equals(END_GROUP)){
                    endGroup(query);
                   continue;
                }else if(operation.equals(AND)){
                    and(query);
                    continue;
                }else if(operation.equals(OR)){
                    or(query);
                    continue ;
                }

                String fieldName = (String) queryElement.get(FIELD_NAME);
                Field field = clazz.getDeclaredField(fieldName);
                switch (operation) {
                    case EQUAL_TO:
                        equalTo(query, field, queryElement.get(VALUE));
                        break;
                    case NOT_EQUAL_TO:
                        notEqualTo(query, field, queryElement.get(VALUE));
                        break;
                    case GREATER_THAN:
                        greaterThan(query, field, queryElement.get(VALUE));
                        break;
                    case GREATER_THAN_OR_EQUAL_TO:
                        greaterThanOrEqualTo(query, field, queryElement.get(VALUE));
                        break;
                    case LESS_THAN:
                        lessThan(query, field, queryElement.get(VALUE));
                        break;
                    case LESS_THAN_OR_EQUAL_TO:
                        lessThanOrEqualTo(query, field, queryElement.get(VALUE));
                        break;
                    case BEGINS_WITH:
                        beginsWith(query, field, queryElement.get(VALUE));
                        break;
                    case ENDS_WITH:
                        endsWith(query, field, queryElement.get(VALUE));
                        break;
                    case CONTAINS:
                        contains(query, field, queryElement.get(VALUE));
                        break;
                    case IS_NULL:
                        isNull(query, field);
                        break;
                    case IS_NOT_NULL:
                        isNotNull(query, field);
                        break;
                    case ORDER_BY_ASC:
                        orderByAsc(query, field);
                        break;
                    case ORDER_BY_DESC:
                        orderByDesc(query, field);
                        break;
                    case IN:
                        in(query, field, queryElement.get(VALUE));
                        break;
                    default:
                        break;
                }
            }
        }
        return query;
    }

    private void equalTo(CloudDBZoneQuery<?> query, Field field, Object object) throws AGConnectCloudDBException {
        String objectString = object.toString();
        if (field.getType() == Double.class) {
            double value = Double.parseDouble(objectString);
            query.equalTo(field.getName(), value);
        } else if (field.getType() == Float.class) {
            double value = Double.parseDouble(objectString);
            query.equalTo(field.getName(), (float) value);
        } else if (field.getType() == Integer.class) {
            double value = Double.parseDouble(objectString);
            query.equalTo(field.getName(), (int) value);
        } else if (field.getType() == Short.class) {
            double value = Double.parseDouble(objectString);
            query.equalTo(field.getName(), (short) value);
        } else if (field.getType() == Long.class) {
            long value = Long.parseLong(objectString);
            query.equalTo(field.getName(), value);
        } else if (field.getType() == Boolean.class) {
            boolean value = Boolean.parseBoolean(objectString);
            query.equalTo(field.getName(), (boolean) value);
        } else if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.equalTo(field.getName(), value);
        } else if (field.getType() == Date.class) {
            double value = Double.parseDouble(objectString);
            Date date = new Date((long) value);
            query.equalTo(field.getName(), date);
        } else if (field.getType() == Byte.class) {
            double value = Double.parseDouble(objectString);
            checkByteValue(value);
            query.equalTo(field.getName(), (byte) value);
        } else {
            query.equalTo(field.getName(), objectString);
        }
    }

    private void notEqualTo(CloudDBZoneQuery<?> query, Field field, Object object) throws AGConnectCloudDBException {
        String objectString = object.toString();
        if (field.getType() == Double.class) {
            double value = Double.parseDouble(objectString);
            query.notEqualTo(field.getName(), value);
        } else if (field.getType() == Float.class) {
            double value = Double.parseDouble(objectString);
            query.notEqualTo(field.getName(), (float) value);
        } else if (field.getType() == Integer.class) {
            double value = Double.parseDouble(objectString);
            query.notEqualTo(field.getName(), (int) value);
        } else if (field.getType() == Short.class) {
            double value = Double.parseDouble(objectString);
            query.notEqualTo(field.getName(), (short) value);
        } else if (field.getType() == Long.class) {
            long value = Long.parseLong(objectString);
            query.notEqualTo(field.getName(), value);
        } else if (field.getType() == Boolean.class) {
            boolean value = Boolean.parseBoolean(objectString);
            query.notEqualTo(field.getName(), (boolean) value);
        } else if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.notEqualTo(field.getName(), value);
        } else if (field.getType() == Date.class) {
            double value = Double.parseDouble(objectString);
            Date date = new Date((long) value);
            query.notEqualTo(field.getName(), date);
        } else if (field.getType() == Byte.class) {
            double value = Double.parseDouble(objectString);
            checkByteValue(value);
            query.notEqualTo(field.getName(), (byte) value);
        } else {
            query.notEqualTo(field.getName(), objectString);
        }
    }

    private void greaterThan(CloudDBZoneQuery<?> query, Field field, Object object) throws AGConnectCloudDBException {
        String objectString = object.toString();
        if (field.getType() == Double.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThan(field.getName(), value);
        } else if (field.getType() == Float.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThan(field.getName(), (float) value);
        } else if (field.getType() == Integer.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThan(field.getName(), (int) value);
        } else if (field.getType() == Short.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThan(field.getName(), (short) value);
        } else if (field.getType() == Long.class) {
            long value = Long.parseLong(objectString);
            query.greaterThan(field.getName(), value);
        } else if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.greaterThan(field.getName(), value);
        } else if (field.getType() == Date.class) {
            double value = Double.parseDouble(objectString);
            Date date = new Date((long) value);
            query.greaterThan(field.getName(), date);
        } else if (field.getType() == Byte.class) {
            double value = Double.parseDouble(objectString);
            checkByteValue(value);
            query.greaterThan(field.getName(), (byte) value);
        } else {
            query.greaterThan(field.getName(), objectString);
        }
    }

    private void greaterThanOrEqualTo(CloudDBZoneQuery<?> query, Field field, Object object)
        throws AGConnectCloudDBException {
        String objectString = object.toString();
        if (field.getType() == Double.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThanOrEqualTo(field.getName(), value);
        } else if (field.getType() == Float.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThanOrEqualTo(field.getName(), (float) value);
        } else if (field.getType() == Integer.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThanOrEqualTo(field.getName(), (int) value);
        } else if (field.getType() == Short.class) {
            double value = Double.parseDouble(objectString);
            query.greaterThanOrEqualTo(field.getName(), (short) value);
        } else if (field.getType() == Long.class) {
            long value = Long.parseLong(objectString);
            query.greaterThanOrEqualTo(field.getName(), value);
        } else if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.greaterThanOrEqualTo(field.getName(), value);
        } else if (field.getType() == Date.class) {
            double value = Double.parseDouble(objectString);
            Date date = new Date((long) value);
            query.greaterThanOrEqualTo(field.getName(), date);
        } else if (field.getType() == Byte.class) {
            double value = Double.parseDouble(objectString);
            checkByteValue(value);
            query.greaterThanOrEqualTo(field.getName(), (byte) value);
        } else {
            query.greaterThanOrEqualTo(field.getName(), objectString);
        }
    }

    private void lessThan(CloudDBZoneQuery<?> query, Field field, Object object) throws AGConnectCloudDBException {
        String objectString = object.toString();
        if (field.getType() == Double.class) {
            double value = Double.parseDouble(objectString);
            query.lessThan(field.getName(), value);
        } else if (field.getType() == Float.class) {
            double value = Double.parseDouble(objectString);
            query.lessThan(field.getName(), (float) value);
        } else if (field.getType() == Integer.class) {
            double value = Double.parseDouble(objectString);
            query.lessThan(field.getName(), (int) value);
        } else if (field.getType() == Short.class) {
            double value = Double.parseDouble(objectString);
            query.lessThan(field.getName(), (short) value);
        } else if (field.getType() == Long.class) {
            long value = Long.parseLong(objectString);
            query.lessThan(field.getName(), value);
        } else if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.lessThan(field.getName(), value);
        } else if (field.getType() == Date.class) {
            double value = Double.parseDouble(objectString);
            Date date = new Date((long) value);
            query.lessThan(field.getName(), date);
        } else if (field.getType() == Byte.class) {
            double value = Double.parseDouble(objectString);
            checkByteValue(value);
            query.lessThan(field.getName(), (byte) value);
        } else {
            query.lessThan(field.getName(), objectString);
        }
    }

    private void lessThanOrEqualTo(CloudDBZoneQuery<?> query, Field field, Object object)
        throws AGConnectCloudDBException {
        String objectString = object.toString();
        if (field.getType() == Double.class) {
            double value = Double.parseDouble(objectString);
            query.lessThanOrEqualTo(field.getName(), value);
        } else if (field.getType() == Float.class) {
            double value = Double.parseDouble(objectString);
            query.lessThanOrEqualTo(field.getName(), (float) value);
        } else if (field.getType() == Integer.class) {
            double value = Double.parseDouble(objectString);
            query.lessThanOrEqualTo(field.getName(), (int) value);
        } else if (field.getType() == Short.class) {
            double value = Double.parseDouble(objectString);
            query.lessThanOrEqualTo(field.getName(), (short) value);
        } else if (field.getType() == Long.class) {
            long value = Long.parseLong(objectString);
            query.lessThanOrEqualTo(field.getName(), value);
        } else if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.lessThanOrEqualTo(field.getName(), value);
        } else if (field.getType() == Date.class) {
            double value = Double.parseDouble(objectString);
            Date date = new Date((long) value);
            query.lessThanOrEqualTo(field.getName(), date);
        } else if (field.getType() == Byte.class) {
            double value = Double.parseDouble(objectString);
            checkByteValue(value);
            query.lessThanOrEqualTo(field.getName(), (byte) value);
        } else {
            query.lessThanOrEqualTo(field.getName(), objectString);
        }
    }

    private void in(CloudDBZoneQuery<?> query, Field field, Object object) throws AGConnectCloudDBException {
        Object[] array = (Object[]) object;
        String fieldTypeName = field.getType().getSimpleName();
        switch (fieldTypeName) {
            case "String":
                String[] strings = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    strings[i] = (String) array[i];
                }
                query.in(field.getName(), strings);
                break;
            case "Long":
                Long[] longs = new Long[array.length];
                for (int i = 0; i < array.length; i++) {
                    Long d = Long.parseLong((String) array[i]);
                    longs[i] = d;
                }
                query.in(field.getName(), longs);
                break;
            case "Byte":
                Byte[] bytes = new Byte[array.length];
                for (int i = 0; i < array.length; i++) {
                    double value = (double) array[i];
                    checkByteValue(value);
                    bytes[i] = (byte) value;
                }
                query.in(field.getName(), bytes);
                break;
            case "Integer":
                Integer[] ints = new Integer[array.length];
                for (int i = 0; i < array.length; i++) {
                    double value = (double) array[i];
                    ints[i] = (int) value;
                }
                query.in(field.getName(), ints);
                break;
            case "Short":
                Short[] shorts = new Short[array.length];
                for (int i = 0; i < array.length; i++) {
                    shorts[i] = ((Double) array[i]).shortValue();
                }
                query.in(field.getName(), shorts);
                break;
            case "Text":
                Text[] texts = new Text[array.length];
                for (int i = 0; i < array.length; i++) {
                    Text value = new Text((String) array[i]);
                    texts[i] = value;
                }
                query.in(field.getName(), texts);
                break;
            case "Date":
                Date[] dates = new Date[array.length];
                for (int i = 0; i < array.length; i++) {
                    double value = (double) array[i];
                    Date date = new Date((long) value);
                    dates[i] = date;
                }
                query.in(field.getName(), dates);
                break;
            case "Float":
                Float[] floats = new Float[array.length];
                for (int i = 0; i < array.length; i++) {
                    floats[i] = ((Double) array[i]).floatValue();
                }
                query.in(field.getName(), floats);
                break;
            case "Double":
                Double[] doubles = new Double[array.length];
                for (int i = 0; i < array.length; i++) {
                    doubles[i] = (Double) array[i];
                }
                query.in(field.getName(), doubles);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + field.getName());
        }
    }

    private void beginsWith(CloudDBZoneQuery<?> query, Field field, Object object) {
        String objectString = object.toString();
        if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.beginsWith(field.getName(), value);
        } else {
            query.beginsWith(field.getName(), objectString);
        }
    }

    private void endsWith(CloudDBZoneQuery<?> query, Field field, Object object) {
        String objectString = object.toString();
        if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.endsWith(field.getName(), value);
        } else {
            query.endsWith(field.getName(), objectString);
        }
    }

    private void contains(CloudDBZoneQuery<?> query, Field field, Object object) {
        String objectString = object.toString();
        if (field.getType() == Text.class) {
            Text value = new Text(objectString);
            query.contains(field.getName(), value);
        } else {
            query.contains(field.getName(), objectString);
        }
    }

    private void isNull(CloudDBZoneQuery<?> query, Field field) {
        query.isNull(field.getName());
    }

    private void isNotNull(CloudDBZoneQuery<?> query, Field field) {
        query.isNotNull(field.getName());
    }

    private void orderByAsc(CloudDBZoneQuery<?> query, Field field) {
        query.orderByAsc(field.getName());
    }

    private void orderByDesc(CloudDBZoneQuery<?> query, Field field) {
        query.orderByDesc(field.getName());
    }

    private void limit(CloudDBZoneQuery<?> query, Map<String, ?> queryElement) {
        int count = (int) Double.parseDouble(queryElement.get(VALUE).toString());
        if (queryElement.containsKey("offset")) {
            int offset = (int) Double.parseDouble(queryElement.get("offset").toString());
            query.limit(count, offset);
            return;
        }
        query.limit(count);
    }

    private void startAt(CloudDBZoneQuery query, Map<String, ?> object)
        throws IllegalAccessException, NoSuchFieldException, InstantiationException, AGConnectCloudDBException {
        query.startAt(CloudDBUtils.mapToObject(this.clazz.getSimpleName(), object));
    }

    private void startAfter(CloudDBZoneQuery query, Map<String, ?> object)
        throws IllegalAccessException, NoSuchFieldException, InstantiationException, AGConnectCloudDBException {
        query.startAfter(CloudDBUtils.mapToObject(this.clazz.getSimpleName(), object));
    }

    private void endAt(CloudDBZoneQuery query, Map<String, ?> object)
        throws IllegalAccessException, NoSuchFieldException, InstantiationException, AGConnectCloudDBException {
        query.endAt(CloudDBUtils.mapToObject(this.clazz.getSimpleName(), object));
    }

    private void endBefore(CloudDBZoneQuery query, Map<String, ?> object)
        throws IllegalAccessException, NoSuchFieldException, InstantiationException, AGConnectCloudDBException {
        query.endBefore(CloudDBUtils.mapToObject(this.clazz.getSimpleName(), object));
    }

    private void endGroup(CloudDBZoneQuery query)
            throws IllegalAccessException{
        query.endGroup();
    }

    private void beginGroup(CloudDBZoneQuery query)
            throws IllegalAccessException{
        query.beginGroup();
    }

    private void and(CloudDBZoneQuery query)
            throws IllegalAccessException{
        query.and();
    }

    private void or(CloudDBZoneQuery query)
            throws IllegalAccessException{
        query.or();
    }
    private void checkByteValue(double value) throws AGConnectCloudDBException {
        if (value >= 128 && value < -128) {
            throw new AGConnectCloudDBException(
                "Byte has a minimum value of -128 and a maximum value of 127 (inclusive). ", -1);
        }
    }
}