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

@property (nonatomic, strong) Bool* visible;
@property (nonatomic, strong) Bool* hideOnScroll;
@property (nonatomic, strong) Bool* leftButtonColor;
@property (nonatomic, strong) Bool* rightButtonColor;
@property (nonatomic, strong) Bool* leftButtonDisabledColor;
@property (nonatomic, strong) Bool* rightButtonDisabledColor;
@property (nonatomic, strong) Bool* drawBehind;
@property (nonatomic, strong) Bool* noBorder;
@property (nonatomic, strong) Bool* animate;
@property (nonatomic, strong) Bool* searchBar;
@property (nonatomic, strong) Bool* searchBarHiddenWhenScrolling;
@property (nonatomic, strong) String* testID;
@property (nonatomic, strong) String* barStyle;
@property (nonatomic, strong) String* searchBarPlaceholder;
@property (nonatomic, strong) RNNLargeTitleOptions* largeTitle;
@property (nonatomic, strong) RNNTitleOptions* title;
@property (nonatomic, strong) RNNSubtitleOptions* subtitle;
@property (nonatomic, strong) RNNBackgroundOptions* background;
@property (nonatomic, strong) RNNBackButtonOptions* backButton;
@property (nonatomic, strong) RNNButtonOptions* leftButtonStyle;
@property (nonatomic, strong) RNNButtonOptions* rightButtonStyle;


@property (nonatomic, strong) RNNComponentOptions* component;

@end
