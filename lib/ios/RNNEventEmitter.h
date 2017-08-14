
#import <Foundation/Foundation.h>

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

@interface RNNEventEmitter : RCTEventEmitter <RCTBridgeModule>

-(void)sendOnAppLaunched;

-(void)sendContainerDidAppear:(NSString*)containerId;

-(void)sendContainerDidDisappear:(NSString*)containerId;

@end
