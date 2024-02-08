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

const { AGCAuthModule } = NativeModules;

export default class VerifyCodeSettings {
  constructor(action, lang, sendInterval) {
    this._action = action;
    this._lang = lang;
    this._sendInterval = sendInterval;
  }

  get action() {
    return this._action || VerifyCodeAction.REGISTER_OR_LOGIN;
  }

  get lang() {
    return this._lang || null;
  }

  get sendInterval() {
    return this._sendInterval || 0;
  }
}

export const VerifyCodeAction = {
  REGISTER_OR_LOGIN: AGCAuthModule.action.ACTION_REGISTER_LOGIN,
  RESET_PASSWORD: AGCAuthModule.action.ACTION_RESET_PASSWORD
}