import { LayoutNode } from './LayoutTreeCrawler';
import { Layout } from '../interfaces/Layout';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
export declare class LayoutTreeParser {
    private uniqueIdProvider;
    constructor(uniqueIdProvider: UniqueIdProvider);
    parse(api: Layout): LayoutNode;
    private topTabs;
    private sideMenu;
    private sideMenuChildren;
    private bottomTabs;
    private stack;
    private component;
    private externalComponent;
    private splitView;
}
