#import "RNNOptions.h"

@interface RNNBottomTabOptions : RNNOptions

@property (nonatomic) NSUInteger tag;
@property (nonatomic, strong) NSString* text;
@property (nonatomic, strong) NSString* badge;
@property (nonatomic, strong) NSDictionary* badgeColor;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSDictionary* icon;
@property (nonatomic, strong) NSDictionary* selectedIcon;
@property (nonatomic, strong) NSDictionary* iconColor;
@property (nonatomic, strong) NSDictionary* selectedIconColor;
@property (nonatomic, strong) NSDictionary* textColor;
@property (nonatomic, strong) NSDictionary* selectedTextColor;
@property (nonatomic, strong) NSString* fontFamily;
@property (nonatomic, strong) NSNumber* fontSize;

@property (nonatomic, strong) NSDictionary* iconInsets;

@end
