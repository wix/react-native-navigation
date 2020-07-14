#import "RNNComponentView.h"
#import "RCTHelpers.h"

@implementation RNNComponentView

- (void)layoutSubviews {
    [super layoutSubviews];
    #ifdef DEBUG
        [RCTHelpers removeYellowBox:self];
    #endif
}

@end
