#import <Foundation/Foundation.h>

extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNTopTabOptions : NSObject

@property (nonatomic, strong) NSString* title;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)topBarOptions;
-(void)mergeWith:(NSDictionary*)otherOptions;

@end

