#import <Foundation/Foundation.h>

extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNTabBarOptions : NSObject

@property (nonatomic, strong) NSNumber* hidden;
@property (nonatomic, strong) NSNumber* animateHide;
@property (nonatomic, strong) NSNumber* currentTabIndex;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) NSNumber* drawUnder;
@property (nonatomic, strong) NSString* currentTabId;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)topBarOptions;
-(void)mergeWith:(NSDictionary*)otherOptions;
- (void)resetOptions;

@end
