
#import "RCCNotification.h"
#import <React/RCTRootView.h>
#import "RCTHelpers.h"

@interface NotificationView : UIView
@property (nonatomic, strong) RCTRootView *reactView;
@property (nonatomic, strong) UIView *slideAnimGapView;
@property (nonatomic, strong) NSDictionary *params;
@property (nonatomic, strong) NSTimer *autoDismissTimer;
@property (nonatomic)         BOOL yellowBoxRemoved;
@end

@implementation NotificationView

-(id)initWithParams:(NSDictionary*)params
{
    self = [super initWithFrame:CGRectZero];
    if (self)
    {
        self.params = params;
        self.yellowBoxRemoved = NO;
        
        self.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        
        self.reactView = [[RCTRootView alloc] initWithBridge:[[RCCManager sharedInstance] getBridge] moduleName:params[@"component"] initialProperties:params[@"passProps"]];
        self.reactView.backgroundColor = [UIColor clearColor];
        self.reactView.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
        self.reactView.autoresizingMask = UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin;
        [self addSubview:self.reactView];
        
        [self.reactView.contentView.layer addObserver:self forKeyPath:@"frame" options:0 context:nil];
        [self.reactView.contentView.layer addObserver:self forKeyPath:@"bounds" options:0 context:NULL];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onRNReload) name:RCTJavaScriptWillStartLoadingNotification object:nil];
        
        if ([params[@"dismissWithSwipe"] boolValue])
        {
            UISwipeGestureRecognizer *swipeGesture = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(performDismiss)];
            swipeGesture.direction = [self swipeDirection];
            [self addGestureRecognizer:swipeGesture];
        }
        
        if (params[@"shadowRadius"] != nil && [params[@"shadowRadius"] floatValue] > 0)
        {
            self.layer.shadowColor = [UIColor blackColor].CGColor;
            self.layer.shadowOffset = CGSizeMake(0, 0);
            self.layer.shadowRadius = [params[@"shadowRadius"] floatValue];
            self.layer.shadowOpacity = 0.6;
        }
        
        self.hidden = YES;
    }
    return self;
}

-(void)layoutSubviews
{
    [super layoutSubviews];
    
    if(!self.yellowBoxRemoved)
    {
        self.yellowBoxRemoved = [RCTHelpers removeYellowBox:self.reactView];
    }
}

-(UISwipeGestureRecognizerDirection)swipeDirection
{
    UISwipeGestureRecognizerDirection direction = [self isBottomPosition] ?  UISwipeGestureRecognizerDirectionDown : UISwipeGestureRecognizerDirectionUp;
    
    NSString *animationType = [self.params valueForKeyPath:@"animation.type"];
    if ([animationType isEqualToString:@"slide-left"])
        direction = UISwipeGestureRecognizerDirectionRight;
    else if ([animationType isEqualToString:@"slide-right"])
        direction = UISwipeGestureRecognizerDirectionLeft;
    
    return direction;
}

-(void)killAutoDismissTimer
{
    if (self.autoDismissTimer != nil)
    {
        [self.autoDismissTimer invalidate];
        self.autoDismissTimer = nil;
    }
}

-(void)removeObservers
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [self.reactView.contentView.layer removeObserver:self forKeyPath:@"frame" context:nil];
    [self.reactView.contentView.layer removeObserver:self forKeyPath:@"bounds" context:NULL];
}

-(void)cleanup
{
    [self removeObservers];
    [self killAutoDismissTimer];
}

-(void)dealloc
{
    [self cleanup];
}

-(void)onRNReload
{
    [self cleanup];
    [self removeFromSuperview];
    self.reactView = nil;
}

