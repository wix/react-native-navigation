#import "RNNOptions.h"
#import "RNNSheetDetentOptions.h"
#import "Text.h"
#import <UIKit/UIKit.h>

@interface RNNModalOptions : RNNOptions

@property(nonatomic, strong) Bool *swipeToDismiss;
@property(nonatomic, copy) NSArray<RNNSheetDetentOptions *> *detents;
@property(nonatomic, strong) Text *selectedDetent;
@property(nonatomic, strong) Text *largestUndimmedDetent;
@property(nonatomic, strong) Bool *prefersGrabberVisible;

- (BOOL)hasSheetPresentationOptions;
- (void)applySheetPresentationToViewController:(UIViewController *)viewController;

@end
