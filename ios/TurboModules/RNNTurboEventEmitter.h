#ifdef RCT_NEW_ARCH_ENABLED
#import "RNNEventEmitter.h"
#import <rnnavigation/rnnavigation.h>

typedef NSString * EmitterEvents NS_STRING_ENUM;

extern EmitterEvents const AppLaunched;
extern EmitterEvents const CommandCompleted;
extern EmitterEvents const BottomTabSelected;
extern EmitterEvents const BottomTabLongPressed;
extern EmitterEvents const ComponentWillAppear;
extern EmitterEvents const ComponentDidAppear;
extern EmitterEvents const ComponentDidDisappear;
extern EmitterEvents const NavigationButtonPressed;
extern EmitterEvents const ModalDismissed;
extern EmitterEvents const ModalAttemptedToDismiss;
extern EmitterEvents const SearchBarUpdated;
extern EmitterEvents const SearchBarCancelPressed;
extern EmitterEvents const PreviewCompleted;
extern EmitterEvents const ScreenPopped;
extern EmitterEvents const BottomTabPressed;

@interface RNNTurboEventEmitter : RCTEventEmitter <NativeRNNTurboEventEmitterSpec>
- (void)send:(NSString *)eventName body:(id)body;
@end
#endif
