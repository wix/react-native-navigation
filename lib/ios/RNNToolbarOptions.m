#import "RNNToolbarOptions.h"
#import "RNNButtonOptions.h"
#import "UIViewController+RNNOptions.h"

@interface RNNToolbarOptions ()

@end

@implementation RNNToolbarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
    self = [super init];
    self.buttons = dict[@"buttons"];
    self.buttonStyle = [[RNNButtonOptions alloc] initWithDict:dict[@"buttonStyle"]];
    return self;
}

@end