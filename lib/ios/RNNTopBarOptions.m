#import "RNNTopBarOptions.h"

@implementation RNNTopBarOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)topBarOptions {
	self = [super init];
	
	self.title = [topBarOptions valueForKey:@"title"];
	self.backgroundColor = [topBarOptions valueForKey:@"backgroundColor"];
	self.textColor = [topBarOptions valueForKey:@"textColor"];
	self.textFontFamily = [topBarOptions valueForKey:@"textFontFamily"];
	self.textFontSize = [topBarOptions valueForKey:@"textFontSize"];
	self.hidden = [topBarOptions valueForKey:@"hidden"];
	self.hideOnScroll = [topBarOptions valueForKey:@"hideOnScroll"];
	self.buttonColor = [topBarOptions valueForKey:@"buttonColor"];
	self.blur = [topBarOptions valueForKey:@"blur"];
	self.translucent = [topBarOptions valueForKey:@"translucent"];
	self.transparent = [topBarOptions valueForKey:@"transparent"];
	self.noBorder = [topBarOptions valueForKey:@"noBorder"];
	self.animateHide = [topBarOptions valueForKey:@"animateHide"];
	self.largeTitle = [topBarOptions valueForKey:@"largeTitle"];
	self.testID = [topBarOptions valueForKey:@"testID"];
	self.drawUnder = [topBarOptions valueForKey:@"drawUnder"];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}
@end


