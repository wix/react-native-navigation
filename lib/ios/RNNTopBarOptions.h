#import "RNNBackButtonOptions.h"
#import "RNNBackgroundOptions.h"
#import "RNNButtonOptions.h"
#import "RNNComponentOptions.h"
#import "RNNLargeTitleOptions.h"
#import "RNNOptions.h"
#import "RNNScrollEdgeAppearanceOptions.h"
#import "RNNSearchBarOptions.h"
#import "RNNSubtitleOptions.h"
#import "RNNTitleOptions.h"

@interface RNNTopBarOptions : RNNOptions

@property(nonatomic, strong) NSArray<RNNButtonOptions *> *leftButtons;
@property(nonatomic, strong) NSArray<RNNButtonOptions *> *rightButtons;

@property(nonatomic, strong) Bool *visible;
@property(nonatomic, strong) Bool *hideOnScroll;
@property(nonatomic, strong) Color *leftButtonColor;
@property(nonatomic, strong) Color *rightButtonColor;
@property(nonatomic, strong) Color *leftButtonDisabledColor;
@property(nonatomic, strong) Color *rightButtonDisabledColor;
@property(nonatomic, strong) Bool *drawBehind;
@property(nonatomic, strong) Bool *noBorder;
@property(nonatomic, strong) Color *borderColor;
@property(nonatomic, strong) Bool *animate;
@property(nonatomic, strong) Bool *animateLeftButtons;
@property(nonatomic, strong) Bool *animateRightButtons;
@property(nonatomic, strong) RNNSearchBarOptions *searchBar;
@property(nonatomic, strong) Bool *searchBarHiddenWhenScrolling;
@property(nonatomic, strong) Bool *hideNavBarOnFocusSearchBar;
@property(nonatomic, strong) Text *testID;
@property(nonatomic, strong) Text *barStyle;
@property(nonatomic, strong) Text *searchBarPlaceholder;
@property(nonatomic, strong) Color *searchBarBackgroundColor;
@property(nonatomic, strong) Color *searchBarTintColor;
@property(nonatomic, strong) RNNLargeTitleOptions *largeTitle;
@property(nonatomic, strong) RNNTitleOptions *title;
@property(nonatomic, strong) RNNSubtitleOptions *subtitle;
@property(nonatomic, strong) RNNBackgroundOptions *background;
@property(nonatomic, strong) RNNScrollEdgeAppearanceOptions *scrollEdgeAppearance;
@property(nonatomic, strong) RNNBackButtonOptions *backButton;

- (BOOL)shouldDrawBehind;

@end
