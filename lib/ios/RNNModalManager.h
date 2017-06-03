#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNStore.h"

@interface RNNModalManager : NSObject

-(instancetype)initWithStore:(RNNStore*)store;

-(void)showModal:(UIViewController*)viewController;
-(void)dismissModal:(NSString*)containerId;
-(void)dismissAllModals;

@end
