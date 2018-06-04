#import "RNNOptions.h"

@interface RNNBottomTabOptions : RNNOptions

@property (nonatomic) NSUInteger tag;
@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* badge;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSDictionary* icon;
@property (nonatomic, strong) NSDictionary* selectedIcon;
@property (nonatomic, strong) NSDictionary* disableIconTint;
@property (nonatomic, strong) NSDictionary* disableSelectedIconTint;
@property (nonatomic, strong) NSDictionary* iconInsets;

@end
