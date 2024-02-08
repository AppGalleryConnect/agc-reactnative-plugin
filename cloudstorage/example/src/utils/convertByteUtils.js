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

export const convertToByte = (str) => {
    var bytes = [];
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        if (c >= 0xd800 && c <= 0xdbff && i + 1 < str.length) {
            var cn = str.charCodeAt(i + 1);
            if (cn >= 0xdc00 && cn <= 0xdfff) {
                var pt = (c - 0xd800) * 0x400 + cn - 0xdc00 + 0x10000;

                bytes.push(
                    0xf0 + Math.floor(pt / 64 / 64 / 64),
                    0x80 + Math.floor(pt / 64 / 64) % 64,
                    0x80 + Math.floor(pt / 64) % 64,
                    0x80 + pt % 64
                );
                i += 1;
                continue;
            }
        }
        if (c >= 2048) {
            bytes.push(
                0xe0 + Math.floor(c / 64 / 64),
                0x80 + Math.floor(c / 64) % 64,
                0x80 + c % 64
            );
        }
        else if (c >= 128) {
            bytes.push(0xc0 + Math.floor(c / 64), 0x80 + c % 64);
        }
        else bytes.push(c);
    }
    return bytes;
};
