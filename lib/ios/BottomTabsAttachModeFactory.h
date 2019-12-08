#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"
#import "BottomTabsBaseAttacher.h"

@interface BottomTabsAttachModeFactory : NSObject

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (BottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options;

@property (nonatomic, retain) RNNNavigationOptions* defaultOptions;

@end
