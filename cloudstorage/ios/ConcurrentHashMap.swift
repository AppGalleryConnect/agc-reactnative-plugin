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

class ConcurrentHashMap<V: Hashable,T>: Collection , AGCStorageHanding{
    
    private var dictionary: [V: T]
    private let concurrentQueue = DispatchQueue(label: "Concurrent Queue", attributes: .concurrent)
    
    var startIndex: Dictionary<V, T>.Index {
        self.concurrentQueue.sync {
            return self.dictionary.startIndex
        }
    }
    
    var endIndex: Dictionary<V, T>.Index {
        self.concurrentQueue.sync {
            return self.dictionary.endIndex
        }
    }
    
    init(dict: [V: T] = [V: T]()) {
        self.dictionary = dict
    }
    
    func index(after i: Dictionary<V, T>.Index) -> Dictionary<V, T>.Index {
        return self.dictionary.index(after: i)
    }
    
    subscript(key: V) -> T? {
        get {
            self.concurrentQueue.sync {
                return self.dictionary[key]
            }
        }
        set(newValue) {
            self.concurrentQueue.async(flags: .barrier) {[weak self] in
                self?.dictionary[key] = newValue
            }
        }
    }
    
    subscript(index: Dictionary<V, T>.Index) -> Dictionary<V, T>.Element {
        self.concurrentQueue.sync {
            return self.dictionary[index]
        }
    }
    
    func removeValue(forKey key: V) {
        self.concurrentQueue.async(flags: .barrier) { [weak self] in
            self?.dictionary.removeValue(forKey: key)
        }
    }
    static func getUUID(set: ConcurrentHashMap<String, T>) -> String {
        var uuid = UUID().uuidString
        while(set[uuid] != nil) {
            uuid = UUID().uuidString
        }
        return uuid
    }
    static func isExist(set: ConcurrentHashMap<String, T>, key: String, reject: RCTPromiseRejectBlock) -> Bool {
        if(set[key] != nil) {
            return true
        }
        reject("11001", "Not found instance.",nil)
        return false
    }
    
    
}
