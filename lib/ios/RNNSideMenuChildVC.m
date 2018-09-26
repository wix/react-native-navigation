//
//  RNNSideMenuChildVC.m
//  ReactNativeNavigation
//
//  Created by Ran Greenberg on 09/02/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import "RNNSideMenuChildVC.h"

@interface RNNSideMenuChildVC ()

@property (readwrite) RNNSideMenuChildType type;
@property (readwrite) UIViewController<RNNParentProtocol> *child;

@end

@implementation RNNSideMenuChildVC

-(instancetype) initWithChild:(UIViewController<RNNParentProtocol>*)child type:(RNNSideMenuChildType)type {
	self = [super init];
	
	self.child = child;	
	[self addChildViewController:self.child];
	[self.child.view setFrame:self.view.bounds];
	[self.view addSubview:self.child.view];
	[self.view bringSubviewToFront:self.child.view];
	
	self.type = type;
	
	return self;
}

- (UIViewController *)getLeafViewController {
	return [self.child getLeafViewController];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.child.preferredStatusBarStyle;
}

@end
