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
import AuthProviderType from './AuthProviderType';
const { AGCAuthModule } = NativeModules;

export default class PhoneAuthProvider {
  constructor() {
    throw new Error('`new PhoneAuthProvider()` is not supported， please use static functions');
  }

  static credentialWithPassword(countryCode, phoneNumber, password) {
    return {
      countryCode: countryCode,
      phoneNumber: phoneNumber,
      password: password,
      provider: AuthProviderType.Phone
    };
  }

  static credentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode) {
    return {
      countryCode: countryCode,
      phoneNumber: phoneNumber,
      password: password,
      verificationCode: verifyCode,
      provider: AuthProviderType.Phone,
    };
  }

  static requestVerifyCode(countryCode, phoneNumber, verifyCodeSettings) {
    if (!verifyCodeSettings) {
      throw new Error('verifyCodeSettings can not be null');
    }
    const settings = {
      action: verifyCodeSettings.action,
      locale: verifyCodeSettings.lang,
      sendInterval: verifyCodeSettings.sendInterval
    }
    return AGCAuthModule.requestPhoneVerifyCode(countryCode, phoneNumber, settings);
  }

}