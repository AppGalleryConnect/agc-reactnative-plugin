import {describe, expect, test} from "@jest/globals";
import AGCRemoteConfig from '../lib';

describe('RemoteConfig Test', () => {

    test('applyDefault', () => {
        const map = new Map();
        map.set('key','value');
        expect(AGCRemoteConfig.applyDefault(map)).toBeDefined;
    });

    test('applyLastFetched', () => {
        expect(AGCRemoteConfig.applyLastFetched()).toBeDefined;
    });

    test('getValue', () => {
        expect(AGCRemoteConfig.getValue('key')).toBeDefined;
    });

    test('clearAll', () => {
        expect(AGCRemoteConfig.clearAll()).toBeDefined;
    });

    test('setCustomAttributes', () => {

        const map = new Map();
        map.set('key','value');
        expect(AGCRemoteConfig.setCustomAttributes(map)).toBeDefined;
    });

});
