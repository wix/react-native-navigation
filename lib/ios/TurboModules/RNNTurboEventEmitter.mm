#ifdef RCT_NEW_ARCH_ENABLED
#import "RNNTurboEventEmitter.h"
#import "RNNEmitterConstants.h"
#import "RNNUtils.h"

@implementation RNNTurboEventEmitter {
  NSInteger _appLaunchedListenerCount;
  BOOL _appLaunchedEventDeferred;
}

RCT_EXPORT_MODULE()

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
	(const facebook::react::ObjCTurboModule::InitParams &)params
{
	return std::make_shared<facebook::react::NativeRNNTurboEventEmitterSpecJSI>(params);
}

- (void)addListener:(NSString *)eventName {
  [super addListener:eventName];
  if ([eventName isEqualToString:AppLaunched]) {
    _appLaunchedListenerCount++;
    if (_appLaunchedEventDeferred) {
      _appLaunchedEventDeferred = FALSE;
      [self sendEventWithName:AppLaunched body:nil];
    }
  }
}

- (NSArray<NSString *> *)supportedEvents {
  return @[
    AppLaunched, CommandCompleted, BottomTabSelected, BottomTabLongPressed, BottomTabPressed,
    ComponentWillAppear, ComponentDidAppear, ComponentDidDisappear, NavigationButtonPressed,
    ModalDismissed, SearchBarUpdated, SearchBarCancelPressed, PreviewCompleted, ScreenPopped,
    ModalAttemptedToDismiss
  ];
}

- (void)send:(NSString *)eventName body:(id)body {
  if ([eventName isEqualToString:AppLaunched]) {
    if (_appLaunchedListenerCount == 0) {
      _appLaunchedEventDeferred = TRUE;
      return;
    }
  }
  
  [self sendEventWithName:eventName body:body];
}

@end
#endif
