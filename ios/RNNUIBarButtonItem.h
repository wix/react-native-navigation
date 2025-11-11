#import "RNNButtonOptions.h"
#import "RNNIconCreator.h"
#import "RNNReactComponentRegistry.h"
#import <Foundation/Foundation.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>

typedef void (^RNNButtonPressCallback)(NSString *buttonId);

@interface RNNUIBarButtonItem : UIBarButtonItem <
#ifdef RCT_NEW_ARCH_ENABLED
RCTSurfaceDelegate
#else
RCTRootViewDelegate
#endif
>

@property(nonatomic, strong) NSString *buttonId;

- (instancetype)initCustomIcon:(RNNButtonOptions *)buttonOptions
                   iconCreator:(RNNIconCreator *)iconCreator
                       onPress:(RNNButtonPressCallback)onPress;
- (instancetype)initWithSFSymbol:(RNNButtonOptions *)buttonOptions
                         onPress:(RNNButtonPressCallback)onPress;
- (instancetype)initWithIcon:(RNNButtonOptions *)buttonOptions
                     onPress:(RNNButtonPressCallback)onPress;
- (instancetype)initWithTitle:(RNNButtonOptions *)buttonOptions
                      onPress:(RNNButtonPressCallback)onPress;
- (instancetype)initWithCustomView:(RNNReactView *)reactView
                     buttonOptions:(RNNButtonOptions *)buttonOptions
                           onPress:(RNNButtonPressCallback)onPress;
- (instancetype)initWithSystemItem:(RNNButtonOptions *)buttonOptions
                           onPress:(RNNButtonPressCallback)onPress;

- (void)mergeColor:(Color *)color;
- (void)mergeBackgroundColor:(Color *)color;

- (void)notifyWillAppear;
- (void)notifyDidAppear;
- (void)notifyDidDisappear;

@end
