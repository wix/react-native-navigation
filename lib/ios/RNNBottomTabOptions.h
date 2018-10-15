#import "RNNOptions.h"

@interface RNNBottomTabOptions : RNNOptions

@property (nonatomic) NSUInteger tag;
@property (nonatomic, strong) String* text;
@property (nonatomic, strong) String* badge;
@property (nonatomic, strong) String* fontFamily;
@property (nonatomic, strong) String* testID;
@property (nonatomic, strong) Color* badgeColor;
@property (nonatomic, strong) Image* icon;
@property (nonatomic, strong) Image* selectedIcon;
@property (nonatomic, strong) Color* iconColor;
@property (nonatomic, strong) Color* selectedIconColor;
@property (nonatomic, strong) Color* selectedTextColor;
@property (nonatomic, strong) Dictionary* iconInsets;
@property (nonatomic, strong) Color* textColor;
@property (nonatomic, strong) Number* fontSize;
@property (nonatomic, strong) Bool* visible;


@end
