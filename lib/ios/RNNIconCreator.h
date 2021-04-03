#import "RNNButtonOptions.h"
#import "RNNIconDrawer.h"
#import <Foundation/Foundation.h>

@interface RNNIconCreator : NSObject

- (instancetype)initWithIconDrawer:(RNNIconDrawer *)iconDrawer;

- (UIImage *)create:(RNNButtonOptions *)buttonOptions;

@end
