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
@property (nonatomic, strong) Color* leftButtonColor;
@property (nonatomic, strong) Color* rightButtonColor;
@property (nonatomic, strong) Color* leftButtonDisabledColor;
@property (nonatomic, strong) Color* rightButtonDisabledColor;
@property (nonatomic, strong) Bool* drawBehind;
@property (nonatomic, strong) Bool* noBorder;
@property (nonatomic, strong) Bool* animate;
@property (nonatomic, strong) Bool* searchBar;
@property (nonatomic, strong) Bool* searchBarHiddenWhenScrolling;
@property (nonatomic, strong) Bool* hideNavBarOnFocusSearchBar;
@property (nonatomic, strong) Text* testID;
@property (nonatomic, strong) Text* barStyle;
@property (nonatomic, strong) Text* searchBarPlaceholder;
@property (nonatomic, strong) RNNLargeTitleOptions* largeTitle;
@property (nonatomic, strong) RNNTitleOptions* title;
@property (nonatomic, strong) RNNSubtitleOptions* subtitle;
@property (nonatomic, strong) RNNBackgroundOptions* background;
@property (nonatomic, strong) RNNBackButtonOptions* backButton;
@property (nonatomic, strong) RNNButtonOptions* leftButtonStyle;
@property (nonatomic, strong) RNNButtonOptions* rightButtonStyle;

- (BOOL)shouldDrawBehind;

@end
