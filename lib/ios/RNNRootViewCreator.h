
#import <UIKit/UIKit.h>
#import "RNNComponentOptions.h"
#import "RNNReactView.h"

@protocol RNNRootViewCreator

- (RNNReactView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId availableSize:(CGSize)availableSize reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

- (UIView*)createRootViewFromComponentOptions:(RNNComponentOptions*)componentOptions;

- (UIView*)createRootViewFromComponentOptions:(RNNComponentOptions*)componentOptions reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock;

@end

