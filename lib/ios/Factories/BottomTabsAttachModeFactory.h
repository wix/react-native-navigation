#import "BottomTabsBaseAttacher.h"
#import "RNNNavigationOptions.h"
#import <Foundation/Foundation.h>

@interface BottomTabsAttachModeFactory : NSObject

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (BottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options;

@property(nonatomic, retain) RNNNavigationOptions *defaultOptions;

@end
