//
//  RNNSideMenuController.m
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 09/02/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "MMDrawerController.h"

@interface RNNSideMenuController ()

@property (readwrite) RNNSideMenuChildVC *center;
@property (readwrite) RNNSideMenuChildVC *left;
@property (readwrite) RNNSideMenuChildVC *right;
@property (readwrite) MMDrawerController *sideMenu;

@end

@implementation RNNSideMenuController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo childViewControllers:(NSArray *)childViewControllers options:(RNNNavigationOptions *)options optionsResolver:(RNNParentOptionsResolver *)optionsResolver presenter:(RNNBasePresenter *)presenter {
	self = [super init];
	
	self.presenter = presenter;
	self.options = options;
	self.layoutInfo = layoutInfo;
	self.optionsResolver = optionsResolver;
	
	[self bindChildViewControllers:childViewControllers];
	
	return self;
}

- (void)bindChildViewControllers:(NSArray<UIViewController<RNNLayoutProtocol> *> *)viewControllers {
	[self setControllers:viewControllers];
	
	self.sideMenu = [[MMDrawerController alloc] initWithCenterViewController:self.center leftDrawerViewController:self.left rightDrawerViewController:self.right];
	
	self.sideMenu.openDrawerGestureModeMask = MMOpenDrawerGestureModeAll;
	self.sideMenu.closeDrawerGestureModeMask = MMCloseDrawerGestureModeAll;
	
	[self addChildViewController:self.sideMenu];
	[self.sideMenu.view setFrame:self.view.bounds];
	[self.view addSubview:self.sideMenu.view];
	[self.view bringSubviewToFront:self.sideMenu.view];
}

-(void)showSideMenu:(MMDrawerSide)side animated:(BOOL)animated {
	[self.sideMenu openDrawerSide:side animated:animated completion:nil];
}

-(void)hideSideMenu:(MMDrawerSide)side animated:(BOOL)animated {
	[self.sideMenu closeDrawerAnimated:animated completion:nil];
}

-(void)setControllers:(NSArray*)controllers {
	for (id controller in controllers) {
		
		if ([controller isKindOfClass:[RNNSideMenuChildVC class]]) {
			RNNSideMenuChildVC *child = (RNNSideMenuChildVC*)controller;
			
			if (child.type == RNNSideMenuChildTypeCenter) {
				self.center = child;
			}
			else if(child.type == RNNSideMenuChildTypeLeft) {
				self.left = child;
			}
			else if(child.type == RNNSideMenuChildTypeRight) {
				self.right = child;
			}
		}
		
		else {
			@throw [NSException exceptionWithName:@"UnknownSideMenuControllerType" reason:[@"Unknown side menu type " stringByAppendingString:[controller description]] userInfo:nil];
		}
	}
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.openedViewController.preferredStatusBarStyle;
}

- (UIViewController *)openedViewController {
	switch (self.sideMenu.openSide) {
		case MMDrawerSideNone:
			return self.center;
		case MMDrawerSideLeft:
			return self.left;
		case MMDrawerSideRight:
			return self.right;
		default:
			return self.center;
			break;
	}
}

- (UIViewController<RNNLayoutProtocol> *)getLeafViewController {
	return [self.center getLeafViewController];
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	[_optionsResolver resolve:self with:self.childViewControllers];
	[_presenter present:self.options onViewControllerDidLoad:self];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[self.presenter present:options onViewControllerWillAppear:self];
}

@end
