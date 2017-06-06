//
//  ExternalViewControllerProtocol.h
//  ReactNativeControllers
//
//  Created by Artal Druk on 01/06/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RCCViewController.h"

@protocol RCCExternalViewControllerProtocol <NSObject>

@property (nullable, nonatomic, weak) id <RCCViewControllerDelegate> controllerDelegate;

-(void)setProps:(nullable NSDictionary*)props;

@end