
#import "RNNControllerFactory.h"
#import "RNNLayoutNode.h"
#import "RNNRootViewController.h"
#import "RNNSideMenuController.h"
#import "RNNSideMenuChildVC.h"
#import "RNNNavigationOptions.h"
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RCTHelpers.h"
#import <React/RCTConvert.h>


@implementation RNNControllerFactory {
	id<RNNRootViewCreator> _creator;
	RNNStore *_store;
	RNNEventEmitter *_eventEmitter;
}

# pragma mark public


- (instancetype)initWithRootViewCreator:(id <RNNRootViewCreator>)creator
								  store:(RNNStore *)store
						   eventEmitter:(RNNEventEmitter*)eventEmitter {
	
	self = [super init];
	_creator = creator;
	_store = store;
	_eventEmitter = eventEmitter;
	
	return self;
}

- (UIViewController*)createLayoutAndSaveToStore:(NSDictionary*)layout {
	return [self fromTree:layout];
}

# pragma mark private

- (UIViewController*)fromTree:(NSDictionary*)json {
	RNNLayoutNode* node = [RNNLayoutNode create:json];
	
	UIViewController* result;
	
	if ( node.isContainer) {
		result = [self createContainer:node];
	}
	
	else if (node.isContainerStack)	{
		result = [self createContainerStack:node];
	}
	
	else if (node.isTabs) {
		result = [self createTabs:node];
	}
	
	else if (node.isSideMenuRoot) {
		result = [self createSideMenu:node];
	}
	
	else if (node.isSideMenuCenter) {
		result = [self createSideMenuChild:node type:RNNSideMenuChildTypeCenter];
	}
	
	else if (node.isSideMenuLeft) {
		result = [self createSideMenuChild:node type:RNNSideMenuChildTypeLeft];
	}
	else if (node.isSideMenuRight) {
		result = [self createSideMenuChild:node type:RNNSideMenuChildTypeRight];
	}
	
	if (!result) {
		@throw [NSException exceptionWithName:@"UnknownControllerType" reason:[@"Unknown controller type " stringByAppendingString:node.type] userInfo:nil];
	}

	[_store setContainer:result containerId:node.nodeId];
	
	return result;
}

- (RNNRootViewController*)createContainer:(RNNLayoutNode*)node {
	NSString* name = node.data[@"name"];
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:node.data[@"navigationOptions"]];
	NSString* containerId = node.nodeId;
	return [[RNNRootViewController alloc] initWithName:name withOptions:options withContainerId:containerId rootViewCreator:_creator eventEmitter:_eventEmitter];
}

- (RNNNavigationController*)createContainerStack:(RNNLayoutNode*)node {
	RNNNavigationController* vc = [[RNNNavigationController alloc] init];
	
	NSMutableArray* controllers = [NSMutableArray new];
	for (NSDictionary* child in node.children) {
		[controllers addObject:[self fromTree:child]];
	}
	[vc setViewControllers:controllers];
	
	return vc;
}

-(RNNTabBarController*)createTabs:(RNNLayoutNode*)node {
	RNNTabBarController* vc = [[RNNTabBarController alloc] init];
	
	NSMutableArray* controllers = [NSMutableArray new];
	NSArray* tabItems = node.data[@"tabItems"];
	int index = 0;
	for (NSDictionary *child in node.children) {
		UIViewController* childVc = [self fromTree:child];
		id tabItem = tabItems[index];
		if (tabItem && tabItem != (id)[NSNull null]) {
			[childVc setTabBarItem:[self createTabBarItem:tabItem atIndex:index]];
		}
		[controllers addObject:childVc];
		index++;
	}
	[vc setViewControllers:controllers];
	
	int selectedIndex = [node.data[@"selectedIndex"] intValue];
	if (selectedIndex > 0) {
		[vc setSelectedIndex:selectedIndex];
	}
	
	return vc;
}

