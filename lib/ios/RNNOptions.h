#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>
@protocol RNNOptionsProtocol <NSObject>

@optional
-(void)resetOptions;
-(void)applyOn:(UIViewController*)viewController;

@end

@interface RNNOptions : NSObject <RNNOptionsProtocol>

-(instancetype)initWithDict:(NSDictionary*)dict;
-(void)mergeWith:(NSDictionary*)otherOptions;

@end
