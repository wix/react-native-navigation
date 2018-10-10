#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

@class RNNOptions;

@protocol RNNOptionsDelegate <NSObject>

@optional
- (void)optionsDidUpdatedWithOptions:(RNNOptions *)otherOptions;

@end

@protocol RNNOptionsProtocol <NSObject>

@optional
- (void)resetOptions;

@required
- (void)applyOn:(UIViewController *)viewController;
- (void)applyOnNavigationController:(UINavigationController *)navigationController;
- (void)applyOnTabBarController:(UITabBarController *)tabBarController;

@end

@interface RNNOptions : NSObject <RNNOptionsProtocol>

@property (nonatomic, weak) id<RNNOptionsDelegate> delegate;

- (instancetype)initWithDict:(NSDictionary*)dict;
- (instancetype)initEmptyOptions;
- (void)mergeWith:(NSDictionary*)otherOptions;
- (void)applyOn:(UIViewController *)viewController defaultOptions:(RNNOptions*)defaultOptions;
- (BOOL)hasProperty:(NSString*)propName;
- (void)mergeOptions:(RNNOptions *)otherOptions;
- (void)mergeOptions:(RNNOptions *)otherOptions overrideOptions:(BOOL)override;

@end
