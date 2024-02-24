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

@property(nonatomic, strong) RNNBool *visible;
@property(nonatomic, strong) RNNBool *hideOnScroll;
@property(nonatomic, strong) RNNColor *leftButtonColor;
@property(nonatomic, strong) RNNColor *rightButtonColor;
@property(nonatomic, strong) RNNColor *leftButtonDisabledColor;
@property(nonatomic, strong) RNNColor *rightButtonDisabledColor;
@property(nonatomic, strong) RNNColor *leftButtonBackgroundColor;
@property(nonatomic, strong) RNNColor *rightButtonBackgroundColor;
@property(nonatomic, strong) RNNBool *drawBehind;
@property(nonatomic, strong) RNNBool *noBorder;
@property(nonatomic, strong) RNNColor *borderColor;
@property(nonatomic, strong) RNNBool *animate;
@property(nonatomic, strong) RNNBool *animateLeftButtons;
@property(nonatomic, strong) RNNBool *animateRightButtons;
@property(nonatomic, strong) RNNSearchBarOptions *searchBar;
@property(nonatomic, strong) RNNBool *searchBarHiddenWhenScrolling;
@property(nonatomic, strong) RNNBool *hideNavBarOnFocusSearchBar;
@property(nonatomic, strong) RNNText *testID;
@property(nonatomic, strong) RNNText *barStyle;
@property(nonatomic, strong) RNNText *searchBarPlaceholder;
@property(nonatomic, strong) RNNColor *searchBarBackgroundColor;
@property(nonatomic, strong) RNNColor *searchBarTintColor;
@property(nonatomic, strong) RNNLargeTitleOptions *largeTitle;
@property(nonatomic, strong) RNNTitleOptions *title;
@property(nonatomic, strong) RNNSubtitleOptions *subtitle;
@property(nonatomic, strong) RNNBackgroundOptions *background;
@property(nonatomic, strong) RNNScrollEdgeAppearanceOptions *scrollEdgeAppearance;
@property(nonatomic, strong) RNNBackButtonOptions *backButton;

- (BOOL)shouldDrawBehind;

@end
