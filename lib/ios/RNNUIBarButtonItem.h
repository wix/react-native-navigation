#import <Foundation/Foundation.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>
#import "RNNReactComponentRegistry.h"

@interface RNNUIBarButtonItem : UIBarButtonItem <RCTRootViewDelegate>

@property (nonatomic, strong) NSString* buttonId;

-(instancetype)init:(NSString*)buttonId withIcon:(UIImage*)iconImage;
-(instancetype)init:(NSString*)buttonId withTitle:(NSString*)title;
-(instancetype)init:(NSString*)buttonId withCustomView:(RCTRootView *)reactView;
-(instancetype)init:(NSString*)buttonId withSystemItem:(NSString*)systemItemName;

- (void)notifyDidAppear;
- (void)notifyDidDisappear;

@end

