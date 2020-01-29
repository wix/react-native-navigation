#import <UIKit/UIKit.h>
#import "Number.h"

@interface UISplitViewController (RNNOptions)

- (void)rnn_setDisplayMode:(NSString *)displayMode;

- (void)rnn_setPrimaryEdge:(NSString *)primaryEdge;

- (void)rnn_setMinWidth:(Number *)minWidth;

- (void)rnn_setMaxWidth:(Number *)maxWidth;

@end
