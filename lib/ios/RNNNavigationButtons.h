#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNButtonOptions.h"
#import "RNNRootViewCreator.h"

@interface RNNNavigationButtons : NSObject

-(instancetype)initWithViewController:(UIViewController*)viewController rootViewCreator:(id<RNNRootViewCreator>)creator;

-(void)applyLeftButtons:(NSArray*)leftButtons rightButtons:(NSArray*)rightButtons defaultLeftButtonStyle:(RNNButtonOptions *)defaultLeftButtonStyle defaultRightButtonStyle:(RNNButtonOptions *)defaultRightButtonStyle;

@end


