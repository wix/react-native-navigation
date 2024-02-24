#import "RNNBottomTabsBaseAttacher.h"
#import "RNNNavigationOptions.h"
#import <Foundation/Foundation.h>

@interface RNNBottomTabsAttachModeFactory : NSObject

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (RNNBottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options;

@property(nonatomic, retain) RNNNavigationOptions *defaultOptions;

@end
