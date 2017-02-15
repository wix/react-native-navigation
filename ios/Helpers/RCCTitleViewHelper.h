//
//  RCCTitleViewHelper.h
//  ReactNativeControllers
//
//  Created by Ran Greenberg on 06/09/2016.
//  Copyright © 2016 artal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RCCTitleView : UIView

@property (nonatomic, strong) UILabel *titleLabel;

@property (nonatomic, strong) UILabel *subtitleLabel;

@end

@interface RCCTitleViewHelper : NSObject


- (instancetype)init:(UIViewController*)viewController
navigationController:(UINavigationController*)navigationController
               title:(NSString*)title subtitle:(NSString*)subtitle
      titleImageData:(id)titleImageData;

-(void)setup:(NSDictionary*)style;

@end
