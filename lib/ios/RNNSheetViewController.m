#import "RNNSheetViewController.h"
#import "AnimationObserver.h"
#import <React/RCTScrollView.h>

@implementation RNNSheetViewController {
    UIViewController *_boundViewController;
    BOOL _stopFindScrollView;
    RNNNavigationOptions *_options;
    CGFloat _delta;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                      eventEmitter:(RNNEventEmitter *)eventEmitter
                         presenter:(RNNComponentPresenter *)presenter
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                    viewController:(UIViewController *)viewController {
    _boundViewController = viewController;

    self = [super initWithLayoutInfo:layoutInfo
                     rootViewCreator:nil
                        eventEmitter:eventEmitter
                           presenter:presenter
                             options:options
                      defaultOptions:defaultOptions];

    _options = options;

    self.isMoving = NO;
    self.isDragging = NO;
    self.previousTranslation = 0;
    self.startTranslationOffset = 0;

    return self;
}

#pragma mark - UIViewController overrides

- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle];
}

- (BOOL)prefersStatusBarHidden {
    return [self.presenter getStatusBarVisibility];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation];
}

- (BOOL)hidesBottomBarWhenPushed {
    return [self.presenter hidesBottomBarWhenPushed];
}

- (void)loadView {
    self.view = [[UIView alloc] initWithFrame:UIScreen.mainScreen.bounds];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.eventEmitter sendComponentWillAppear:self.layoutInfo.componentId
                                 componentName:self.layoutInfo.name
                                 componentType:ComponentTypeScreen];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [[AnimationObserver sharedObserver] endAnimation];
    [self.eventEmitter sendComponentDidAppear:self.layoutInfo.componentId
                                componentName:self.layoutInfo.name
                                componentType:ComponentTypeScreen];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.eventEmitter sendComponentDidDisappear:self.layoutInfo.componentId
                                   componentName:self.layoutInfo.name
                                   componentType:ComponentTypeScreen];

    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setup];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShow:)
                                                 name:UIKeyboardWillShowNotification
                                               object:nil];

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillHide:)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
}

- (void)keyboardWillShow:(NSNotification *)notification {
    NSDictionary *userInfo = [notification userInfo];
    NSValue *keyboardFrameValue = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardFrame = [keyboardFrameValue CGRectValue];
    self.keyboardHeight = keyboardFrame.size.height;

    NSTimeInterval animationDuration =
        [[userInfo objectForKey:UIKeyboardAnimationDurationUserInfoKey] doubleValue];
    UIViewAnimationOptions animationCurve =
        [[userInfo objectForKey:UIKeyboardAnimationCurveUserInfoKey] unsignedIntegerValue] << 16;

    [self.backdrop.layer removeAllAnimations];
    [self.containerView.layer removeAllAnimations];

    CGFloat sheetHeight = [self getSheetHeight];

    CGFloat safeAreaBottom =
        !self.footerView && self.keyboardHeight == 0 ? self.view.safeAreaInsets.bottom : 0;
    CGFloat adjustedHeight = MIN(self.contentMaximumHeight, sheetHeight) + safeAreaBottom;
    self.containerFrame =
        CGRectMake(0, self.view.bounds.size.height - adjustedHeight - self.keyboardHeight,
                   self.view.bounds.size.width, adjustedHeight);

    _boundViewController.view.frame =
        CGRectMake(0, 0, self.containerView.frame.size.width, adjustedHeight);

    self.isAnimatePresentInProcess = YES;

    [UIView animateWithDuration:animationDuration
        delay:0
        options:animationCurve
        animations:^{
          self.containerView.frame = self.containerFrame;
          [self.view layoutIfNeeded];
        }
        completion:^(BOOL finished) {
          if (finished) {
              self.isAnimatePresentInProcess = NO;
          }
        }];

    if (self.rctScrollView) {
        [self.rctScrollView layoutIfNeeded];
    }
}

- (void)keyboardWillHide:(NSNotification *)notification {
    self.keyboardHeight = 0;

    if (self.isPresented) {
        [self.backdrop.layer removeAllAnimations];
        [self.containerView.layer removeAllAnimations];
        [self updateContentSize:[self getSheetHeight]];
    }
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    self.backdrop.frame = self.view.bounds;

    [self setupCornerRadius];
}

