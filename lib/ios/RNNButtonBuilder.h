#import "RNNButtonOptions.h"
#import "RNNUIBarButtonItem.h"
#import "UIViewController+LayoutProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNButtonBuilder : NSObject

- (instancetype)initWithViewController:(UIViewController<RNNLayoutProtocol> *)viewController
                     componentRegistry:(id)componentRegistry;

- (RNNUIBarButtonItem *)build:(RNNButtonOptions *)button;

@end
