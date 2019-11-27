#import <Foundation/Foundation.h>

@interface NSArray (utils)

- (NSArray*)intersect:(NSArray *)array withPropertyName:(NSString *)propertyName;

- (NSArray*)difference:(NSArray *)array withPropertyName:(NSString *)propertyName;

@end
