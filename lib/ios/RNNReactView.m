#import "RNNReactView.h"
#import "RCTHelpers.h"
#import <React/RCTUIManager.h>

@implementation RNNReactView

- (instancetype)initWithBridge:(RCTBridge *)bridge moduleName:(NSString *)moduleName initialProperties:(NSDictionary *)initialProperties availableSize:(CGSize)availableSize reactViewReadyBlock:(RNNReactViewReadyCompletionBlock)reactViewReadyBlock {
	self = [super initWithBridge:bridge moduleName:moduleName initialProperties:initialProperties];
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(contentDidAppear:) name:RCTContentDidAppearNotification object:nil];
	 _reactViewReadyBlock = reactViewReadyBlock;
	[bridge.uiManager setAvailableSize:availableSize forRootView:self];
	
	return self;
}

- (void)contentDidAppear:(NSNotification *)notification {
#ifdef DEBUG
	if ([((RNNReactView *)notification.object).moduleName isEqualToString:self.moduleName]) {
		[RCTHelpers removeYellowBox:self];
	}
#endif
	
	RNNReactView* appearedView = notification.object;
	
	 if (_reactViewReadyBlock && [appearedView.appProperties[@"componentId"] isEqual:self.componentId]) {
	 	_reactViewReadyBlock();
		 _reactViewReadyBlock = nil;
		 [[NSNotificationCenter defaultCenter] removeObserver:self];
	 }
}

- (NSString *)componentId {
	return self.appProperties[@"componentId"];
}

- (void)setRootViewDidChangeIntrinsicSize:(void (^)(CGSize))rootViewDidChangeIntrinsicSize {
		_rootViewDidChangeIntrinsicSize = rootViewDidChangeIntrinsicSize;
		self.delegate = self;
}

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
	if (_rootViewDidChangeIntrinsicSize) {
		_rootViewDidChangeIntrinsicSize(rootView.intrinsicContentSize);
	}
}

- (void)setAlignment:(NSString *)alignment inFrame:(CGRect)frame {
	if ([alignment isEqualToString:@"fill"]) {
		self.sizeFlexibility = RCTRootViewSizeFlexibilityNone;
		[self setFrame:frame];
	} else {
		self.sizeFlexibility = RCTRootViewSizeFlexibilityWidthAndHeight;
		__weak RNNReactView *weakSelf = self;
		[self setRootViewDidChangeIntrinsicSize:^(CGSize intrinsicSize) {
			[weakSelf setFrame:CGRectMake(0, 0, intrinsicSize.width, intrinsicSize.height)];
		}];
	}
}

@end
