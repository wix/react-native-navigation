#import <Foundation/Foundation.h>
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import <React/RCTBridge.h>

@interface RNNReactRootViewCreator : NSObject <RNNComponentViewCreator>

-(instancetype)initWithBridge:(RCTBridge*)bridge eventEmitter:(RNNEventEmitter*)eventEmitter;

@end
