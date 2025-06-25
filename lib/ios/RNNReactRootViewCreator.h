#import <Foundation/Foundation.h>
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import <React/RCTBridge.h>

@interface RNNReactRootViewCreator : NSObject <RNNComponentViewCreator>

- (instancetype)initWithBridge:(RCTBridge *)bridge eventEmitter:(RNNEventEmitter *)eventEmitter;

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithHost:(RCTHost *)host eventEmitter:(RNNEventEmitter *)eventEmitter;
#endif

@end
