#import "OptionsArrayParser.h"
#import "RNNOptions.h"

@implementation OptionsArrayParser

+ (NSArray *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)className {
    if (json[key]) {
        NSMutableArray *mutableArray = [NSMutableArray new];
        for (NSDictionary *dict in json[key]) {
            RNNOptions *options = [[className alloc] initWithDict:dict];
            [mutableArray addObject:options];
        }
        return mutableArray;
    }

    return nil;
}

@end
