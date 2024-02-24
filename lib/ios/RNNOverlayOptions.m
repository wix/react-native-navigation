#import "RNNOverlayOptions.h"
#import <React/RCTRootView.h>

@implementation RNNOverlayOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super initWithDict:dict];

    self.interceptTouchOutside = [RNNBoolParser parse:dict key:@"interceptTouchOutside"];
    self.handleKeyboardEvents = [RNNBoolParser parse:dict key:@"handleKeyboardEvents"];

    return self;
}

- (void)mergeOptions:(RNNOverlayOptions *)options {
    if (options.interceptTouchOutside.hasValue)
        self.interceptTouchOutside = options.interceptTouchOutside;
    if (options.handleKeyboardEvents.hasValue)
        self.handleKeyboardEvents = options.handleKeyboardEvents;
}

@end
