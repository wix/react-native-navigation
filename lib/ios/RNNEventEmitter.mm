#import "RNNEventEmitter.h"
#import "RNNUtils.h"
#import "RNNTurboEventEmitter.h"
#import <React-RuntimeApple/ReactCommon/RCTHost.h>

@implementation RNNEventEmitter { }

- (void)setHost:(RCTHost *)host {
  if (_host != nil) {
    return;
  }
  _host = host;
}

#pragma mark public

- (void)sendOnAppLaunched {
    [self send:AppLaunched body:nil];
}

- (void)sendComponentWillAppear:(NSString *)componentId
                  componentName:(NSString *)componentName
                  componentType:(NSString *)componentType {
    [self send:ComponentWillAppear
          body:@{
              @"componentId" : componentId,
              @"componentName" : componentName,
              @"componentType" : componentType
          }];
}

- (void)sendComponentDidAppear:(NSString *)componentId
                 componentName:(NSString *)componentName
                 componentType:(NSString *)componentType {
    [self send:ComponentDidAppear
          body:@{
              @"componentId" : componentId,
              @"componentName" : componentName,
              @"componentType" : componentType
          }];
}

- (void)sendComponentDidDisappear:(NSString *)componentId
                    componentName:(NSString *)componentName
                    componentType:(NSString *)componentType {
    [self send:ComponentDidDisappear
          body:@{
              @"componentId" : componentId,
              @"componentName" : componentName,
              @"componentType" : componentType
          }];
}

- (void)sendOnNavigationButtonPressed:(NSString *)componentId buttonId:(NSString *)buttonId {
    [self send:NavigationButtonPressed
          body:@{@"componentId" : componentId, @"buttonId" : buttonId}];
}

- (void)sendBottomTabSelected:(NSNumber *)selectedTabIndex
                   unselected:(NSNumber *)unselectedTabIndex {
    [self
        send:BottomTabSelected
        body:@{@"selectedTabIndex" : selectedTabIndex, @"unselectedTabIndex" : unselectedTabIndex}];
}

- (void)sendBottomTabLongPressed:(NSNumber *)selectedTabIndex {
    [self send:BottomTabLongPressed body:@{@"selectedTabIndex" : selectedTabIndex}];
}

- (void)sendBottomTabPressed:(NSNumber *)tabIndex {
    [self send:BottomTabPressed body:@{@"tabIndex" : tabIndex}];
}

- (void)sendOnNavigationCommandCompletion:(NSString *)commandName commandId:(NSString *)commandId {
    [self send:CommandCompleted
          body:@{
              @"commandId" : commandId,
              @"commandName" : commandName,
              @"completionTime" : [RNNUtils getCurrentTimestamp]
          }];
}

- (void)sendOnSearchBarUpdated:(NSString *)componentId
                          text:(NSString *)text
                     isFocused:(BOOL)isFocused {
    [self send:SearchBarUpdated
          body:@{@"componentId" : componentId, @"text" : text, @"isFocused" : @(isFocused)}];
}

- (void)sendOnSearchBarCancelPressed:(NSString *)componentId {
    [self send:SearchBarCancelPressed body:@{@"componentId" : componentId}];
}

- (void)sendOnPreviewCompleted:(NSString *)componentId
            previewComponentId:(NSString *)previewComponentId {
    [self send:PreviewCompleted
          body:@{@"componentId" : componentId, @"previewComponentId" : previewComponentId}];
}

- (void)sendModalsDismissedEvent:(NSString *)componentId
         numberOfModalsDismissed:(NSNumber *)modalsDismissed {
    if (componentId) {
        [self send:ModalDismissed
              body:@{@"componentId" : componentId, @"modalsDismissed" : modalsDismissed}];
    }
}

- (void)sendModalAttemptedToDismissEvent:(NSString *)componentId {
    [self send:ModalAttemptedToDismiss
          body:@{
              @"componentId" : componentId,
          }];
}

- (void)sendScreenPoppedEvent:(NSString *)componentId {
    [self send:ScreenPopped body:@{@"componentId" : componentId}];
}

#pragma mark private

- (void)send:(NSString *)eventName body:(id)body {
  if (_host == nil) {
    return;
  }
  
  RNNTurboEventEmitter *_eventEmitter = [[_host moduleRegistry] moduleForName:"RNNTurboEventEmitter"];
  if (_eventEmitter == nil) {
    return;
  }
  
  [_eventEmitter send:eventName body:body];
}

@end
