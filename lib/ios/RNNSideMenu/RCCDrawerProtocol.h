//
//  RCCDrawerProtocol.h
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 23/03/2016.
//  Copyright © 2016 artal. All rights reserved.
//



@class RCTBridge;


@protocol RCCDrawerDelegate <NSObject>

@property (nonatomic, strong) UIButton *overlayButton;
@property (nonatomic, strong) NSDictionary *drawerStyle;

@required
- (instancetype)initWithProps:(NSDictionary *)props children:(NSArray *)children globalProps:(NSDictionary*)globalProps bridge:(RCTBridge *)bridge;
- (void)performAction:(NSString*)performAction actionParams:(NSDictionary*)actionParams bridge:(RCTBridge *)bridge;

@end

