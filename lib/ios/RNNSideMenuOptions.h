#import <Foundation/Foundation.h>

@interface RNNSideMenuOptions : NSObject

@property (nonatomic, strong) NSNumber* leftSideVisible;
@property (nonatomic, strong) NSNumber* rightSideVisible;

-(instancetype)init;
-(instancetype)initWithDict:(NSDictionary *)sideMenuOptions;
-(void)mergeWith:(NSDictionary*)otherOptions;

-(void)resetOptions;

@end
