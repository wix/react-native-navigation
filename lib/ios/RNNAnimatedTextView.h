#import "RNNAnimatedReactView.h"
#import <Foundation/Foundation.h>

@interface RNNAnimatedTextView : RNNAnimatedReactView

@property(nonatomic, strong) NSTextStorage *fromTextStorage;
@property(nonatomic, strong) NSTextStorage *toTextStorage;

@end
