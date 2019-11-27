#import "NSArray+utils.h"

@implementation NSArray (utils)

- (NSArray*)intersect:(NSArray *)array withPropertyName:(NSString *)propertyName {
    NSMutableArray* result = [NSMutableArray new];
    for (NSObject* object in array) {
        NSArray* filteredArray = [self filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"%@ == %@", propertyName, object]];
        [result addObjectsFromArray:filteredArray];
    }
    
    return [NSArray arrayWithArray:result];
}

- (NSArray*)difference:(NSArray *)array withPropertyName:(NSString *)propertyName {
    NSMutableArray* diff = [NSMutableArray arrayWithArray:self];
    NSArray* intersect = [self intersect:array withPropertyName:propertyName];
    [diff removeObjectsInArray:intersect];
    
    return diff;
}

@end
