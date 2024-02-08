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

/**
 * Cloud DB entry class, which is in single AGCCloudDB instance mode.
 */
export default class AGCCloudDB {

    /**
     * Obtains  an AGConnectCloudDB Instance.
     */
    static getInstance(): AGCCloudDB;

    /**
     * Creates object types and defines a set for storing CloudDBZoneObjects.
     */
    createObjectType(): Promise<null>;

    /**
     * Closes the AGCCloudDBZone object opened on the device.
     */
    closeCloudDBZone(cloudDBZone: AGCCloudDBZone): Promise<null>;
    
    /**
     * Obtains all CloudDBZoneConfig lists in the AGConnectCloudDB instance on the device.
     */
    getCloudDBZoneConfigs(): Promise<[CloudDBZoneConfig]>;

    /**
     * @deprecated The method should not be used
     * Creates or opens an object of a AGCCloudDBZone which represents a unique data storage zone.
     * 
     * @param cloudDBZoneConfig AGCCloudDBZoneConfig object is used to create or open a AGCCloudDBZone.
     * @param isAllowToCreate Specifies whether to allow AGCCloudDBZone object creation.
     */
    openCloudDBZone(cloudDBZoneConfig: AGCCloudDBZoneConfig, isAllowToCreate: boolean): Promise<AGCCloudDBZone>;

    /**
     * Asynchronously creates or opens an object of a AGCCloudDBZone which represents a unique data storage zone.
     * 
     * @param cloudDBZoneConfig AGCCloudDBZoneConfig object is used to create or open a AGCCloudDBZone.
     * @param isAllowToCreate Specifies whether to allow AGCCloudDBZone object creation.
     */
    openCloudDBZone2(cloudDBZoneConfig: AGCCloudDBZoneConfig, isAllowToCreate: boolean): Promise<AGCCloudDBZone>;

    /**
     * Deletes the AGCCloudDBZone object that is no longer used on the device.
     * 
     * @param zoneName Cloud DB zone name.
     */
    deleteCloudDBZone(zoneName: string): Promise<null>;

    /**
     * Enables data synchronization between the device and cloud.
     * 
     * @param zoneName Cloud DB zone name.
     */
    enableNetwork(zoneName: string): Promise<null>;

    /**
     * Disables data synchronization between the device and cloud.
     * 
     * @param zoneName Cloud DB zone name.
     */
    disableNetwork(zoneName: string): Promise<null>;

    /**
     * Sets or modifies the user password for Cloud DB full encryption.
     * 
     * @param userKey User password, which is a string of 8 to 32 characters and can contain only digits, lowercase letters, uppercase letters, spaces, and special characters.
     * @param userReKey New user password, which is a string of 8 to 32 characters and can contain only digits, lowercase letters, uppercase letters, spaces, and special characters.
     * This parameter is required when the password is changed. Otherwise, the value is null or an empty string.
     * @param needStrongCheck Determines whether to enable strong password verification. The rules for setting strong or weak verification are as follows:
     * Weak verification: User password is a string of 6 to 32 characters and can contain only digits, lowercase letters, uppercase letters, spaces, and special characters.
     * Strong verification: User password is a string of 8 to 32 characters and must contain at least two types of the following: digits, lowercase letters, uppercase letters, spaces, and special characters.
     */
    
    setUserKey(userKey: string, userReKey: string, needStrongCheck:boolean): Promise<boolean>;

    /**
     * Updates the data key.
     */
    updateDataEncryptionKey(): Promise<boolean>;

    /**
     * Listens to registered user key change events.
     * 
     * @param listener Event listener function.
     */
    addEventListener(listener: eventListener): Promise<null>;

    /**
     * Registers a listener for data key changes.
     * 
     * @param listener Data encryption key of a listener function.
     */
    addDataEncryptionKeyListener(listener: dataEncryptionKeyListener): Promise<null>;
}

/**
 * Event listener function type.
 */
export type eventListener = (eventType: AGCCloudDBEventType) => any;

/**
 * Data encryption key listener function type.
 */
export type dataEncryptionKeyListener = (isDataKeyChanged: boolean) => any;

/**
 * An object of a AGCCloudDBZone indicates a Cloud DB storage zone.
 */
