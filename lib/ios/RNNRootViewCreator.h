
#import <UIKit/UIKit.h>


@protocol RNNRootViewCreator

-(UIView*)createRootView:(NSString*)name rootViewId:(NSString*)rootViewId;

@end

