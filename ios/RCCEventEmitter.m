//
//  RCCEventEmitter.m
//  ReactNativeNavigation
//
//  Created by Daniel Maly on 22/12/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import <RCTEventEmitter.h>


@interface RCCEventEmitter: RCTEventEmitter <RCTBridgeModule,UIAlertViewDelegate>
@end

@implementation RCCEventEmitter

RCT_EXPORT_MODULE();

- (NSArray<NSString*> *)supportedEvents {
    return @[@"TabBarMiddleButtonClicked"];
}


- (void)startObserving {
    for (NSString *event in [self supportedEvents]) {
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(handleNotification:)
                                                     name:event
                                                   object:nil];
    }
}

- (void)stopObserving {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

# pragma mark Public

+ (void)tabBarMiddleButtonClicked:(id) sender {
    [self postNotificationName:@"TabBarMiddleButtonClicked" withPayload:nil];
}


# pragma mark Private

+ (void)postNotificationName:(NSString *)name withPayload:(NSObject *)object {
    NSDictionary<NSString *, id> *payload = object ? @{@"payload": object} : @{};
    
    [[NSNotificationCenter defaultCenter] postNotificationName:name
                                                        object:self
                                                      userInfo:payload];
}

- (void)handleNotification:(NSNotification *)notification {
    [self sendEventWithName:notification.name body:notification.userInfo];
}


@end