export class AGCCloudDBZone {

    /**
     * Obtains the current configuration information of Cloud DB zone. (ONLY ANDROID)
     */
    getCloudDBZoneConfig(): Promise<AGCCloudDBZoneConfig>;

    /**
     * Writes an object a or group of objects to the current Cloud DB zone in batches.
     * 
     * @param className Object Type Name.
     * @param object A list of objects, which represents the data to be written.
     */
    executeUpsert(className: string, object: Object | Object[]): Promise<number>;

    /**
     * Deletes an object or a group of objects whose primary key values are the same as those of the input object list in batches.
     * 
     * @param className Object Type Name.
     * @param object A list of objects, which represents the data to be deleted.
     */
    executeDelete(className: string, object: Object | Object[]): Promise<number>;

    /**
     * Queries objects that meet specified conditions in Cloud DB zone and encapsulates the query result set into a CloudDBZoneSnapshot.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     */
    executeQuery(query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy): Promise<CloudDBZoneSnapshot>;

    /**
     * Queries objects that meet specified conditions in Cloud DB zone and returns the average value of specified fields.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param fieldName Specifies the name of the fields whose average value needs to be calculated in the query object. The data type of a specified field can only be number.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     */
    executeAverageQuery(query: AGCCloudDBQuery, fieldName: string, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy): Promise<number>;

    /**
     * Queries objects that meet specific conditions and returns the sum of data record values of specified fields in the Cloud DB zone.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param fieldName Specifies the names of the fields to be summed up in the query object. The data type of a specified field can only be number.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     */
    executeSumQuery(query: AGCCloudDBQuery, fieldName: string, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy): Promise<number | null>;

    /**
     * Queries the objects that meet specific conditions and returns the maximum value of the data records in the specified fields in the Cloud DB zone.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param fieldName Specifies the name of the field whose maximum value is to be queried in the query object. The data type of a specified field can only be number.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     */
    executeMaximumQuery(query: AGCCloudDBQuery, fieldName: string, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy): Promise<number>; 

    /**
     * Queries the objects that meet specific conditions in the Cloud DB zone and returns the minimum value of the data records in the designated fields.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param fieldName Specifies the name of the field whose minimum  value is to be queried in the query object. The data type of a specified field can only be number.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     */
    executeMinimalQuery(query: AGCCloudDBQuery, fieldName: string, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy): Promise<number>;

    /**
     * Queries the objects that meet specific conditions in the Cloud DB zone and returns the number of data records of the specified fields.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param fieldName	Specifies the names of the fields which need to be counted in the query object.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     */
    executeCountQuery(query: AGCCloudDBQuery, fieldName: string, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy): Promise<number>;

    /**
     * Queries all object data that meets specified conditions in Cloud DB but has not been synchronized to the cloud, and encapsulates the query result set into a CloudDBZoneSnapshot.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     */
    executeQueryUnsynced(query: AGCCloudDBQuery): Promise<CloudDBZoneSnapshot>;

    /**
     * Registers a listener for a specified object on the device or cloud. When the data of the object that is listened on is changed, the registered OnSnapshotListener callback is triggered.
     * 
     * @param query A AGCCloudDBQuery object, which indicates the query condition. The AGCCloudDBQuery class provides methods such as where(), equalTo(), notEqualTo(), and greaterThan() to encapsulate and implement query conditions.
     * @param queryPolicy Query policy, which specifies the data source to be queried.
     * @param listener listener function.
     */
    subscribeSnapshot(query: AGCCloudDBQuery, queryPolicy: AGCCloudDBQuery.CloudDBZoneQueryPolicy, listener: snapshotListener): Promise<AGCCloudDBListenerHandler | AGCCloudDBException>;

    /**
     * Executes a specified transaction operation.
     * 
     * @param transaction An instance of the class that implements the function. You can encapsulate a group of operations in the function.
     */
    runTransaction(transaction: AGCCloudDBTransaction): Promise<boolean>;

    /**
     * Queries the current server status from CloudDBZone.
     */
    executeServerStatusQuery(): Promise<ServerStatus>;
}

/**
 * Snapshot listener function type.
 */
export type snapshotListener = (snapshot: CloudDBZoneSnapshot) => any;

