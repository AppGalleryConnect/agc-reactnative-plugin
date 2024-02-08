import {describe, expect, test} from "@jest/globals";
import AGCCrash from '../lib';

describe('Crash Test', () => {
    test('testIt', () => {
        expect(AGCCrash.testIt()).toBeDefined;
    });
    test('setUserId', () => {
        expect(AGCCrash.setUserId('userid')).toBeDefined;
    });
    test('log', () => {
        expect(AGCCrash.log('key')).toBeDefined;
    });
    test('setCustomKey', () => {
        expect(AGCCrash.setCustomKey('key','value')).toBeDefined;
    });
    test('logWithLevel', () => {
        expect(AGCCrash.logWithLevel('level','value')).toBeDefined;
    });
    test('recordError', () => {
        expect(AGCCrash.recordError(new Error('message'))).toBeDefined;
    });
    test('recordFatalError', () => {
        expect(AGCCrash.recordFatalError(new Error('message'))).toBeDefined;
    });
});
