#import <Foundation/Foundation.h>

@interface RNNTabItemOptions : NSObject

@property (nonatomic) NSUInteger tag;
@property (nonatomic, strong) NSString* title;
@property (nonatomic, strong) NSString* badge;
@property (nonatomic, strong) NSString* testID;
@property (nonatomic, strong) NSNumber* visible;
@property (nonatomic, strong) NSDictionary* icon;

-(instancetype)initWithDict:(NSDictionary*)tabItemDict;

-(void)mergeWith:(NSDictionary *)otherOptions;

-(void)resetOptions;

@end