- (UITabBarItem*)createTabBarItem:(NSDictionary *)tabItem atIndex:(int)index {
	UIColor *iconColor = nil;
	UIColor *selectedIconColor = nil;
	UIColor *labelColor = nil;
	UIColor *selectedLabelColor = nil;
	
	NSString *iconColorText = tabItem[@"iconColor"];
	if (iconColorText)
	{
		UIColor *color = iconColorText != (id)[NSNull null] ? [RCTConvert UIColor:iconColorText] : nil;
		iconColor = color;
		selectedIconColor = color;
	}
	NSString *selectedIconColorText = tabItem[@"selectedIconColor"];
	if (selectedIconColorText)
	{
		UIColor *color = selectedIconColorText != (id)[NSNull null] ? [RCTConvert UIColor:selectedIconColorText] : nil;
		selectedIconColor = color;
	}
	NSString *labelColorText = tabItem[@"labelColor"];
	if(labelColorText) {
		UIColor *color = labelColorText != (id)[NSNull null] ? [RCTConvert UIColor:labelColorText] : nil;
		labelColor = color;
	}
	NSString *selectedLabelColorText = tabItem[@"selectedLabelColor"];
	if(labelColorText) {
		UIColor *color = selectedLabelColorText != (id)[NSNull null] ? [RCTConvert UIColor:selectedLabelColorText] : nil;
		selectedLabelColor = color;
	}
	
	UIImage *iconImage = nil;
	id icon = tabItem[@"icon"];
	if (icon)
	{
		iconImage = [RCTConvert UIImage:icon];
		if (iconColor)
		{
			iconImage = [[self image:iconImage withColor:iconColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
		}
	}
	UIImage *iconImageSelected = nil;
	id selectedIcon = tabItem[@"selectedIcon"];
	if (selectedIcon) {
		iconImageSelected = [RCTConvert UIImage:selectedIcon];
	} else {
		iconImageSelected = [RCTConvert UIImage:icon];
	}
	if (iconImageSelected && selectedIconColor) {
		iconImageSelected = [[self image:iconImageSelected withColor:selectedIconColor] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
	}
	
	UITabBarItem* tabBarItem = [[UITabBarItem alloc] initWithTitle:tabItem[@"label"] image:iconImage tag:index];
	tabBarItem.accessibilityIdentifier = tabItem[@"testID"];
	tabBarItem.selectedImage = iconImageSelected;
	
	id imageInsets = tabItem[@"iconInsets"];
	if (imageInsets && imageInsets != (id)[NSNull null])
	{
		id topInset = imageInsets[@"top"];
		id leftInset = imageInsets[@"left"];
		id bottomInset = imageInsets[@"bottom"];
		id rightInset = imageInsets[@"right"];
		
		CGFloat top = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:topInset] : 0;
		CGFloat left = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:leftInset] : 0;
		CGFloat bottom = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:bottomInset] : 0;
		CGFloat right = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:rightInset] : 0;
		
		tabBarItem.imageInsets = UIEdgeInsetsMake(top, left, bottom, right);
	}
	
	NSMutableDictionary *unselectedAttributes = [RCTHelpers textAttributesFromDictionary:tabItem withPrefix:@"text" baseFont:[UIFont systemFontOfSize:10]];
	if (!unselectedAttributes[NSForegroundColorAttributeName] && labelColor) {
		unselectedAttributes[NSForegroundColorAttributeName] = labelColor;
	}
	[tabBarItem setTitleTextAttributes:unselectedAttributes forState:UIControlStateNormal];
	
	NSMutableDictionary *selectedAttributes = [RCTHelpers textAttributesFromDictionary:tabItem withPrefix:@"selectedText" baseFont:[UIFont systemFontOfSize:10]];
	if (!selectedAttributes[NSForegroundColorAttributeName] && selectedLabelColor) {
		selectedAttributes[NSForegroundColorAttributeName] = selectedLabelColor;
	}
	[tabBarItem setTitleTextAttributes:selectedAttributes forState:UIControlStateSelected];
	
	// create badge
	NSObject *badge = tabItem[@"badge"];
	if (badge == nil || [badge isEqual:[NSNull null]]) {
		tabBarItem.badgeValue = nil;
	} else {
		tabBarItem.badgeValue = [NSString stringWithFormat:@"%@", badge];
	}
	
	return tabBarItem;
}

- (UIViewController*)createSideMenu:(RNNLayoutNode*)node {
	NSMutableArray* childrenVCs = [NSMutableArray new];
	
	
	for (NSDictionary *child in node.children) {
		UIViewController *vc = [self fromTree:child];
		[childrenVCs addObject:vc];
	}
	RNNSideMenuController *sideMenu = [[RNNSideMenuController alloc] initWithControllers:childrenVCs];
	return sideMenu;
}


- (UIViewController*)createSideMenuChild:(RNNLayoutNode*)node type:(RNNSideMenuChildType)type {
	UIViewController* child = [self fromTree:node.children[0]];
	RNNSideMenuChildVC *sideMenuChild = [[RNNSideMenuChildVC alloc] initWithChild: child type:type];
	
	return sideMenuChild;
}


- (UIImage *)image:(UIImage*)image withColor:(UIColor *)color1
{
	UIGraphicsBeginImageContextWithOptions(image.size, NO, image.scale);
	CGContextRef context = UIGraphicsGetCurrentContext();
	CGContextTranslateCTM(context, 0, image.size.height);
	CGContextScaleCTM(context, 1.0, -1.0);
	CGContextSetBlendMode(context, kCGBlendModeNormal);
	CGRect rect = CGRectMake(0, 0, image.size.width, image.size.height);
	CGContextClipToMask(context, rect, image.CGImage);
	[color1 setFill];
	CGContextFillRect(context, rect);
	UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	return newImage;
}



@end
