#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

@class RNNOptions;

@protocol RNNOptionsProtocol <NSObject>

@optional
- (void)resetOptions;

@required
- (void)applyOn:(UIViewController *)viewController;
- (void)applyOnNavigationController:(UINavigationController *)navigationController;
- (void)applyOnTabBarController:(UITabBarController *)tabBarController;

@end

@interface RNNOptions : NSObject <RNNOptionsProtocol>

- (instancetype)initWithDict:(NSDictionary*)dict;
- (void)mergeWith:(NSDictionary*)otherOptions;
- (void)applyOn:(UIViewController *)viewController defaultOptions:(RNNOptions*)defaultOptions;
- (BOOL)hasProperty:(NSString*)propName;
- (void)mergeOptions:(RNNOptions *)otherOptions;
- (void)mergeOptions:(RNNOptions *)otherOptions overrideOptions:(BOOL)override;

@end