/**
 * This class provides various predicates such as where(), equalTo(), notEqualTo(), and in() to construct query conditions.
 */
export class AGCCloudDBQuery {

    /**
     * Obtains a AGCCloudDBQuery object and specifies the object type to be queried. The AGCCloudDBQuery object does not provide any construction method. You can obtain an object type instance only by using the where method.
     * 
     * @param className A class name that represents the object type entity class defined by the developer.
     */
    static where(className: string): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is equal to a specified value.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    equalTo(fieldName: string, value: string | number | boolean | Date): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is not equal to a specified value.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    notEqualTo(fieldName: string, value: string | number | boolean | Date): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is greater than a specified value.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    greaterThan(fieldName: string, value: string | number | Date): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is greater than or equal to a specified value.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    greaterThanOrEqualTo(fieldName: string, value: string | number | Date): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is less than a specified value.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    lessThan(fieldName: string, value: string | number | Date): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is less than or equal to a specified value.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    lessThanOrEqualTo(fieldName: string, value: string | number | Date): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field in an entity class is contained in a specified array.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    in(fieldName: string, value: number[] | string[] | Date[]): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field of the string or text type in an entity class starts with a specified substring.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    beginsWith(fieldName: string, value: string): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field of the string or text type in an entity class ends with a specified substring.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field. 
     */
    endsWith(fieldName: string, value: string): AGCCloudDBQuery;

    /**
     * Adds a query condition where the value of a field of the string or text type in an entity class contains a specified substring.
     * 
     * @param fieldName Name of a field in an entity class.
     * @param value Specified value of the selected field.
     */
    contains(fieldName: string, value: string): AGCCloudDBQuery;

    /**
     * Adds a query condition where a field in an entity class is null.
     * 
     * @param fieldName Name of a field in an entity class.
     */
    isNull(fieldName: string): AGCCloudDBQuery;

    /**
     * Adds a query condition where a field in an entity class is not null.
     * 
     * @param fieldName Name of a field in an entity class.
     */
    isNotNull(fieldName: string): AGCCloudDBQuery;

    /**
     * Sorts the query results in ascending order by a specified field.
     * 
     * @param fieldName Name of a field in an entity class.
     */
    orderByAsc(fieldName: string): AGCCloudDBQuery;

    /**
     * Sorts the query results in descending order by a specified field.
     * 
     * @param fieldName Name of a field in an entity class.
     */
    orderByDesc(fieldName: string): AGCCloudDBQuery;

    /**
     * Adds a query condition to specify that only the count object data records starting from the specified offset position in the query result set are returned.
     * 
     * @param count Maximum number of data records that can be obtained.
     * @param offset (OPTIONAL) Specifies the start position of data records. You can specify whether to set the offset position based on the actual requirements.
     */
    limit(count: number, offset?: number): AGCCloudDBQuery;

    /**
     * Sets the start position of the data to be queried and returns the data records after the start position. The returned result contains the data records corresponding to the specified start position.
     * 
     * @param object The object corresponding to the start position. The object cannot be empty.
     */
    startAt(object: Object): AGCCloudDBQuery;

    /**
     * Sets the start position of the data to be queried and returns the data records after the start position. The returned result does not contain the data records corresponding to the specified start position.
     * 
     * @param object The object corresponding to the start position. The object cannot be empty.
     */
    startAfter(object: Object): AGCCloudDBQuery;

    /**
     * Sets the end position of the data to be queried. The data records before the end position are returned. The returned result contains the data records corresponding to the specified end position.
     * 
     * @param object The object corresponding to the start position. The object cannot be empty.
     */
    endAt(object: Object): AGCCloudDBQuery;

    /**
     * Sets the end position of the data to be queried. The data records before the end position are returned. The returned result does not contain the data records corresponding to the specified end position.
     * 
     * @param object The object corresponding to the start position. The object cannot be empty.
     */
    endBefore(object: Object): AGCCloudDBQuery;

    /**
     * Combines two conditions before and after the and method (the AND operation) and returns the intersection of the two query results.
     *
     */
    and(): AGCCloudDBQuery;

    /**
     * Combines the conditions before and after the or method (the OR operation) and returns the union of the two query results.
     *
     */
    or(): AGCCloudDBQuery;

