/**
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
 *
 * @format
 */
/* eslint-env jest */

import {NativeModules} from 'react-native';

NativeModules.RNRemoteConfig = {
  applyDefault: jest.fn(),
  applyLastFetched:jest.fn(),
  fetch:jest.fn(),
  fetchDefault:jest.fn(),
  getValue:jest.fn(),
  getSource:jest.fn(),
  getMergedAll:jest.fn(),
  clearAll:jest.fn(),
  setDeveloperMode:jest.fn(),
  getCustomAttributes:jest.fn(),
  setCustomAttributes:jest.fn(),
};



// Reset the mocks before each test
global.beforeEach(() => {
  jest.resetAllMocks();
});
