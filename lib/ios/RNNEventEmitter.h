#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTBridgeModule.h>

@interface RNNEventEmitter : RCTEventEmitter <RCTBridgeModule>

- (void)sendOnAppLaunched;

- (void)sendComponentDidAppear:(NSString*)componentId componentName:(NSString*)componentName componentType:(NSString *)componentType;

- (void)sendComponentDidDisappear:(NSString *)componentId componentName:(NSString *)componentName componentType:(NSString *)componentType;

- (void)sendOnNavigationButtonPressed:(NSString*)componentId buttonId:(NSString*)buttonId;

- (void)sendBottomTabSelected:(NSNumber *)selectedTabIndex unselected:(NSNumber*)unselectedTabIndex;

- (void)sendBottomTabLongPressed:(NSNumber *)selectedTabIndex;

- (void)sendOnNavigationCommandCompletion:(NSString *)commandName commandId:(NSString *)commandId params:(NSDictionary*)params;

- (void)sendOnSearchBarUpdated:(NSString *)componentId text:(NSString*)text isFocused:(BOOL)isFocused;

- (void)sendOnSearchBarCancelPressed:(NSString *)componentId;

- (void)sendOnPreviewCompleted:(NSString *)componentId previewComponentId:(NSString *)previewComponentId;

- (void)sendModalsDismissedEvent:(NSString *)componentId componentName:(NSString *)componentName numberOfModalsDismissed:(NSNumber *)modalsDismissed;

- (void)sendModalAttemptedToDismissEvent:(NSString *)componentId;

- (void)sendScreenPoppedEvent:(NSString *)componentId;

- (void)sendBottomTabPressed:(NSNumber *)tabIndex;


@end
