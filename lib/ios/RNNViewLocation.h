//
//  RNNViewLocation.h
//  ReactNativeNavigation
//
//  Created by Elad Bogomolny on 03/10/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNTransitionStateHolder.h"

@class RNNTransitionStateHolder;
@interface RNNViewLocation : NSObject
@property (nonatomic) CGRect fromFrame;
@property (nonatomic) CGPoint fromCenter;
@property (nonatomic) CGSize fromSize;
@property (nonatomic) CGRect toFrame;
@property (nonatomic) CGPoint toCenter;
@property (nonatomic) CGSize toSize;
@property (nonatomic) CGAffineTransform transform;
@property (nonatomic) CGAffineTransform transformBack;

-(instancetype)initWithTransition:(RNNTransitionStateHolder*)transition andVC:(UIViewController*)vc;
-(CGRect)frameFromSuperViewController:(UIView*)view andVC:(UIViewController*)vc;
-(CGPoint)centerFromSuperViewController:(UIView*)view andVC:(UIViewController*)vc;
@end
