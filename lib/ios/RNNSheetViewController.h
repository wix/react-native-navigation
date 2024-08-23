#import "RNNComponentViewController.h"
#import <React/RCTScrollView.h>

static NSString *const NavigationLayoutElementHeaderID = @"NavigationLayoutHeader";
static NSString *const NavigationLayoutElementContentID = @"NavigationLayoutContent";
static NSString *const NavigationLayoutElementFooterID = @"NavigationLayoutFooter";

@interface RNNSheetViewController : RNNComponentViewController

@property(nonatomic, strong) UIView *containerView;
@property(nonatomic, strong) UIView *backdrop;
@property(nonatomic, strong) UIView *bottomCompensator;
@property(nonatomic, assign) CGRect containerFrame;

@property(nonatomic, weak) RCTScrollView *rctScrollView;
@property(nonatomic, weak) UIView *contentView;
@property(nonatomic, weak) UIView *headerView;
@property(nonatomic, weak) UIView *footerView;
@property(nonatomic, assign) CGFloat cachedHeight;

@property(nonatomic, assign) BOOL isPresented;
@property(nonatomic, assign) BOOL isDragging;
@property(nonatomic, assign) BOOL isMoving;
@property(nonatomic, assign) BOOL isAnimatePresentInProcess;
@property(nonatomic, assign) CGFloat previousTranslation;
@property(nonatomic, assign) CGFloat startTranslationOffset;
@property(nonatomic, assign) CGFloat keyboardHeight;

@property(nonatomic, assign) double backdropOpacity;
@property(nonatomic, assign) NSNumber *cornerTopRadius;

@property(nonatomic, strong) UITapGestureRecognizer *tapGesture;
@property(nonatomic, strong) UIPanGestureRecognizer *panGesture;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                      eventEmitter:(RNNEventEmitter *)eventEmitter
                         presenter:(RNNComponentPresenter *)presenter
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                    viewController:(UIViewController *)viewController;
- (void)dismiss;

- (void)setSheetBackgroundColor:(UIColor *)backgroundColor;

- (void)setupContentViews:(nonnull NSNumber *)headerTag
               contentTag:(nonnull NSNumber *)contentTag
                footerTag:(nonnull NSNumber *)footerTag;

@end
