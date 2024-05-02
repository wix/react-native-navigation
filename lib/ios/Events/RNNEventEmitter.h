#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <rnnavigation/rnnavigation.h>
#endif

@class RCTHost;

@interface RNNEventEmitter : RCTEventEmitter
#ifdef RCT_NEW_ARCH_ENABLED
                            <NativeRNNEventEmitterSpec>

- (void)setHost:(RCTHost *)host;

@property(nonatomic, strong, readonly) RCTHost *host;
#endif

- (void)sendOnAppLaunched;

- (void)sendComponentWillAppear:(NSString *)componentId
                  componentName:(NSString *)componentName
                  componentType:(NSString *)componentType;

- (void)sendComponentDidAppear:(NSString *)componentId
                 componentName:(NSString *)componentName
                 componentType:(NSString *)componentType;

- (void)sendComponentDidDisappear:(NSString *)componentId
                    componentName:(NSString *)componentName
                    componentType:(NSString *)componentType;

- (void)sendOnNavigationButtonPressed:(NSString *)componentId buttonId:(NSString *)buttonId;

- (void)sendBottomTabSelected:(NSNumber *)selectedTabIndex
                   unselected:(NSNumber *)unselectedTabIndex;

- (void)sendBottomTabLongPressed:(NSNumber *)selectedTabIndex;

- (void)sendOnNavigationCommandCompletion:(NSString *)commandName commandId:(NSString *)commandId;

- (void)sendOnSearchBarUpdated:(NSString *)componentId
                          text:(NSString *)text
                     isFocused:(BOOL)isFocused;

- (void)sendOnSearchBarCancelPressed:(NSString *)componentId;

- (void)sendOnPreviewCompleted:(NSString *)componentId
            previewComponentId:(NSString *)previewComponentId;

- (void)sendModalsDismissedEvent:(NSString *)componentId
         numberOfModalsDismissed:(NSNumber *)modalsDismissed;

- (void)sendModalAttemptedToDismissEvent:(NSString *)componentId;

- (void)sendScreenPoppedEvent:(NSString *)componentId;

- (void)sendBottomTabPressed:(NSNumber *)tabIndex;

@end
