#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewController.h"
#import "RNNButtonOptions.h"

@interface RNNNavigationButtons : NSObject

-(instancetype)initWithViewController:(RNNRootViewController*)viewController;

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons defaultLeftButtonStyle:(RNNButtonOptions *)defaultLeftButtonStyle defaultRightButtonStyle:(RNNButtonOptions *)defaultRightButtonStyle;

@end


