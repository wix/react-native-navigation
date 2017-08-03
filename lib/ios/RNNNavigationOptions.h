#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNNavigationOptions : NSObject

@property (nonatomic, strong) NSNumber* topBarBackgroundColor;
@property (nonatomic, strong) NSNumber* topBarTextColor;
@property (nonatomic, strong) NSNumber* statusBarHidden;
@property (nonatomic, strong) NSString* title;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)navigationOptions;

-(void)applyOn:(UIViewController*)viewController;
-(void)mergeWith:(NSDictionary*)otherOptions;

@end

