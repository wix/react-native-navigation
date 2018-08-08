#import "RNNOptions.h"
#import "RNNLargeTitleOptions.h"
#import "RNNTitleOptions.h"
#import "RNNSubtitleOptions.h"
#import "RNNBackgroundOptions.h"
#import "RNNComponentOptions.h"
#import "RNNBackButtonOptions.h"
#import "RNNButtonOptions.h"

@interface RNNTopBarOptions : RNNOptions

@property (nonatomic, strong) NSArray* leftButtons;
@property (nonatomic, strong) NSArray* rightButtons;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSNumber* hideOnScroll;
@property (nonatomic, strong) NSNumber* leftButtonColor;
@property (nonatomic, strong) NSNumber* rightButtonColor;
@property (nonatomic, strong) NSNumber* leftButtonDisabledColor;
@property (nonatomic, strong) NSNumber* rightButtonDisabledColor;
@property (nonatomic, strong) NSString* barStyle;
@property (nonatomic, strong) NSNumber* translucent;
@property (nonatomic, strong) NSNumber* transparent;
@property (nonatomic, strong) NSNumber* drawBehind;
@property (nonatomic, strong) NSNumber* noBorder;
@property (nonatomic, strong) NSNumber* blur;
@property (nonatomic, strong) NSNumber* animate;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) RNNLargeTitleOptions* largeTitle;
@property (nonatomic, strong) RNNTitleOptions* title;
@property (nonatomic, strong) RNNSubtitleOptions* subtitle;
@property (nonatomic, strong) RNNBackgroundOptions* background;
@property (nonatomic, strong) RNNBackButtonOptions* backButton;
@property (nonatomic, strong) RNNButtonOptions* leftButtonStyle;
@property (nonatomic, strong) RNNButtonOptions* rightButtonStyle;
@property (nonatomic, strong) NSNumber* searchBar;
@property (nonatomic, strong) NSNumber* searchBarHiddenWhenScrolling;
@property (nonatomic, strong) NSString* searchBarPlaceholder;

@property (nonatomic, strong) RNNComponentOptions* component;

@end
