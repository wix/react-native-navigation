#import "Double.h"
#import "RNNOptions.h"
#import "Text.h"

typedef NS_ENUM(NSInteger, RNNSheetDetentType) {
    RNNSheetDetentTypeSystem,
    RNNSheetDetentTypeCustom,
};

@interface RNNSheetDetentOptions : RNNOptions

@property(nonatomic, assign) RNNSheetDetentType type;
@property(nonatomic, strong) Text *systemIdentifier;
@property(nonatomic, strong) Text *customIdentifier;
@property(nonatomic, strong) Double *height;

+ (NSArray<RNNSheetDetentOptions *> *)parseDetents:(id)json;

@end