- (UINavigationItem *)navigationItem {
    return _boundViewController.navigationItem;
}

- (void)render {
    [self readyForPresentation];
}

- (CGFloat)calcSheetHeight:(CGFloat)contentHeight {
    return contentHeight + (self.headerView ? self.headerView.frame.size.height : 0) +
           (self.footerView ? self.footerView.frame.size.height : 0);
}

- (void)setSheetHeight:(CGFloat)contentHeight {
    CGFloat sheetHeight = [self calcSheetHeight:contentHeight];
    if (sheetHeight > 0 && sheetHeight != self.cachedHeight) {
        if (self.isPresented) {
            [self updateContentSize:sheetHeight];
        } else {
            [self present:sheetHeight];
        }

        if (self.rctScrollView) {
            [self.rctScrollView.scrollView
                setScrollEnabled:sheetHeight >= self.contentMaximumHeight];
        }

        self.cachedHeight = sheetHeight;
    }
}

- (void)setupContentViews:(nonnull NSNumber *)headerTag
               contentTag:(nonnull NSNumber *)contentTag
                footerTag:(nonnull NSNumber *)footerTag {
    RNNReactView *reactView = _boundViewController.reactView;
    RCTUIManager *uiManager = reactView.bridge.uiManager;

    dispatch_async(dispatch_get_main_queue(), ^{
      if (headerTag) {
          self.headerView = [uiManager viewForReactTag:headerTag];
      }

      if (footerTag) {
          self.footerView = [uiManager viewForReactTag:footerTag];
      }

      if (contentTag) {
          UIView *content = [uiManager viewForReactTag:contentTag];
          if (content) {
              if ([content isKindOfClass:[RCTScrollView class]] && !self.rctScrollView) {
                  RCTScrollView *rctScrollView = (RCTScrollView *)content;
                  self.rctScrollView = rctScrollView;

                  [self.rctScrollView.scrollView.panGestureRecognizer
                      addTarget:self
                         action:@selector(scrollViewPanGestureHandler:)];
                  [self setSheetHeight:rctScrollView.scrollView.contentSize.height];

                  [self.rctScrollView.scrollView addObserver:self
                                                  forKeyPath:@"contentSize"
                                                     options:NSKeyValueObservingOptionNew
                                                     context:NULL];
              } else if (!self.contentView) {
                  self.contentView = content;
                  [self setSheetHeight:content.bounds.size.height];
                  [self.contentView addObserver:self
                                     forKeyPath:@"frame"
                                        options:NSKeyValueObservingOptionNew
                                        context:nil];
              }
          } else {
              @throw [NSException exceptionWithName:@"Failed present Sheet"
                                             reason:@"Failed find element by tag"
                                           userInfo:nil];
          }
      } else {
          @throw [NSException exceptionWithName:@"Failed present Sheet"
                                         reason:@"contentTag does not exist"
                                       userInfo:nil];
      }
    });
}

