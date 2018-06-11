
#import <Foundation/Foundation.h>

#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

@interface RNNEventEmitter : RCTEventEmitter <RCTBridgeModule>

-(void)sendOnAppLaunched;

-(void)sendComponentDidAppear:(NSString*)componentId componentName:(NSString*)componentName;

-(void)sendComponentDidDisappear:(NSString*)componentId componentName:(NSString*)componentName;

-(void)sendOnNavigationButtonPressed:(NSString*)componentId buttonId:(NSString*)buttonId;

-(void)sendOnNavigationEvent:(NSString *)commandName params:(NSDictionary*)params;

-(void)sendOnNavigationCommand:(NSString *)commandName params:(NSDictionary*)params;

-(void)sendOnNavigationCommandCompletion:(NSString *)commandName params:(NSDictionary*)params;

-(void)sendOnSearchBarUpdated:(NSString *)componentId text:(NSString*)text isFocused:(BOOL)isFocused;

@end
