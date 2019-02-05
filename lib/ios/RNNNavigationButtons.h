#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNButtonOptions.h"
#import "RNNRootViewCreator.h"
#import "RNNReactComponentManager.h"

@interface RNNNavigationButtons : NSObject

-(instancetype)initWithViewController:(UIViewController*)viewController componentManager:(RNNReactComponentManager *)componentManager;

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons defaultLeftButtonStyle:(RNNButtonOptions *)defaultLeftButtonStyle defaultRightButtonStyle:(RNNButtonOptions *)defaultRightButtonStyle;

@end


