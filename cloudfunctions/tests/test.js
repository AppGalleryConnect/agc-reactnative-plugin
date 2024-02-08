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

import AGCCloudFunctions from './AGCCloudFunctions';
import AGCTimeUnit from './Constants/AGCTimeUnit';

describe('AGCCloudFunctions', () => {
  it('should call the function with the correct name and options', () => {
    const name = 'myFunction';
    const options = { timeUnit: AGCTimeUnit.SECONDS };
    const expectedResponse = 'Success!';

    AGCCloudFunctionsModule.call = jest.fn(() => Promise.resolve(expectedResponse));

    return AGCCloudFunctions.call(name, options).then((response) => {
      expect(response).toEqual(expectedResponse);
      expect(AGCCloudFunctionsModule.call).toHaveBeenCalledWith(name, options);
    });
  });
});
