//
//  REAJsiUtilities.cpp
//  RNReanimated
//
//  Created by Christian Falch on 25/04/2019.
//  Copyright © 2019 Facebook. All rights reserved.
//

#include "YeetJSIUtils.h"
#import <React/RCTBridgeModule.h>
#import <React/RCTConvert.h>
//#import <ReactCommon/TurboModuleUtils.h>

/**
 * All helper functions are ObjC++ specific.
 */
jsi::Value convertNSNumberToJSIBoolean(jsi::Runtime &runtime, NSNumber *value) {
    return jsi::Value((bool)[value boolValue]);
}

jsi::Value convertNSNumberToJSINumber(jsi::Runtime &runtime, NSNumber *value) {
    return jsi::Value([value doubleValue]);
}

jsi::String convertNSStringToJSIString(jsi::Runtime &runtime, NSString *value) {
    return jsi::String::createFromUtf8(runtime, [value UTF8String] ?: "");
}

jsi::Value convertObjCObjectToJSIValue(jsi::Runtime &runtime, id value);
jsi::Object convertNSDictionaryToJSIObject(jsi::Runtime &runtime, NSDictionary *value) {
    jsi::Object result = jsi::Object(runtime);
    for (NSString *k in value) {
        result.setProperty(runtime, [k UTF8String], convertObjCObjectToJSIValue(runtime, value[k]));
    }
    return result;
}

jsi::Array convertNSArrayToJSIArray(jsi::Runtime &runtime, NSArray *value) {
    jsi::Array result = jsi::Array(runtime, value.count);
    for (size_t i = 0; i < value.count; i++) {
        result.setValueAtIndex(runtime, i, convertObjCObjectToJSIValue(runtime, value[i]));
    }
    return result;
}

std::vector<jsi::Value> convertNSArrayToStdVector(jsi::Runtime &runtime, NSArray *value) {
    std::vector<jsi::Value> result;
    for (size_t i = 0; i < value.count; i++) {
        result.emplace_back(convertObjCObjectToJSIValue(runtime, value[i]));
    }
    return result;
}

jsi::Value convertObjCObjectToJSIValue(jsi::Runtime &runtime, id value) {
    if ([value isKindOfClass:[NSString class]]) {
        return convertNSStringToJSIString(runtime, (NSString *)value);
    } else if ([value isKindOfClass:[NSNumber class]]) {
        if ([value isKindOfClass:[@YES class]]) {
            return convertNSNumberToJSIBoolean(runtime, (NSNumber *)value);
        }
        return convertNSNumberToJSINumber(runtime, (NSNumber *)value);
    } else if ([value isKindOfClass:[NSDictionary class]]) {
        return convertNSDictionaryToJSIObject(runtime, (NSDictionary *)value);
    } else if ([value isKindOfClass:[NSArray class]]) {
        return convertNSArrayToJSIArray(runtime, (NSArray *)value);
    } else if (value == (id)kCFNull) {
        return jsi::Value::null();
    }
    return jsi::Value::undefined();
}

id convertJSIValueToObjCObject(jsi::Runtime &runtime, const jsi::Value &value);
NSString *convertJSIStringToNSString(jsi::Runtime &runtime, const jsi::String &value) {
    return [NSString stringWithUTF8String:value.utf8(runtime).c_str()];
}

NSArray *convertJSIArrayToNSArray(jsi::Runtime &runtime, const jsi::Array &value) {
    size_t size = value.size(runtime);
    NSMutableArray *result = [NSMutableArray new];
    for (size_t i = 0; i < size; i++) {
        // Insert kCFNull when it's `undefined` value to preserve the indices.
        [result addObject:convertJSIValueToObjCObject(runtime, value.getValueAtIndex(runtime, i))
                              ?: (id)kCFNull];
    }
    return [result copy];
}

NSDictionary *convertJSIObjectToNSDictionary(jsi::Runtime &runtime, const jsi::Object &value) {
    jsi::Array propertyNames = value.getPropertyNames(runtime);
    size_t size = propertyNames.size(runtime);
    NSMutableDictionary *result = [NSMutableDictionary new];
    for (size_t i = 0; i < size; i++) {
        jsi::String name = propertyNames.getValueAtIndex(runtime, i).getString(runtime);
        NSString *k = convertJSIStringToNSString(runtime, name);
        id v = convertJSIValueToObjCObject(runtime, value.getProperty(runtime, name));
        if (v) {
            result[k] = v;
        }
    }
    return [result copy];
}

RCTResponseSenderBlock convertJSIFunctionToCallback(jsi::Runtime &runtime,
                                                    const jsi::Function &value) {
    __block auto cb = value.getFunction(runtime);

    return ^(NSArray *responses) {
      cb.call(runtime, convertNSArrayToJSIArray(runtime, responses), 2);
    };
}

id convertJSIValueToObjCObject(jsi::Runtime &runtime, const jsi::Value &value) {
    if (value.isUndefined() || value.isNull()) {
        return nil;
    }
    if (value.isBool()) {
        return @(value.getBool());
    }
    if (value.isNumber()) {
        return @(value.getNumber());
    }
    if (value.isString()) {
        return convertJSIStringToNSString(runtime, value.getString(runtime));
    }
    if (value.isObject()) {
        jsi::Object o = value.getObject(runtime);
        if (o.isArray(runtime)) {
            return convertJSIArrayToNSArray(runtime, o.getArray(runtime));
        }
        if (o.isFunction(runtime)) {
            return convertJSIFunctionToCallback(runtime, std::move(o.getFunction(runtime)));
        }
        return convertJSIObjectToNSDictionary(runtime, o);
    }

    throw std::runtime_error("Unsupported jsi::jsi::Value kind");
}

Promise::Promise(jsi::Runtime &rt, jsi::Function resolve, jsi::Function reject)
    : runtime_(rt), resolve_(std::move(resolve)), reject_(std::move(reject)) {}

void Promise::resolve(const jsi::Value &result) { resolve_.call(runtime_, result); }

void Promise::reject(const std::string &message) {
    jsi::Object error(runtime_);
    error.setProperty(runtime_, "message", jsi::String::createFromUtf8(runtime_, message));
    reject_.call(runtime_, error);
}

jsi::Value createPromiseAsJSIValue(jsi::Runtime &rt, const PromiseSetupFunctionType func) {
    jsi::Function JSPromise = rt.global().getPropertyAsFunction(rt, "Promise");
    jsi::Function fn = jsi::Function::createFromHostFunction(
        rt, jsi::PropNameID::forAscii(rt, "fn"), 2,
        [func](jsi::Runtime &rt2, const jsi::Value &thisVal, const jsi::Value *args, size_t count) {
            jsi::Function resolve = args[0].getObject(rt2).getFunction(rt2);
            jsi::Function reject = args[1].getObject(rt2).getFunction(rt2);
            auto wrapper = std::make_shared<Promise>(rt2, std::move(resolve), std::move(reject));
            func(rt2, wrapper);
            return jsi::Value::undefined();
        });

    return JSPromise.callAsConstructor(rt, fn);
}
