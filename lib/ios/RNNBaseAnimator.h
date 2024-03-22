#import "RNNDisplayLinkAnimatorDelegateProtocol.h"
#import <Foundation/Foundation.h>

@interface RNNBaseAnimator : NSObject <RNNDisplayLinkAnimatorDelegateProtocol>

@property(nonatomic, strong) UIView *view;

@property(nonatomic, strong) NSArray<id<RNNDisplayLinkAnimation>> *animations;

@end
