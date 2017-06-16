#import "RCCDrawerController.h"
#import "RCCViewController.h"
#import "MMExampleDrawerVisualStateManager.h"
#import "RCCDrawerHelper.h"
#import <React/RCTConvert.h>
#import "RCCManagerModule.h"
#import "UIViewController+Rotation.h"
#import "RCCManager.h"
#import "RCCTabBarController.h"

#define RCCDRAWERCONTROLLER_ANIMATION_DURATION 0.33f


@implementation RCCDrawerController

@synthesize drawerStyle = _drawerStyle;

UIViewController *leftViewController = nil;
UIViewController *rightViewController = nil;

-(UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self supportedControllerOrientations];
}

- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge
{
    
    self.drawerStyle = props[@"style"];
    
    // center
    if ([children count] < 1) return nil;
    UIViewController *centerViewController = [RCCViewController controllerWithLayout:children[0] globalProps:globalProps bridge:bridge];
    
    // left
    NSString *componentLeft = props[@"componentLeft"];
    NSDictionary *passPropsLeft = props[@"passPropsLeft"];
    if (componentLeft) leftViewController = [[RCCViewController alloc] initWithComponent:componentLeft passProps:passPropsLeft navigatorStyle:nil globalProps:globalProps bridge:bridge];
    
    // right
    NSString *componentRight = props[@"componentRight"];
    NSDictionary *passPropsRight = props[@"passPropsRight"];
    if (componentRight) rightViewController = [[RCCViewController alloc] initWithComponent:componentRight passProps:passPropsRight navigatorStyle:nil globalProps:globalProps bridge:bridge];
    
    self = [super initWithCenterViewController:centerViewController
                      leftDrawerViewController:leftViewController
                     rightDrawerViewController:rightViewController];
    
    [self setAnimationTypeWithName:props[@"animationType"]];
    
    // default is all MMOpenDrawerGestureModeAll and MMCloseDrawerGestureModeAll
    self.openDrawerGestureModeMask = MMOpenDrawerGestureModeAll;
    self.closeDrawerGestureModeMask = MMCloseDrawerGestureModeAll;
    
    NSNumber *disableOpenGesture = props[@"disableOpenGesture"];
    if ([disableOpenGesture boolValue]) {
        self.openDrawerGestureModeMask = MMOpenDrawerGestureModeNone;
    }
    
    [self setStyle];
    
    [self setDrawerVisualStateBlock:^(MMDrawerController *drawerController, MMDrawerSide drawerSide, CGFloat percentVisible) {
        MMDrawerControllerDrawerVisualStateBlock block;
        block = [[MMExampleDrawerVisualStateManager sharedManager] drawerVisualStateBlockForDrawerSide:drawerSide];
        if (block) {
            block(drawerController, drawerSide, percentVisible);
        }
    }];
    
    [self setGestureStartBlock:^(MMDrawerController *drawerController, UIGestureRecognizer *gesture) {
        [RCCManagerModule cancelAllRCCViewControllerReactTouches];
     }];
                                               
    self.view.backgroundColor = [UIColor clearColor];
    
    [self setRotation:props];
    
    
    if (!self) return nil;
    return self;
}


-(void)setStyle {
    
    if (self.drawerStyle[@"drawerShadow"]) {
        self.showsShadow = ([self.drawerStyle[@"drawerShadow"] boolValue]) ? YES : NO;
    }
    
    NSNumber *leftDrawerWidth = self.drawerStyle[@"leftDrawerWidth"];
    if (leftDrawerWidth) {
        self.maximumLeftDrawerWidth = self.view.bounds.size.width * MIN(1, (leftDrawerWidth.floatValue/100.0));
    }
    
    NSNumber *rightDrawerWidth = self.drawerStyle[@"rightDrawerWidth"];
    if (rightDrawerWidth) {
        self.maximumRightDrawerWidth = self.view.bounds.size.width * MIN(1, (rightDrawerWidth.floatValue/100.0));
    }
    
    NSString *contentOverlayColor = self.drawerStyle[@"contentOverlayColor"];
    if (contentOverlayColor)
    {
        UIColor *color = contentOverlayColor != (id)[NSNull null] ? [RCTConvert UIColor:contentOverlayColor] : nil;
        [self setCenterOverlayColor:color];
    }
}


- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge
{
    MMDrawerSide side = MMDrawerSideLeft;
    if ([actionParams[@"side"] isEqualToString:@"right"]) side = MMDrawerSideRight;
    BOOL animated = actionParams[@"animated"] ? [actionParams[@"animated"] boolValue] : YES;
    
    // open
    if ([performAction isEqualToString:@"open"])
    {
        [self openDrawerSide:side animated:animated completion:nil];
        return;
    }
    
    // close
    if ([performAction isEqualToString:@"close"])
    {
        if (self.openSide == side) {
            [self closeDrawerAnimated:animated completion:nil];
        }
        
        return;
    }

    // setDrawerEnabled
    if ([performAction isEqualToString:@"setDrawerEnabled"])
    {
        bool enabled = [actionParams[@"enabled"] boolValue];
        if ([actionParams[@"side"] isEqualToString:@"left"]) {
            [super setLeftDrawerViewController: enabled ? leftViewController : nil];
        } else if ([actionParams[@"side"] isEqualToString:@"right"]) {
            [super setRightDrawerViewController: enabled ? rightViewController : nil];
        }
    }

    // toggle
    if ([performAction isEqualToString:@"toggle"])
    {
        [super toggleDrawerSide:side animated:animated completion:nil];
        return;
    }

    if ([performAction isEqualToString:@"disableOpenGesture"])
    {
        BOOL disableOpenGesture = [actionParams[@"disableOpenGesture"] boolValue];
        self.openDrawerGestureModeMask = disableOpenGesture ?
                MMOpenDrawerGestureModeNone : MMOpenDrawerGestureModeAll;
        return;
    }

    // setStyle
    if ([performAction isEqualToString:@"setStyle"])
    {
        if (actionParams[@"animationType"]) {
            NSString *animationTypeString = actionParams[@"animationType"];
            [self setAnimationTypeWithName:animationTypeString];
        }
        return;
    }

    if ([performAction isEqualToString:@"updateScreen"])
    {
        NSDictionary *layout = actionParams[@"layout"];
        NSDictionary *passProps = actionParams[@"passProps"];

		NSArray *children = layout[@"children"];

		__block NSInteger selectedIndex = -1;

		[children enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop)
		{
			if ([obj[@"props"][@"selected"] boolValue]) {
				selectedIndex = idx;
				*stop = YES;
			}
		}];

		NSString *type = layout[@"type"];

		if ([self.centerViewController isKindOfClass:[RCCTabBarController class]] && [type isEqualToString:@"TabBarControllerIOS"]) {
			RCCTabBarController *tabBarController = (RCCTabBarController *)self.centerViewController;

			if (selectedIndex > -1)
			{
				tabBarController.selectedIndex = (NSUInteger)selectedIndex;
			}
		} else {
			UIViewController *controller = [RCCViewController controllerWithLayout:layout globalProps:passProps bridge:[[RCCManager sharedInstance] getBridge]];
			if (controller == nil)
			{
				return;
			}

			if ([controller isKindOfClass:[RCCTabBarController class]])
			{
				RCCTabBarController *tabBarController = (RCCTabBarController *)controller;

				if (selectedIndex > -1)
				{
					tabBarController.selectedIndex = (NSUInteger)selectedIndex;
				}
			}

        	self.centerViewController = controller;
		}

        return;
    }
    
}

-(void)setAnimationTypeWithName:(NSString*)animationTypeName {
    MMDrawerAnimationType animationType = MMDrawerAnimationTypeNone;
    
    if ([animationTypeName isEqualToString:@"door"]) animationType = MMDrawerAnimationTypeSwingingDoor;
    else if ([animationTypeName isEqualToString:@"parallax"]) animationType = MMDrawerAnimationTypeParallax;
    else if ([animationTypeName isEqualToString:@"slide"]) animationType = MMDrawerAnimationTypeSlide;
    else if ([animationTypeName isEqualToString:@"slide-and-scale"]) animationType = MMDrawerAnimationTypeSlideAndScale;
    
    [MMExampleDrawerVisualStateManager sharedManager].leftDrawerAnimationType = animationType;
    [MMExampleDrawerVisualStateManager sharedManager].rightDrawerAnimationType = animationType;
}


@end
