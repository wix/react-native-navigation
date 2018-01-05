#import "RNNOptions.h"

@interface RNNBottomTabOptions : RNNOptions

@property (nonatomic) NSUInteger tag;
@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* badge;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSDictionary* icon;

@end
