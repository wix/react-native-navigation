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
@property (readwrite) UIViewController *child;

@end

@implementation RNNSideMenuChildVC

-(instancetype) initWithChild:(UIViewController*)child type:(RNNSideMenuChildType)type {
	self = [super init];
	
	self.child = child;
	[self addChildViewController:self.child];
	
	[self addChildViewController:self.child];
	[self.child.view setFrame:self.view.bounds];
	[self.view addSubview:self.child.view];
	[self.view bringSubviewToFront:self.child.view];
	
	self.type = type;
	
	return self;
}

- (BOOL)isAnimated {
	return NO;
}

@end
