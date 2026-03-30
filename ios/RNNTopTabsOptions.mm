#import "RNNTopTabsOptions.h"
#import "RNNTopTabsViewController.h"

@implementation RNNTopTabsOptions

- (void)mergeOptions:(RNNTopTabsOptions *)options {
    if (options.backgroundColor.hasValue)
        self.backgroundColor = options.backgroundColor;
}

@end
