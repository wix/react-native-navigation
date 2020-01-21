#import <Foundation/Foundation.h>
#import "TransitionOptions.h"

@interface SharedElementTransitionOptions : TransitionOptions

@property (nonatomic, strong) NSString* fromId;
@property (nonatomic, strong) NSString* toId;
@property (nonatomic, strong) TimeInterval* duration;
@property (nonatomic, strong) TimeInterval* startDelay;
@property (nonatomic, strong) Text* interpolation;

@end
