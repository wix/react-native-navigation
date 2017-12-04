//
//  RNNAnimationUtills.h
//  ReactNativeNavigation
//
//  Created by Elad Bogomolny on 18/10/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RNNElementView.h"
#import "RNNTransitionStateHolder.h"

@interface RNNElementFinder : NSObject

@property (nonatomic, strong) NSArray* toVCTransitionElements;
@property (nonatomic, strong) NSArray* fromVCTransitionElements;

-(instancetype)initWithToVC:(UIViewController*)toVC andfromVC:(UIViewController*)fromVC;
-(NSArray*)findRNNElementViews:(UIView*)view;
-(RNNElementView*)findViewToAnimate:(NSArray*)RNNTransitionElementViews withId:(NSString*)elementId;
-(void)findElementsInTransition:(RNNTransitionStateHolder*)transitionStateHolder;
@end
