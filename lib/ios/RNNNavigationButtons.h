#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNRootViewController.h"

@interface RNNNavigationButtons : NSObject

-(instancetype)init;
-(instancetype)initWithViewController:(RNNRootViewController*)viewController;

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons;

@end