    /**
     * Appends the left parenthesis to any query condition and uses it together with the right parenthesis of the same query.
     * During query condition construction, beginGroup() and endGroup() must appear in pairs and be used together with other query methods.
     *
     */
    beginGroup(): AGCCloudDBQuery;

    /**
     * Appends the right parenthesis to any query condition and uses it together with the left parenthesis of the same query.
     * During query condition construction, beginGroup() and endGroup() must appear in pairs and be used together with other query methods.
     *
     */
    endGroup(): AGCCloudDBQuery;

    /**
     * Finalize and builds the AGCCloudDBQuery object.
     */
    build(): AGCCloudDBQuery;
}

export namespace AGCCloudDBQuery{

    /**
     * Query policy, which specifies the data source to be queried.
     */
    enum CloudDBZoneQueryPolicy {
        /**
         * Data is queried from local cache.
         */
        POLICY_QUERY_FROM_LOCAL_ONLY,

        /**
         * Data is queried from Cloud DB zone on the cloud.
         */
        POLICY_QUERY_FROM_CLOUD_ONLY,

        /**
         * Data is queried from both Cloud DB zone on the cloud and local cache.
         * When a device is offline, data is queried from local cache.
         * When a device is online, data is queried from both Cloud DB zone on the cloud and local cache.
         */
        POLICY_QUERY_DEFAULT,
    }
}

/**
 * This class is used to create a AGCCloudDBZoneConfig object and configure AGCCloudDBZone information such as the synchronization property, access property, encrypted storage property, and data persistency property.
 */
export class AGCCloudDBZoneConfig {
    
    /**
     * Creates a AGCCloudDBZoneConfig object.
     * 
     * @param cloudDBZoneName Name of a Cloud DB zone which represents a unique data storage zone.
     * @param cloudDBZoneSyncProperty Synchronization property of the Cloud DB zone, which specifies whether to synchronize data of Cloud DB zone between the device and the cloud.
     * @param cloudDBZoneAccessProperty Access property of the Cloud DB zone, which is used to define the security property when an application accesses Cloud DB zone.
     */
    constructor(cloudDBZoneName: string, cloudDBZoneSyncProperty: AGCCloudDBZoneConfig.CloudDBZoneSyncProperty, cloudDBZoneAccessProperty: AGCCloudDBZoneConfig.CloudDBZoneAccessProperty);

    /**
     * Obtains the Cloud DB zone name on the device.
     */
    getCloudDBZoneName(): string;

    /**
     * Obtains the Cloud DB zone data synchronization property on the device.
     */
    getSyncProperty(): AGCCloudDBZoneConfig.CloudDBZoneSyncProperty;

    /**
     * Obtains the Cloud DB zone access property on the device.
     */
    getAccessProperty(): AGCCloudDBZoneConfig.CloudDBZoneAccessProperty;

    /**
     * Determines whether the Cloud DB zone data is encrypted on the device.
     */
    isEncrypted(): boolean;

    /**
     * Sets or changes the key for encrypting and storing Cloud DB zone data on the device.
     * 
     * @param key Encryption key of Cloud DB zone on the device. The value contains 1 to 128 characters. This parameter is mandatory when a key is added.
     * @param reKey New encryption key of Cloud DB zone on the device. The value contains 1 to 128 characters. This parameter is mandatory when the key is changed. Otherwise, the parameter value is null or an empty string
     */
    setEncryptedKey(key: string , reKey: string): void;

    /**
     * Sets the data persistency property of Cloud DB zone on the device.
     * 
     * @param enabled Data persistency property of the Cloud DB zone.
     */
    setPersistenceEnabled(enabled: boolean): void;

    /**
     * Obtains the data persistence information of Cloud DB zone on the device.
     */
    getPersistenceEnabled(): boolean;

    /**
     * Sets the storage space size of Cloud DB zone on the device.
     * 
     * @param capacity Size of the Cloud DB zone, in bytes.
     */
    setCapacity(capacity: number): void;

    /**
     * Obtains the storage space size of Cloud DB zone on the device.
     */
    getCapacity(): number;
}

export namespace AGCCloudDBZoneConfig {

    /**
     * Synchronization property of the Cloud DB zone, which specifies whether to synchronize data of Cloud DB zone between the device and the cloud and the synchronization mode.
     */
    enum CloudDBZoneSyncProperty {

