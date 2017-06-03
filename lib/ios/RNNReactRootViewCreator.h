#import <Foundation/Foundation.h>
#import "RNNRootViewCreator.h"

#import <React/RCTBridge.h>

@interface RNNReactRootViewCreator : NSObject <RNNRootViewCreator>

-(instancetype)initWithBridge:(RCTBridge*)bridge;

@end
