/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
import React from 'react';
import { render, fireEvent } from '@testing-library/react-native';
import AGCCloudFunctions from './AGCCloudFunctions';
import AGCTimeUnit from './Constants/AGCTimeUnit';

describe('App', () => {
  it('should call the cloud function when the button is pressed', async () => {
    const mockCall = jest.spyOn(AGCCloudFunctions, 'call');
    const triggerIdentifier = '<your_trigger_identifier>';
    const options = {
      timeout: 1000,
      timeUnit: AGCTimeUnit.SECONDS,
      params: {
        key1: 'testString',
        key2: 123,
      },
    };

    const { getByTestId } = render(<App />);
    const button = getByTestId('call-cloud-function-button');
    fireEvent.press(button);

    expect(mockCall).toHaveBeenCalledWith(triggerIdentifier, options);
  });
});

