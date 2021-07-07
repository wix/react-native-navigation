#import "RNNOptions.h"

@interface RNNBottomTabBadgeOptions : RNNOptions

@property(nonatomic, strong) Text *text;
@property(nonatomic, strong) Color *textColor;
@property(nonatomic, strong) Color *backgroundColor;

- (BOOL)hasValue;

+ (RNNBottomTabBadgeOptions *)parse:(NSDictionary *)dict;

@end
