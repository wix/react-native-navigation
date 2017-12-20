#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"

@interface RNNModalManager : NSObject

@property (nonatomic, strong) UIViewController* toVC;

-(instancetype)initWithStore:(RNNStore*)store;
-(void)showModal:(UIViewController*)viewController completion:(RNNTransitionCompletionBlock)completion;
-(void)dismissModal:(NSString*)containerId;
-(void)dismissAllModals;

@end
