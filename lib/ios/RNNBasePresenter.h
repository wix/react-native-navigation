#import <Foundation/Foundation.h>
#import "RNNNavigationOptions.h"

@protocol RNNPresenterDelegate <NSObject>

- (void)optionsUpdated;

@end

@interface RNNBasePresenter : NSObject

@property (nonatomic) id<RNNPresenterDelegate> delegate;
@property (nonatomic, strong) RNNNavigationOptions* options;

- (instancetype)initWithOptions:(RNNNavigationOptions *)options;

- (void)presentOn:(UIViewController *)viewController;

- (void)presentOnLoad:(UIViewController *)viewController;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)overrideOptions:(RNNNavigationOptions *)options;

@end