        /**
         * Local mode. Data is stored only on the device and is not synchronized to the cloud.
         */
        CLOUDDBZONE_LOCAL_ONLY,

        /**
         * Cache mode. Data is stored on the cloud, and data on the device is a subset of data on the cloud. If persistent cache is allowed, Cloud DB supports the automatic caching of query results on the device. After a listener is registered on the device to listen on the data on the cloud, the device is notified only when the data on the cloud changes.
         */
        CLOUDDBZONE_CLOUD_CACHE
    }

    /**
     * Access property of the Cloud DB zone, which is used to define the security property when an application accesses Cloud DB zone.
     */
    enum CloudDBZoneAccessProperty {

        /**
         * Public storage zone. Access is authenticated based on the user-defined permissions.
         */
        CLOUDDBZONE_PUBLIC
    }
}

/**
 * Type of events
 */
export enum AGCCloudDBEventType {

    /**
     * User key change event.
     */
    USER_KEY_CHANGED
}

/**
 * CloudDBZoneSnapshot is used to describe the queried snapshot data, including full object sets, added and modified object sets, and newly deleted object sets.
 */
export interface CloudDBZoneSnapshot {

    /**
     * Determines whether the current snapshot contains objects that have not been synchronized to the cloud.
     */
    hasPendingWrites: boolean,

    /**
     * Determines whether the objects in the current snapshot can be queried from the cloud.
     */
    isFromCloud: boolean,

    /**
     * Obtains all objects from snapshots.
     */
    snapshotObjects: Object[],

    /**
     * Obtains the objects added or modified in a snapshot.
     */
    upsertedObjects: Object[],

    /**
     * Obtains the newly deleted objects from the snapshot.
     */
    deletedObjects: Object[],
}

/**
 * AGCCloudDBZone config informations.
 */
 export interface CloudDBZoneConfig {

    /**
     * Cloud DB zone access property on the device.
     */
    accessProperty: number,

    /**
     * The storage space size of Cloud DB zone on the device.
     */
    capacity: number,

    /**
     * The Cloud DB zone name on the device.
     */
    cloudDBZoneName: string,

    /**
     * The data persistence information of Cloud DB zone on the device.
     */
    isPersistenceAvailable: boolean,

    /**
     * The Cloud DB zone data synchronization property on the device.
     */
    syncProperty: number,

    /**
     * Whether the Cloud DB zone data is encrypted on the device.
     */
    isEncrypted: boolean
}

/**
 * The ServerStatus class indicates the cloud server status
 */
export interface ServerStatus {

    /**
     * The current timestamp of the cloud server.
     */
    ServerTimestamp: Double,
}

/**
 * AGCCloudDBTransaction class describes a transaction. The transaction provides the executeUpsert(), and executeDelete() methods to add, delete, and modify data in the Cloud DB zone on the cloud.
 */
export class AGCCloudDBTransaction {

    /**
     * Initializes a AGCCloudDBTransaction Function object.
     */
    static Function(): AGCCloudDBTransaction;

    /**
     * Writes a group of objects to the Cloud DB zone in a transaction in batches.
     * 
     * @param className Object Type Name.
     * @param objectArray A list of objects, which represents the data to be written.
     */
    executeUpsert(className: string, objectArray: Object[]): AGCCloudDBTransaction;

    /**
     * Deletes a group of objects from the Cloud DB zone in a transaction in batches.
     * 
     * @param className Object Type Name.
     * @param objectArray A list of objects, which represents the data to be written.
     */
    executeDelete(className: string, objectArray: Object[]): AGCCloudDBTransaction;


    /**
     * Finalize and builds the AGCCloudDBTransaction object.
     */
    build(): AGCCloudDBTransaction;
}

/**
 * The ListenerHandler class indicates a snapshot listener.
 */
export class AGCCloudDBListenerHandler {

    /**
     * Deregisters the snapshot listener.
     */
    remove(): Promise<null>;
}

/**
 * Exception base class of Cloud DB
 */
export class AGCCloudDBException {

    /**
     * For detailed exception information.
     */
    message: string;

    /**
     * Error code value
     */
    code?: number;
}