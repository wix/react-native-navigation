
#import <Foundation/Foundation.h>

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

@interface RNNEventEmitter : RCTEventEmitter <RCTBridgeModule>

-(void)sendOnAppLaunched;

-(void)sendComponentDidAppear:(NSString*)componentId;

-(void)sendComponentDidDisappear:(NSString*)componentId;

-(void)sendOnNavigationButtonPressed:(NSString*)componentId buttonId:(NSString*)buttonId;

@end
