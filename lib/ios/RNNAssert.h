#import <Foundation/Foundation.h>

extern BOOL RNNIsMainQueue(void);

#ifndef RNN_NSASSERT
#define RNN_NSASSERT RCT_DEBUG
#endif

#ifndef NS_BLOCK_ASSERTIONS
#define RNNAssert(condition, ...) do { \
  if ((condition) == 0) { \
    if (RNN_NSASSERT) { \
      [[NSAssertionHandler currentHandler] handleFailureInFunction:(NSString * _Nonnull)@(__func__) \
        file:(NSString * _Nonnull)@(__FILE__) lineNumber:__LINE__ description:__VA_ARGS__]; \
    } \
  } \
} while (false)
#else
#define RNNAssert(condition, ...) do {} while (false)
#endif

#define RNNAssertMainQueue() RNNAssert(RNNIsMainQueue(), \
@"This function must be called on the main queue")
