#import "AnimatedReactView.h"
#import <Foundation/Foundation.h>

@interface AnimatedTextView : AnimatedReactView

@property(nonatomic, strong) NSTextStorage *fromTextStorage;
@property(nonatomic, strong) NSTextStorage *toTextStorage;

@end
