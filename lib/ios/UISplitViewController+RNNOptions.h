#import "RNNNumber.h"
#import <UIKit/UIKit.h>

@interface UISplitViewController (RNNOptions)

- (void)rnn_setDisplayMode:(NSString *)displayMode;

- (void)rnn_setPrimaryEdge:(NSString *)primaryEdge;

- (void)rnn_setMinWidth:(RNNNumber *)minWidth;

- (void)rnn_setMaxWidth:(RNNNumber *)maxWidth;

- (void)rnn_setPrimaryBackgroundStyle:(NSString *)style;

@end
