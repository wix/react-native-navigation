#import "RNNOptions.h"

@interface RNNPreviewOptions : RNNOptions

@property(nonatomic, strong) RNNNumber *reactTag;
@property(nonatomic, strong) RNNNumber *width;
@property(nonatomic, strong) RNNNumber *height;
@property(nonatomic, strong) RNNBool *commit;
@property(nonatomic, strong) NSArray *actions;

@end
