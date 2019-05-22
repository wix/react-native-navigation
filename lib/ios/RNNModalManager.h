#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNTransitionWithComponentIdCompletionBlock)(NSString *componentId);
typedef void (^RNNTransitionRejectionBlock)(NSString *code, NSString *message, NSError *error);

@protocol RNNModalManagerDelegate <NSObject>

- (void)dismissedModal:(UIViewController *)viewController;
- (void)dismissedMultipleModals:(NSArray *)viewControllers;

@end

@interface RNNModalManager : NSObject

@property (nonatomic, weak) id<RNNModalManagerDelegate> delegate;

- (void)showModal:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNTransitionWithComponentIdCompletionBlock)completion;
- (void)showModal:(UIViewController *)viewController animated:(BOOL)animated hasCustomAnimation:(BOOL)hasCustomAnimation completion:(RNNTransitionWithComponentIdCompletionBlock)completion;
- (void)dismissModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion;
- (void)dismissAllModalsAnimated:(BOOL)animated;

@end
