#import "NSArray+utils.h"
#import <UIKit/UIKit.h>

@implementation NSArray (utils)

- (NSArray *)intersect:(NSArray *)array withPropertyName:(NSString *)propertyName {
    NSMutableArray *intersection = [NSMutableArray new];
    for (NSObject *object in array) {
        NSArray *filteredArray = [self
            filteredArrayUsingPredicate:[NSPredicate
                                            predicateWithBlock:^BOOL(
                                                id _Nullable evaluatedObject,
                                                NSDictionary<NSString *, id> *_Nullable bindings) {
                                              return [evaluatedObject valueForKey:propertyName] &&
                                                     [evaluatedObject valueForKey:propertyName] ==
                                                         [object valueForKey:propertyName];
                                            }]];
        [intersection addObjectsFromArray:filteredArray];
    }

    return [NSArray arrayWithArray:intersection];
}

- (NSArray *)difference:(NSArray *)array withPropertyName:(NSString *)propertyName {
    NSMutableArray *diff = [NSMutableArray arrayWithArray:self];
    NSArray *intersection = [self intersect:array withPropertyName:propertyName];
    [diff removeObjectsInArray:intersection];

    return [NSArray arrayWithArray:diff];
}

- (NSArray *)mapObjectsUsingBlock:(id (^)(id obj, NSUInteger idx))block {
    NSMutableArray *result = [NSMutableArray arrayWithCapacity:[self count]];
    [self enumerateObjectsUsingBlock:^(id obj, NSUInteger idx, BOOL *stop) {
      [result addObject:block(obj, idx)];
    }];
    return result;
}

- (NSArray *)sortByPropertyName:(NSString *)propertyName {
    return [self sortedArrayUsingComparator:^NSComparisonResult(id a, id b) {
      id first = [a valueForKey:propertyName];
      id second = [b valueForKey:propertyName];
      return [first compare:second];
    }];
}

@end
