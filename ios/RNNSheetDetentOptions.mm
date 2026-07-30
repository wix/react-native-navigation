#import "RNNSheetDetentOptions.h"
#import "DoubleParser.h"
#import "TextParser.h"

@implementation RNNSheetDetentOptions

+ (NSArray<RNNSheetDetentOptions *> *)parseDetents:(id)json {
    if (![json isKindOfClass:[NSArray class]]) {
        return nil;
    }

    NSMutableArray<RNNSheetDetentOptions *> *detents = [NSMutableArray new];
    for (id item in (NSArray *)json) {
        RNNSheetDetentOptions *detent = [RNNSheetDetentOptions parseDetent:item];
        if (detent != nil) {
            [detents addObject:detent];
        }
    }

    return detents.count > 0 ? detents : nil;
}

+ (RNNSheetDetentOptions *)parseDetent:(id)item {
    if ([item isKindOfClass:[NSString class]]) {
        RNNSheetDetentOptions *detent = [RNNSheetDetentOptions new];
        detent.type = RNNSheetDetentTypeSystem;
        detent.systemIdentifier = [[Text alloc] initWithValue:item];
        return detent;
    }

    if (![item isKindOfClass:[NSDictionary class]]) {
        return nil;
    }

    NSDictionary *dict = (NSDictionary *)item;
    Text *identifierText = [TextParser parse:dict key:@"id"];
    if (!identifierText.hasValue) {
        return nil;
    }
    NSString *identifier = identifierText.get;

    RNNSheetDetentOptions *detent = [RNNSheetDetentOptions new];
    detent.type = RNNSheetDetentTypeCustom;
    detent.customIdentifier = [[Text alloc] initWithValue:identifier];
    detent.height = [DoubleParser parse:dict key:@"height"];
    return detent;
}

@end
