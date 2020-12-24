#import "RNNButtonOptions.h"
#import "RNNUIBarButtonItem.h"
#import "UIViewController+LayoutProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNButtonBuilder : NSObject

- (instancetype)initWithComponentRegistry:(id)componentRegistry;

- (RNNUIBarButtonItem *)build:(RNNButtonOptions *)button onPress:(RNNButtonPressCallback)onPress;

- (void)bindViewController:(UIViewController<RNNLayoutProtocol> *)viewController;

@end
