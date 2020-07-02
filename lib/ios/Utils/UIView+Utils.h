#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, ViewType) {
    ViewTypeImage,
    ViewTypeText,
    ViewTypeOther
};

@interface UIView (Utils)

- (UIView *)findChildByClass:clazz;

- (ViewType)viewType;

@end