-(UIColor*)reactViewAvgColor
{
    if (self.reactView.contentView.frame.size.width == 0 || self.reactView.contentView.frame.size.height == 0)
    {
        return [UIColor clearColor];
    }
    
    UIGraphicsBeginImageContextWithOptions(CGSizeMake(1, 1), YES, 0);
    [self.reactView.contentView drawViewHierarchyInRect:CGRectMake(0, 0, 1, 1) afterScreenUpdates:NO];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    CFDataRef pixelData = CGDataProviderCopyData(CGImageGetDataProvider(image.CGImage));
    const UInt8* data = CFDataGetBytePtr(pixelData);
    CFRelease(pixelData);
    
    //after scale defaults to bgr
    CGFloat red = data[2] / 255.0f,
            green = data[1] / 255.0f,
            blue = data[0] / 255.0f,
            alpha = data[3] / 255.0f;
    
    UIColor *color = [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
    return color;
}

-(BOOL)shouldAddSlidingAnimGapView
{
    if (![[self.params valueForKeyPath:@"animation.animated"] boolValue])
    {
        return NO;
    }
    
    NSString *animationType = [self.params valueForKeyPath:@"animation.type"];
    if (![animationType isEqualToString:@"slide-down"])
    {
        return NO;
    }
    
    if (![self.params valueForKeyPath:@"animation.damping"])
    {
        return NO;
    }
    
    CGFloat damping = [[self.params valueForKeyPath:@"animation.damping"] floatValue];
    if (damping >= 1)
    {
        return NO;
    }
    
    return YES;
}

-(BOOL)isBottomPosition
{
    return ((self.params[@"position"] != nil) && ([self.params[@"position"] isEqualToString:@"bottom"]));
}

- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context
{
    CGSize frameSize = CGSizeZero;
    if ([object isKindOfClass:[CALayer class]])
        frameSize = ((CALayer*)object).frame.size;
    if ([object isKindOfClass:[UIView class]])
        frameSize = ((UIView*)object).frame.size;
    
    if (!CGSizeEqualToSize(frameSize, self.reactView.frame.size))
    {
        BOOL isBottomPosition = [self isBottomPosition];
        CGFloat yPos = isBottomPosition ? self.superview.bounds.size.height - frameSize.height : 0;
        self.frame = CGRectMake((self.superview.frame.size.width - frameSize.width) / 2.0, yPos, frameSize.width, frameSize.height);
        self.reactView.frame = CGRectMake(0, 0, frameSize.width, frameSize.height);
        self.reactView.contentView.frame = CGRectMake(0, 0, frameSize.width, frameSize.height);
    
        //if necessary, add a view with the same color to cover the gap if there's a slide animation with spring
        if ([self shouldAddSlidingAnimGapView])
        {
            if (self.slideAnimGapView == nil)
            {
                self.slideAnimGapView = [[UIView alloc] initWithFrame:CGRectZero];
                [self.reactView addSubview:self.slideAnimGapView];
            }
            
            CGFloat yPos = isBottomPosition ? frameSize.height : -20;
            self.slideAnimGapView.frame = CGRectMake(0, yPos, self.superview.frame.size.width, 20);
            
            dispatch_async(dispatch_get_main_queue(), ^
            {
               self.slideAnimGapView.backgroundColor = [self reactViewAvgColor];
            });
        }
        
        if (self.params[@"shadowRadius"] != nil && [self.params[@"shadowRadius"] floatValue] > 0)
        {
            self.layer.shadowPath = [UIBezierPath bezierPathWithRect:self.bounds].CGPath;
        }
    }
}

-(void)performDismiss
{
    [RCCNotification dismissWithParams:self.params resolver:nil rejecter:nil];
}

-(void)startAutoDismissTimerIfNecessary
{
    if (self.params[@"autoDismissTimerSec"])
    {
        [self killAutoDismissTimer];
        
        CGFloat interval = [self.params[@"autoDismissTimerSec"] floatValue];
        interval = MAX(interval, 1);
        self.autoDismissTimer = [NSTimer scheduledTimerWithTimeInterval:interval target:self selector:@selector(performDismiss) userInfo:nil repeats:NO];
    }
}

-(void)applyAnimations
{
    NSString *animationType = [self.params valueForKeyPath:@"animation.type"];
    if ([animationType isEqualToString:@"swing"])
    {
        CATransform3D transform = CATransform3DIdentity;
        transform.m34 = -0.002f;
        transform.m24 = -0.007f;
        if (self.layer.anchorPoint.x == 0.5 && self.layer.anchorPoint.y == 0.5)
        {
            CGPoint anchorPoint = CGPointMake(0.5, 0);
            CGFloat yOffset = -self.layer.frame.size.height / 2.0;
            if([self isBottomPosition])
            {
                anchorPoint = CGPointMake(0.5, 1);
                yOffset = self.layer.frame.size.height / 2.0;
            }
            
            self.layer.anchorPoint = anchorPoint;
            self.layer.frame = CGRectOffset(self.layer.frame, 0, yOffset);
        }
        self.layer.zPosition = 1000;
        self.layer.transform = CATransform3DRotate(transform, M_PI_2, 1, 0, 0);
    }
    else
    {
        int slideAnimationSign = [self isBottomPosition] ? 1 : -1;
        CGAffineTransform transform = CGAffineTransformIdentity;
        if ([animationType isEqualToString:@"slide-down"])
            transform = CGAffineTransformMakeTranslation(0, slideAnimationSign * self.frame.size.height);
        else if ([animationType isEqualToString:@"slide-left"])
            transform = CGAffineTransformMakeTranslation(self.frame.size.width, 0);
        else if ([animationType isEqualToString:@"slide-right"])
            transform = CGAffineTransformMakeTranslation(-self.frame.size.width, 0);
        
        self.transform = transform;
    }
    
    if ( [[self.params valueForKeyPath:@"animation.fade"] boolValue])
    {
        self.alpha = 0;
    }
}

#pragma mark - show/dismiss

-(void)showAnimationEnded:(void (^)(void))completion
{
    self.alpha = 1;
    
    if (completion)
    {
        completion();
    }
    
    [self startAutoDismissTimerIfNecessary];
}

-(void)dismissAnimationEnded:(void (^)(void))completion
{
    if (completion)
    {
        completion();
    }
    
    [self removeFromSuperview];
}

-(void)performShowWithCompletion:(void (^)(void))completion
{
    self.hidden = NO;
    
    if ([[self.params valueForKeyPath:@"animation.animated"] boolValue])
    {
        CGFloat duration = [[self.params valueForKeyPath:@"animation.duration"] floatValue];
        CGFloat delay = [[self.params valueForKeyPath:@"animation.delay"] floatValue];
        
        CGFloat damping = 1;
        if ([self.params valueForKeyPath:@"animation.damping"] != nil)
        {
            damping = [[self.params valueForKeyPath:@"animation.damping"] floatValue];
            damping = MAX(damping, 0);
            damping = MIN(damping, 1);
        }
        
        [self applyAnimations];
        
        [UIView animateWithDuration:duration delay:delay usingSpringWithDamping:damping initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseOut animations:^()
         {
             self.alpha = 1;
             self.transform = CGAffineTransformIdentity;
             self.layer.transform = CATransform3DIdentity;
         }
                         completion:^(BOOL finished)
         {
             [self showAnimationEnded:completion];
         }];
    }
    else
    {
        [self showAnimationEnded:completion];
    }
}

-(void)showWithCompletion:(void (^)(void))completion
{
    if (self.frame.size.height == 0 || self.frame.size.width == 0)
    {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.05 * NSEC_PER_SEC)),
                       dispatch_get_main_queue(),
                       ^{
                           [self showWithCompletion:completion];
                       });
    }
    else
    {
        [self performShowWithCompletion:completion];
    }
}

