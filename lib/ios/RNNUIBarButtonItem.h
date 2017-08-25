#import <Foundation/Foundation.h>

@interface RNNUIBarButtonItem : UIBarButtonItem

@property (nonatomic, strong) NSString* buttonId;

-(instancetype)init:(NSString*)buttonId withIcon:(UIImage*)iconImage;
-(instancetype)init:(NSString*)buttonId withTitle:(NSString*)title;

@end