- (void)observeValueForKeyPath:(NSString *)keyPath
                      ofObject:(id)object
                        change:(NSDictionary<NSKeyValueChangeKey, id> *)change
                       context:(void *)context {
    if ([keyPath isEqualToString:@"contentSize"]) {
        CGSize newContentSize = [change[NSKeyValueChangeNewKey] CGSizeValue];
        [self setSheetHeight:newContentSize.height];
    } else if ([keyPath isEqualToString:@"frame"]) {
        CGRect newFrame = [[change objectForKey:NSKeyValueChangeNewKey] CGRectValue];
        [self setSheetHeight:newFrame.size.height];
    } else {
        [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
    }
}

#pragma mark - Present And update sizes

- (CGFloat)contentMaximumHeight {
    return self.view.frame.size.height - self.view.safeAreaInsets.top -
           self.view.safeAreaInsets.bottom;
}

- (void)present:(CGFloat)height {
    if (self.isPresented)
        return;
    CGFloat safeAreaBottom = self.keyboardHeight == 0 ? self.view.safeAreaInsets.bottom : 0;
    CGFloat adjustedHeight = MIN(self.contentMaximumHeight, height) + safeAreaBottom;

    self.containerFrame =
        CGRectMake(0, self.view.bounds.size.height - adjustedHeight - self.keyboardHeight,
                   self.view.bounds.size.width, adjustedHeight);
    self.containerView.frame =
        CGRectMake(0, self.view.bounds.size.height, self.view.bounds.size.width, adjustedHeight);

    _boundViewController.view.frame = CGRectMake(
        0, 0, self.containerFrame.size.width,
        self.footerView ? adjustedHeight - self.view.safeAreaInsets.bottom : adjustedHeight);

    self.isAnimatePresentInProcess = YES;
    [self
        animate:^{
          self.containerView.frame =
              CGRectMake(0, self.view.bounds.size.height - self.containerView.frame.size.height,
                         self.view.bounds.size.width, self.containerView.frame.size.height);
          self.backdrop.backgroundColor =
              [[UIColor blackColor] colorWithAlphaComponent:self.backdropOpacity];
        }
        velocity:0.0
        completion:^(BOOL finished) {
          if (finished) {
              self.isAnimatePresentInProcess = NO;
          }
        }];

    self.isPresented = true;
}

- (void)updateContentSize:(CGFloat)height {
    CGFloat safeAreaBottom = self.keyboardHeight == 0 ? self.view.safeAreaInsets.bottom : 0;
    CGFloat adjustedHeight = MIN(self.contentMaximumHeight, height) + safeAreaBottom;
    CGFloat prevHeight = self.containerFrame.size.height;
    self.containerFrame =
        CGRectMake(0, self.view.bounds.size.height - adjustedHeight - self.keyboardHeight,
                   self.view.bounds.size.width, adjustedHeight);

    _boundViewController.view.frame = CGRectMake(
        0, 0, self.containerView.frame.size.width,
        self.footerView ? adjustedHeight - self.view.safeAreaInsets.bottom : adjustedHeight);

    CGRect tempContainerFrame =
        CGRectMake(0, self.containerFrame.origin.y, self.containerFrame.size.width, prevHeight);

    BOOL updateDown = prevHeight > self.containerFrame.size.height;

    self.isAnimatePresentInProcess = YES;
    [self
        animate:^{
          self.containerView.frame = updateDown ? tempContainerFrame : self.containerFrame;
        }
        velocity:0.0
        completion:^(BOOL finished) {
          if (updateDown) {
              self.containerView.frame = self.containerFrame;
          }
          if (finished) {
              self.isAnimatePresentInProcess = NO;
          }
        }];
}

- (CGFloat)getSheetHeight {
    CGFloat contentHeight = 0;
    if (self.rctScrollView) {
        contentHeight = self.rctScrollView.contentSize.height;
    } else if (self.contentView) {
        contentHeight = self.contentView.frame.size.height;
    }

    return [self calcSheetHeight:contentHeight];
}

- (void)dismiss {
    [self
        dismissWithCompletion:^{
        }
                     velocity:0.0];
}

- (void)dismissWithVelocity:(CGFloat)velocity {
    [self
        dismissWithCompletion:^{
        }
                     velocity:velocity];
}

- (void)dismissWithCompletion:(void (^)(void))completion velocity:(CGFloat)velocity {
    self.isPresented = false;
    [self.view endEditing:YES];
    [self
        animateDismiss:^{
          self.containerView.frame =
              CGRectMake(0, self.view.bounds.size.height, self.containerView.bounds.size.width,
                         self.containerView.bounds.size.height);
          self.backdrop.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.0];
        }
        velocity:velocity
        completion:^(BOOL finished) {
          [self dismissViewControllerAnimated:NO completion:completion];
        }];
}

- (void)setSheetBackgroundColor:(UIColor *)backgroundColor {
    self.containerView.backgroundColor = backgroundColor;
}

#pragma mark - Private Methods

- (void)setupCornerRadius {
    CGFloat radius = (CGFloat)[self.cornerTopRadius floatValue];
    UIBezierPath *maskPath =
        [UIBezierPath bezierPathWithRoundedRect:self.containerView.bounds
                              byRoundingCorners:(UIRectCornerTopLeft | UIRectCornerTopRight)
                                    cornerRadii:CGSizeMake(radius, radius)];

    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
    maskLayer.frame = self.containerView.bounds;
    maskLayer.path = maskPath.CGPath;

    self.containerView.layer.mask = maskLayer;
}

