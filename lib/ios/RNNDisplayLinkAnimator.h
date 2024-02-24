#import "RNNDisplayLinkAnimatorDelegateProtocol.h"
#import <Foundation/Foundation.h>

typedef void (^CompletionBlock)(void);

@interface RNNDisplayLinkAnimator : NSObject

@property(nonatomic, copy) CompletionBlock completion;
@property(nonatomic, copy) CompletionBlock onStart;

- (instancetype)initWithDisplayLinkAnimators:
                    (NSArray<id<RNNDisplayLinkAnimatorDelegateProtocol>> *)displayLinkAnimators
                                    duration:(CGFloat)duration;

- (instancetype)initWithDisplayLinkAnimator:(id<RNNDisplayLinkAnimatorDelegateProtocol>)displayLinkAnimators
                                   duration:(CGFloat)duration;

- (void)start;

@end
