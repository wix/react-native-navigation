#import <Foundation/Foundation.h>
#import "ElementBaseTransition.h"

@interface ElementFrameTransition : ElementBaseTransition

@property (nonatomic) CGRect from;
@property (nonatomic) CGRect to;

@end
