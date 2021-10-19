#import "SimpleJsi.h"
#import "RNNHostObject.h"
#import "RNNInstance.h"
#import "YeetJSIUtils.h"
#import <React/RCTBridge+Private.h>
#import <React/RCTUtils.h>
#import <jsi/jsi.h>
#import <sys/utsname.h>

using namespace facebook::jsi;
using namespace std;

@implementation SimpleJsi

@synthesize bridge = _bridge;
@synthesize methodQueue = _methodQueue;

RCT_EXPORT_MODULE()

- (instancetype)initWithCommandsHandler:(RNNCommandsHandler *)commandsHandler {
    self = [super init];
    _commandsHandler = commandsHandler;
    return self;
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (void)setBridge:(RCTBridge *)bridge {
    _bridge = bridge;
    _setBridgeOnMainQueue = RCTIsMainQueue();
    [self installLibrary];
}

- (void)installLibrary {

    RCTCxxBridge *cxxBridge = (RCTCxxBridge *)self.bridge;

    if (!cxxBridge.runtime) {

        /**
         * This is a workaround to install library
         * as soon as runtime becomes available and is
         * not recommended. If you see random crashes in iOS
         * global.xxx not found etc. use this.
         */

        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, 0.001 * NSEC_PER_SEC),
                       dispatch_get_main_queue(), ^{
                         /**
                          When refreshing the app while debugging, the setBridge
                          method is called too soon. The runtime is not ready yet
                          quite often. We need to install library as soon as runtime
                          becomes available.
                          */
                         [self installLibrary];
                       });
        return;
    }

    install(*(facebook::jsi::Runtime *)cxxBridge.runtime, self);
}

- (void)setRoot:(NSDictionary *)layout commandId:(NSString *)commandId {
    //	sleep(3);
    //    __block NSString *c;
    RCTExecuteOnMainQueue(^{
      [_commandsHandler setRoot:layout
                      commandId:commandId
                     completion:^(NSString *_Nonnull componentId){
                     }];
    });
}

- (void)mergeOptions:(NSString *)componentId options:(NSDictionary *)mergeOptions {
    RCTExecuteOnMainQueue(^{
      [_commandsHandler mergeOptions:componentId
                             options:mergeOptions
                          completion:^{

                          }];
    });
}

- (void)push:(NSString *)componentId commandId:(NSString *)commandId layout:(NSDictionary *)layout {
    RCTExecuteOnMainQueue(^{
      [_commandsHandler push:componentId
                   commandId:commandId
                      layout:layout
                  completion:^(NSString *_Nonnull componentId) {

                  }
                   rejection:^(NSString *code, NSString *message, NSError *error){

                   }];
    });
}

static void install(jsi::Runtime &jsiRuntime, SimpleJsi *simpleJsi) {
    auto setRoot = Function::createFromHostFunction(
        jsiRuntime, PropNameID::forAscii(jsiRuntime, "setRoot"), 2,
        [simpleJsi](Runtime &runtime, const Value &thisValue, const Value *arguments,
                    size_t count) -> Value {
            NSString *commandId =
                convertJSIStringToNSString(runtime, arguments[0].getString(runtime));
            NSDictionary *layout =
                convertJSIObjectToNSDictionary(runtime, arguments[1].asObject(runtime));
            [simpleJsi setRoot:layout commandId:commandId];

            return Value(runtime, convertNSStringToJSIString(runtime, @"componentId"));
        });

    jsiRuntime.global().setProperty(jsiRuntime, "setRoot", move(setRoot));

    auto mergeOptions = Function::createFromHostFunction(
        jsiRuntime, PropNameID::forAscii(jsiRuntime, "mergeOptions"), 2,
        [simpleJsi](Runtime &runtime, const Value &thisValue, const Value *arguments,
                    size_t count) -> Value {
            //        NSString *commandId = convertJSIStringToNSString(runtime,
            //        arguments[0].getString(runtime));
            NSString *componentId =
                convertJSIStringToNSString(runtime, arguments[0].getString(runtime));
            NSDictionary *options =
                convertJSIObjectToNSDictionary(runtime, arguments[1].asObject(runtime));
            [simpleJsi mergeOptions:componentId options:options];

            return Value(runtime, convertNSStringToJSIString(runtime, @"componentId"));
        });

    jsiRuntime.global().setProperty(jsiRuntime, "mergeOptions", move(mergeOptions));

    auto push = Function::createFromHostFunction(
        jsiRuntime, PropNameID::forAscii(jsiRuntime, "push"), 3,
        [simpleJsi](Runtime &runtime, const Value &thisValue, const Value *arguments,
                    size_t count) -> Value {
            NSString *commandId =
                convertJSIStringToNSString(runtime, arguments[0].getString(runtime));
            NSString *componentId =
                convertJSIStringToNSString(runtime, arguments[1].getString(runtime));
            NSDictionary *layout =
                convertJSIObjectToNSDictionary(runtime, arguments[2].asObject(runtime));
            [simpleJsi push:componentId commandId:commandId layout:layout];

            return Value(runtime, convertNSStringToJSIString(runtime, @"componentId"));
        });

    jsiRuntime.global().setProperty(jsiRuntime, "push", move(push));
}

@end
