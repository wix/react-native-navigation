#import <Foundation/Foundation.h>
#import "AnimatedReactView.h"

@interface AnimatedTextView : AnimatedReactView

@property (nonatomic, strong) NSTextStorage* fromTextStorage;
@property (nonatomic, strong) NSTextStorage* toTextStorage;

@end
