#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"

@protocol RNNPresenterDelegate <NSObject>

- (void)optionsUpdated;

@end

@interface RNNBasePresenter : NSObject

@property (nonatomic, weak) id<RNNPresenterDelegate> delegate;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (instancetype)initWithOptions:(RNNNavigationOptions *)options;

- (RNNNavigationOptions *)presentWithChildOptions:(RNNNavigationOptions *)childOptions on:(UIViewController *)viewController;

- (void)present:(RNNNavigationOptions *)options on:(UIViewController *)viewController;

- (void)presentOn:(UIViewController *)viewController;

- (void)overrideOptions:(RNNNavigationOptions *)options;

@end
