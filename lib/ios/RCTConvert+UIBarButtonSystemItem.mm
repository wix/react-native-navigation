#import "RCTConvert+UIBarButtonSystemItem.h"
#import <UIKit/UIKit.h>

@implementation RCTConvert (UIBarButtonSystemItem)

RCT_ENUM_CONVERTER(UIBarButtonSystemItem, (@{
                       @"done" : @(UIBarButtonSystemItemDone),
                       @"cancel" : @(UIBarButtonSystemItemCancel),
                       @"edit" : @(UIBarButtonSystemItemEdit),
                       @"save" : @(UIBarButtonSystemItemSave),
                       @"add" : @(UIBarButtonSystemItemAdd),
                       @"flexibleSpace" : @(UIBarButtonSystemItemFlexibleSpace),
                       @"fixedSpace" : @(UIBarButtonSystemItemFixedSpace),
                       @"compose" : @(UIBarButtonSystemItemCompose),
                       @"reply" : @(UIBarButtonSystemItemReply),
                       @"action" : @(UIBarButtonSystemItemAction),
                       @"organize" : @(UIBarButtonSystemItemOrganize),
                       @"bookmarks" : @(UIBarButtonSystemItemBookmarks),
                       @"search" : @(UIBarButtonSystemItemSearch),
                       @"refresh" : @(UIBarButtonSystemItemRefresh),
                       @"stop" : @(UIBarButtonSystemItemStop),
                       @"camera" : @(UIBarButtonSystemItemCamera),
                       @"trash" : @(UIBarButtonSystemItemTrash),
                       @"play" : @(UIBarButtonSystemItemPlay),
                       @"pause" : @(UIBarButtonSystemItemPause),
                       @"rewind" : @(UIBarButtonSystemItemRewind),
                       @"fastForward" : @(UIBarButtonSystemItemFastForward),
                       @"undo" : @(UIBarButtonSystemItemUndo),
                       @"redo" : @(UIBarButtonSystemItemRedo),
                   }),
                   UIBarButtonSystemItemDone, integerValue)

@end