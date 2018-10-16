#import <Foundation/Foundation.h>
#import "RNNLayoutNode.h"

@interface RNNLayoutInfo : NSObject

- (instancetype)initWithNode:(RNNLayoutNode *)node;

@property (nonatomic, strong) NSString* componentId;
@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSDictionary* props;

@end
