#import "RNNOptions.h"

@interface RNNPreviewOptions : RNNOptions

@property (nonatomic, strong) NSString* elementId;
@property (nonatomic, strong) NSNumber* width;
@property (nonatomic, strong) NSNumber* height;
@property (nonatomic, strong) NSNumber* commit;
@property (nonatomic, strong) NSArray* actions;

@end
