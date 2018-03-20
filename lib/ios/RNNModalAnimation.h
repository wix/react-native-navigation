#import <Foundation/Foundation.h>
#import "RNNScreenTransition.h"

@interface RNNModalAnimation : NSObject <UIViewControllerAnimatedTransitioning>

@property (nonatomic, strong) RNNScreenTransition* screenTransition;
@property (nonatomic) BOOL isDismiss;

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition isDismiss:(BOOL)isDismiss;

@end
