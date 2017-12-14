#import <UIKit/UIKit.h>

@interface RNNUITabBarItem : UITabBarItem

-(instancetype)initWithDictionary:(NSDictionary*)tabItemDict;

-(void)mergeWith:(NSDictionary *)otherOptions;

@end
