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

#import "ObjC.h"
#import <AGConnectDatabase/AGConnectCloudDB.h>

@implementation ObjC 

+ (BOOL)catchException:(void(^)(void))tryBlock error:(__autoreleasing NSError **)error {
    @try {
        tryBlock();
        return YES;
    }
    @catch (NSException *exception) {
        *error = [[NSError alloc] initWithDomain:exception.reason code:0 userInfo:exception.userInfo];
        return NO;
    }
}

+ (BOOL)deleteCloudDB:(AGConnectCloudDB*)agcConnectCloudDB zoneName:(NSString*)zoneName error:(__autoreleasing NSError **)error {
  
  [agcConnectCloudDB deleteCloudDBZone:zoneName error:error];
  if (*error != nil) {
    return NO;
  } else {
    return YES;
  }
  
}

+ (BOOL)closeCloudDB:(AGConnectCloudDB*)agcConnectCloudDB dbZone:(AGCCloudDBZone*)dbZone error:(__autoreleasing NSError **)error {
  
  [agcConnectCloudDB closeCloudDBZone:dbZone error:error];
  if (*error != nil) {
    return NO;
  } else {
    return YES;
  }
  
}


@end
