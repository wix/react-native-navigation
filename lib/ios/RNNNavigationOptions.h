//
//  RNNNavigationOptions.h
//  ReactNativeNavigation
//
//  Created by Elad Bogomolny on 25/07/2017.
//  Copyright © 2017 Wix. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNNavigationOptions : NSObject

@property (nonatomic, strong) NSNumber* topBarBackgroundColor;

-(instancetype)initWithDict:(NSDictionary *)navigationOptions;

-(void)apply:(UIViewController*)viewController;

@end

