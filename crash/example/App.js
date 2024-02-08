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

import * as React from 'react';
import { View, Button, StyleSheet } from 'react-native';
import { Separator, Styles } from './separator';
import AGCCrash, { LogLevel } from '@hw-agconnect/react-native-crash';


export default function App() {
  return (
    <View style={Styles.sectionContainer}>
      <Button
        title="enableCrashCollection"
        onPress={() => AGCCrash.enableCrashCollection(true)}
      />
      <Separator />
      <Button
        style={{ marginTop: 30 }}
        title="testIt"
        onPress={() => AGCCrash.testIt()}
      />
      <Separator />
      <Button
        title="setUserId"
        onPress={() => AGCCrash.setUserId('userid001')}
      />
      <Separator />
      <Button
        title="setCustomKey"
        onPress={() => {
          AGCCrash.setCustomKey('key_string', 'value_string');
          AGCCrash.setCustomKey('key_boolean', true);
          AGCCrash.setCustomKey('key_number', 3.14);
        }}
      />
      <Separator />
      <Button
        title="log"
        onPress={() => AGCCrash.log('log:default message001')}
      />
      <Separator />
      <Button
        title="logWithLevel"
        onPress={() => {
          AGCCrash.logWithLevel(LogLevel.DEBUG, 'logWithLevel:DEBUG message DEBUG')
          AGCCrash.logWithLevel(LogLevel.INFO, 'logWithLevel:INFO message INFO')
          AGCCrash.logWithLevel(LogLevel.WARN, 'logWithLevel:WARN message WARN')
          AGCCrash.logWithLevel(100, 'logWithLevel:ERROR message ERROR')
        }}
      />

      <Separator />
      <Button
        title="recordError"
        onPress={() => {
          AGCCrash.recordError(new Error("recordError 1520 "));
          AGCCrash.logWithLevel(LogLevel.DEBUG, 'logWithLevel:DEBUG message DEBUG')
        }}
      />

      <Separator />

      <Separator />
      <Button
        title="recordFatalError"
        onPress={() => {
          AGCCrash.recordFatalError(new Error("recordFatalError 1521 "));
        }}
      />

      <Separator />

      <Button
        title="throw a error"
        onPress={() => {
          throw new Error("throw a error 1520");
        }}
      />

      <Separator />
      <Button
        title="promiseError"
        onPress={() => {
          new Promise(function (resolve, reject) {
            reject(new Error("Promise error 1520"));
          });
        }}
      />
    </View>
  );
}
