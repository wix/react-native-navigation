//
//  KYDrawerController.h
//  KYDrawerController
//
//  Created by Yifei Zhou on 1/8/16.
//  Copyright © 2016 Yifei Zhou. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class KYDrawerController;

typedef NS_ENUM(NSUInteger, KYDrawerControllerDrawerState) { KYDrawerControllerDrawerStateOpened, KYDrawerControllerDrawerStateClosed };

typedef NS_ENUM(NSUInteger, KYDrawerControllerDrawerDirection) { KYDrawerControllerDrawerDirectionLeft, KYDrawerControllerDrawerDirectionRight };

@protocol KYDrawerControllerDelegate <NSObject>

@optional
- (void)drawerController:(KYDrawerController *)drawerController stateDidChange:(KYDrawerControllerDrawerState)drawerState;

@end

@interface KYDrawerController : UIViewController

@property (copy, nonatomic, nullable) IBInspectable NSString *mainSegueIdentifier;

@property (copy, nonatomic, nullable) IBInspectable NSString *drawerSegueIdentifier;

@property (assign, nonatomic) IBInspectable CGFloat containerViewMaxAlpha;

@property (assign, nonatomic) IBInspectable NSTimeInterval drawerAnimationDuration;

@property (strong, nonatomic) UIViewController *mainViewController;

@property (strong, nonatomic) UIViewController *drawerViewController;

@property (readonly, nonatomic) UIViewController *displayingViewController;

@property (weak, nonatomic, nullable) id<KYDrawerControllerDelegate> delegate;

@property (assign, nonatomic) KYDrawerControllerDrawerState drawerState;

@property (assign, nonatomic) KYDrawerControllerDrawerDirection drawerDirection;

@property (assign, nonatomic) CGFloat drawerWidth;

@property (assign, nonatomic, getter=isScreenEdgePanGestreEnabled) BOOL screenEdgePanGestreEnabled;

@property (strong, nonatomic) UITapGestureRecognizer *containerViewTapGesture;

@property (readonly, nonatomic) UIScreenEdgePanGestureRecognizer *screenEdgePanGesture;

@property (readonly, nonatomic) UIPanGestureRecognizer *panGesture;

- (instancetype)initWithDrawerDirection:(KYDrawerControllerDrawerDirection)drawerDirection drawerWidth:(CGFloat)drawerWidth;

- (void)setDrawerState:(KYDrawerControllerDrawerState)drawerState animated:(BOOL)animated;

@end

NS_ASSUME_NONNULL_END