- (void)setup {
    self.view.backgroundColor = [UIColor clearColor];

    self.backdrop = [[UIView alloc] init];
    self.backdrop.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.0];
    [self.view addSubview:self.backdrop];

    self.containerView = [[UIView alloc] init];
    self.containerView.backgroundColor = [UIColor whiteColor];
    self.containerView.layer.masksToBounds = YES;
    [self.view addSubview:self.containerView];

    [self addChildViewController:_boundViewController];

    [self.containerView addSubview:_boundViewController.view];
    [_boundViewController didMoveToParentViewController:self];

    self.tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self
                                                              action:@selector(tapGestureHandler)];
    [self.backdrop addGestureRecognizer:self.tapGesture];

    self.panGesture = [[UIPanGestureRecognizer alloc] initWithTarget:self
                                                              action:@selector(panGestureHandler:)];
    [self.containerView addGestureRecognizer:self.panGesture];
}

- (void)tapGestureHandler {
    if (!self.isAnimatePresentInProcess && self.isPresented) {
        [self dismiss];
    }
}

- (void)findContentElements:(void (^)(UIView *header, UIView *content, UIView *footer))completion {
    RNNReactView *reactView = _boundViewController.reactView;
    dispatch_async(reactView.bridge.uiManager.methodQueue, ^{
      [reactView.bridge.uiManager
          addUIBlock:^(RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
            UIView *header = [uiManager viewForNativeID:NavigationLayoutElementHeaderID
                                            withRootTag:reactView.reactTag];
            UIView *content = [uiManager viewForNativeID:NavigationLayoutElementContentID
                                             withRootTag:reactView.reactTag];
            UIView *footer = [uiManager viewForNativeID:NavigationLayoutElementFooterID
                                            withRootTag:reactView.reactTag];

            dispatch_async(dispatch_get_main_queue(), ^{
              completion(header, content, footer);
            });
          }];
    });
}

- (void)scrollViewPanGestureHandler:(UIPanGestureRecognizer *)recognizer {
    if (!self.rctScrollView.scrollView) {
        return;
    }

    CGFloat yTranslation = [recognizer translationInView:self.rctScrollView.scrollView].y;
    CGFloat yVelocity = [recognizer velocityInView:self.rctScrollView.scrollView].y;

    BOOL isScrollOnTop = self.rctScrollView.scrollView.contentOffset.y <= 0;

    switch (recognizer.state) {
    case UIGestureRecognizerStateBegan:
        if (isScrollOnTop) {
            self.rctScrollView.scrollView.contentOffset = CGPointZero;
        }
        break;

    case UIGestureRecognizerStateChanged:
        if (!self.isDragging && isScrollOnTop) {
            self.isDragging = YES;
            self.startTranslationOffset = yTranslation;
        }

        if (self.isDragging) {
            CGFloat offset = yTranslation - self.startTranslationOffset;
            if (offset < 0) {
                self.isDragging = NO;
                [self didEndDragging:offset velocity:yVelocity];

            } else {
                self.rctScrollView.scrollView.contentOffset = CGPointZero;
                [self didDragWithOffset:offset];
            }
        }
        break;

    case UIGestureRecognizerStateEnded:
        if (self.isDragging) {
            self.isDragging = NO;
            [self didEndDragging:yTranslation velocity:yVelocity];
        }
        break;

    default:
        break;
    }
}

- (void)panGestureHandler:(UIPanGestureRecognizer *)recognizer {
    CGFloat offset = [recognizer translationInView:recognizer.view].y;
    CGFloat velocity = [recognizer velocityInView:recognizer.view].y;
    if (recognizer.state == UIGestureRecognizerStateChanged) {
        [self didDragWithOffset:offset];
    } else if (recognizer.state == UIGestureRecognizerStateEnded) {
        [self didEndDragging:offset velocity:velocity];
    } else if (recognizer.state == UIGestureRecognizerStateCancelled ||
               recognizer.state == UIGestureRecognizerStateFailed) {
        [self didFailedDrag];
    }
}

