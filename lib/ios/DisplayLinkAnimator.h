#import <Foundation/Foundation.h>
#import "DisplayLinkAnimatorDelegate.h"

typedef void (^CompletionBlock)(void);

@interface DisplayLinkAnimator : NSObject

@property (nonatomic, copy) CompletionBlock completion;

- (instancetype)initWithDisplayLinkAnimators:(NSArray<id<DisplayLinkAnimatorDelegate>> *)displayLinkAnimators duration:(CGFloat)duration;

- (instancetype)initWithDisplayLinkAnimator:(id<DisplayLinkAnimatorDelegate>)displayLinkAnimators duration:(CGFloat)duration;

- (void)start;

@end