-(void)dismissWithCompletion:(void (^)(void))completion
{
    [self killAutoDismissTimer];
    [self.reactView cancelTouches];
    
    if ([[self.params valueForKeyPath:@"animation.animated"] boolValue])
    {
        CGFloat duration = [[self.params valueForKeyPath:@"animation.duration"] floatValue] * 0.75;
        [UIView animateWithDuration:duration delay:0 usingSpringWithDamping:1 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn
                         animations:^()
         {
             [self applyAnimations];
         }
                         completion:^(BOOL finished)
         {
             [self dismissAnimationEnded:completion];
         }];
    }
    else
    {
        [self dismissAnimationEnded:completion];
    }
}

@end

@interface PendingNotification : NSObject
@property (nonatomic, copy) void (^resolve)(id result);
@property (nonatomic, copy) void (^reject)(NSString *code, NSString *message, NSError *error);
@property (nonatomic, strong) NSDictionary *params;
@end

@implementation PendingNotification
@end

@implementation RCCNotification

static NSTimeInterval gLastShownTime = NSTimeIntervalSince1970;
static NSMutableArray *gPendingNotifications = nil;
static NSMutableArray *gShownNotificationViews = nil;

+(void)showWithParams:(NSDictionary*)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject
{
    if(gPendingNotifications == nil)
        gPendingNotifications = [NSMutableArray array];
    
    if(gShownNotificationViews == nil)
        gShownNotificationViews = [NSMutableArray array];
    
    if ([gShownNotificationViews count] > 0)
    {
        for (NotificationView *notificationView in gShownNotificationViews)
        {
            [notificationView killAutoDismissTimer];
        }
        
        //limit the amount of consecutive notifications per second. If they arrive too fast, the last one will be remembered as pending
        if(CFAbsoluteTimeGetCurrent() - gLastShownTime < 1)
        {
            PendingNotification *pendingNotification = [PendingNotification new];
            pendingNotification.params = params;
            pendingNotification.resolve = resolve;
            pendingNotification.reject = reject;
            [gPendingNotifications addObject:pendingNotification];
            return;
        }
    }
    
    gLastShownTime = CFAbsoluteTimeGetCurrent();
    
    UIWindow *window = [[RCCManager sharedInstance] getAppWindow];
    NotificationView *notificationView = [[NotificationView alloc] initWithParams:params];
    [window addSubview:notificationView];
    [gShownNotificationViews addObject:notificationView];
    [notificationView showWithCompletion:^()
     {
         if (resolve)
         {
             resolve(nil);
         }
     }];
}

+(void)dismissWithParams:(NSDictionary*)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject
{
    int count = [gShownNotificationViews count];
    for (int i = count - 1 ; i >= 0; i--)
    {
        NotificationView *notificationView = [gShownNotificationViews objectAtIndex:i];
        [gShownNotificationViews removeObject:notificationView];
        [notificationView dismissWithCompletion:^()
         {
             if (i == (count - 1))
             {
                 if(resolve)
                 {
                     resolve(nil);
                 }
                 
                 if ([gPendingNotifications count] > 0)
                 {
                     PendingNotification *pendingNotification = [gPendingNotifications lastObject];
                     [self showWithParams:pendingNotification.params resolver:pendingNotification.resolve rejecter:pendingNotification.reject];
                     [gPendingNotifications removeLastObject];
                 }
             }
         }];
    }
}

@end