- (void)didDragWithOffset:(CGFloat)offset {
    CGFloat maxContentHeight = [self contentMaximumHeight];
    if (offset < 0) {
        if (self.containerFrame.size.height <= maxContentHeight) {
            _delta = (sqrt(1 + (offset * -1)) * 2.5) * -1;
            self.containerView.frame = CGRectMake(
                self.containerFrame.origin.x, self.containerFrame.origin.y + _delta,
                self.containerFrame.size.width, self.containerFrame.size.height - _delta);
        }
    } else {
        self.containerView.frame =
            CGRectMake(self.containerFrame.origin.x, self.containerFrame.origin.y + offset,
                       self.containerFrame.size.width, self.containerFrame.size.height);

        CGFloat minX = 0;
        CGFloat maxX = maxContentHeight;
        CGFloat minY = self.backdropOpacity;
        CGFloat maxY = 0.0;

        CGFloat alpha = (offset - minX) * ((maxY - minY) / (maxX - minX)) + minY;
        self.backdrop.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:alpha];
    }
}

- (void)didEndDragging:(CGFloat)offset velocity:(CGFloat)velocity {
    CGFloat destination = offset + 0.18 * velocity;
    CGFloat contentCenter = self.containerFrame.size.height / 2;
    if (destination > contentCenter) {
        [self dismiss];
    } else {
        CGRect newFrame = self.containerFrame;
        if (_delta < 0) {
            newFrame = CGRectMake(0, self.containerFrame.origin.y, self.containerFrame.size.width,
                                  self.containerFrame.size.height - _delta);
            _delta = 0;
        }
        self.isAnimatePresentInProcess = YES;

        [self
            animateDraggingWithCompletion:^{
              self.containerView.frame = newFrame;
            }
            completion:^(BOOL finished) {
              self.containerView.frame = self.containerFrame;
              self.isAnimatePresentInProcess = NO;
            }];
    }
}

- (void)didFailedDrag {
    [self animateDragging:^{
      self.containerView.frame = self.containerFrame;
    }];
}

- (void)animateDragging:(void (^)(void))animations {
    [self animate:animations velocity:0.0 completion:nil];
}

- (void)animateDraggingWithVelocity:(CGFloat)velocity animation:(void (^)(void))animations {
    [self animate:animations velocity:velocity completion:nil];
}

- (void)animateDraggingWithCompletion:(void (^)(void))animations
                           completion:(void (^__nullable)(BOOL finished))completion {
    [self animate:animations velocity:0.0 completion:completion];
}

- (void)animate:(void (^)(void))animations
       velocity:(CGFloat)velocity
     completion:(void (^__nullable)(BOOL finished))completion {
    CGFloat damping = 500.0;
    CGFloat stiffness = 1000.0;
    CGFloat mass = 3.0;
    CGFloat dampingRatio = damping / (2 * sqrt(stiffness * mass));

    [UIView animateWithDuration:0.38
                          delay:0
         usingSpringWithDamping:dampingRatio
          initialSpringVelocity:velocity / 100
                        options:UIViewAnimationOptionCurveEaseInOut |
                                UIViewAnimationOptionAllowUserInteraction
                     animations:animations
                     completion:completion];
}

- (void)animateDismiss:(void (^)(void))animations
              velocity:(CGFloat)velocity
            completion:(void (^__nullable)(BOOL finished))completion {
    CGFloat damping = 500.0;
    CGFloat stiffness = 1000.0;
    CGFloat mass = 3.0;
    CGFloat dampingRatio = damping / (2 * sqrt(stiffness * mass));

    [UIView animateWithDuration:0.38
                          delay:0
         usingSpringWithDamping:dampingRatio
          initialSpringVelocity:0.0
                        options:UIViewAnimationOptionCurveEaseIn |
                                UIViewAnimationOptionAllowUserInteraction
                     animations:animations
                     completion:completion];
}

- (void)dealloc {
    if (self.rctScrollView) {
        [self.rctScrollView.scrollView removeObserver:self forKeyPath:@"contentSize"];
    }

    if (self.contentView) {
        [self.contentView removeObserver:self forKeyPath:@"frame"];
    }

    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end
