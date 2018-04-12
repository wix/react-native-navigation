
#import <UIKit/UIKit.h>
#import "RNNComponentOptions.h"

@protocol RNNRootViewCreator

-(UIView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId;

-(UIView*)createRootViewFromComponentOptions:(RNNComponentOptions*)componentOptions;

@end

