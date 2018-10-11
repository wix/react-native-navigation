#import "RNNOptions.h"

@interface RNNBottomTabOptions : RNNOptions

@property (nonatomic) NSUInteger tag;
@property (nonatomic, strong) String* text;
@property (nonatomic, strong) String* badge;
@property (nonatomic, strong) String* fontFamily;
@property (nonatomic, strong) String* testID;
@property (nonatomic, strong) Dictionary* badgeColor;
@property (nonatomic, strong) Dictionary* icon;
@property (nonatomic, strong) Dictionary* selectedIcon;
@property (nonatomic, strong) Dictionary* iconColor;
@property (nonatomic, strong) Dictionary* selectedIconColor;
@property (nonatomic, strong) Dictionary* selectedTextColor;
@property (nonatomic, strong) Dictionary* iconInsets;
@property (nonatomic, strong) Number* textColor;
@property (nonatomic, strong) Number* fontSize;
@property (nonatomic, strong) Bool* visible;


@end
