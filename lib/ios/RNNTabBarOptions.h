#import <Foundation/Foundation.h>

extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNTabBarOptions : NSObject

@property (nonatomic, strong) NSNumber* hidden;
@property (nonatomic, strong) NSNumber* animateHide;
@property (nonatomic, strong) NSString* tabBadge;
@property (nonatomic, strong) NSNumber* currentTabIndex;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)topBarOptions;
-(void)mergeWith:(NSDictionary*)otherOptions;

@end
