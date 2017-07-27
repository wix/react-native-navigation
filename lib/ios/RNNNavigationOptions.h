#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNNavigationOptions : NSObject

@property (nonatomic, strong) NSNumber* topBarBackgroundColor;
@property (nonatomic, strong) NSNumber* statusBarHidden;
@property (nonatomic, strong) NSString* title;

-(instancetype)initWithDict:(NSDictionary *)navigationOptions;

-(void)apply:(UIViewController*)viewController;

@end

