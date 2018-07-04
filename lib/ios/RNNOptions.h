#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

@class RNNOptions;

@protocol RNNOptionsProtocol <NSObject>

@optional
- (void)resetOptions;
- (void)applyOn:(UIViewController *)viewController;

@end

@interface RNNOptions : NSObject <RNNOptionsProtocol>

- (instancetype)initWithDict:(NSDictionary*)dict;
- (void)mergeWith:(NSDictionary*)otherOptions;
- (void)applyOn:(UIViewController *)viewController defaultOptions:(RNNOptions*)defaultOptions;
- (BOOL)hasProperty:(NSString*)propName;
- (void)mergeOptions:(RNNOptions *)otherOptions;
- (void)mergeOptions:(RNNOptions *)otherOptions overrideOptions:(BOOL)override;

@end
