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

import { NativeModules } from 'react-native';
import AGCTimeUnit from './Constants/AGCTimeUnit';
 
const { AGCCloudFunctionsModule } = NativeModules;
 
export default class AGCCloudFunctions {
/**
 * Configures and calls a cloud function.
 * @param {string} name - HTTP trigger identifier of the cloud function to be called. The trigger identifier is in "Function name-Version number" format.
 * For Example the identifier of the latest version of hello function is as follows: "hello-$latest"
 * For details about how to query the HTTP trigger identifier, please refer to:
 * {@link https://developer.huawei.com/consumer/en/doc/AppGallery-connect-Guides/agc-function-reactnative-usage-0000001066750394#section141341855561 Querying the Trigger Identifier}.
 * @param {AGCFunctionOptions} options - Cloud function settings.
 * @returns {Promise<object>} - Result returned after a cloud function is executed.
 * @example
 * import AGCCloudFunctions, {
 *   AGCTimeUnit,
 * } from "@hw-agconnect/react-native-cloudfunctions";
 *
 * const triggerIdentifier = "hello-$latest";
 *
 * const options = {
 *   timeout: 1000,
 *   timeUnit: AGCTimeUnit.SECONDS,
 *   params: {
 *     key1: "testString",
 *     key2: 123
 *   }
 * }
 *
 * AGCCloudFunctions.call(triggerIdentifier, options)
 *   .then(response => {
 *    console.log(response);
 *   })
 *   .catch((error) => {
 *     console.log("Error", error.toString());
 *   });
 */
    static call(name, options = {}) {
        return AGCCloudFunctionsModule.call(name, options);
    }
}
 
export { AGCTimeUnit };