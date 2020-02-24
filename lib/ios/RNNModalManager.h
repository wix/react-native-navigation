#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTUIManager.h>

typedef void (^RNNTransitionCompletionBlock)(void);
typedef void (^RNNTransitionWithComponentIdCompletionBlock)(NSString *componentId);
typedef void (^RNNTransitionRejectionBlock)(NSString *code, NSString *message, NSError *error);

@protocol RNNModalManagerDelegate <NSObject>

- (void)dismissedModal:(UIViewController *)viewController;
- (void)attemptedToDismissModal:(UIViewController *)viewController;
- (void)dismissedMultipleModals:(NSArray *)viewControllers;

@end

@interface RNNModalManager : NSObject <UIAdaptivePresentationControllerDelegate>

- (instancetype)initWithUIManager:(RCTUIManager *)uiManager;

@property (nonatomic, weak) id<RNNModalManagerDelegate> delegate;

- (void)showModal:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNTransitionWithComponentIdCompletionBlock)completion;
- (void)dismissModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion;
- (void)dismissAllModalsAnimated:(BOOL)animated completion:(void (^ __nullable)(void))completion;
- (void)dismissAllModalsSynchronosly;

@end
