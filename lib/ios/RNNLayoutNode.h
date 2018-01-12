
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RNNLayoutNode : NSObject

@property NSString* type;
@property NSString* nodeId;
@property NSDictionary* data;
@property NSArray* children;

+(instancetype)create:(NSDictionary *)json;

-(BOOL)isComponent;
-(BOOL)isStack;
-(BOOL)isTabs;
-(BOOL)isTopTabs;
-(BOOL)isSideMenuRoot;
-(BOOL)isSideMenuLeft;
-(BOOL)isSideMenuRight;
-(BOOL)isSideMenuCenter;

@end
