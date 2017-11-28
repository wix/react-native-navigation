#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "RNNTopBarOptions.h"

extern const NSInteger BLUR_STATUS_TAG;
extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNNavigationOptions : NSObject

@property (nonatomic, strong) NSNumber* statusBarHidden;
@property (nonatomic, strong) NSNumber* screenBackgroundColor;
@property (nonatomic, strong) NSString* tabBadge;
@property (nonatomic, strong) id orientation;
@property (nonatomic, strong) NSArray* leftButtons;
@property (nonatomic, strong) NSArray* rightButtons;
@property (nonatomic, strong) NSNumber* statusBarBlur;
@property (nonatomic, strong) NSNumber* statusBarHideWithTopBar;
@property (nonatomic, strong) NSNumber* tabBarHidden;
@property (nonatomic, strong) RNNTopBarOptions* topBar;


- (UIInterfaceOrientationMask)supportedOrientations;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)navigationOptions;

-(void)applyOn:(UIViewController*)viewController;
-(void)mergeWith:(NSDictionary*)otherOptions;

@end
