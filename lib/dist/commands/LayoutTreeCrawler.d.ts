import { LayoutType } from './LayoutType';
import { OptionsProcessor } from './OptionsProcessor';
import { Store } from '../components/Store';
export interface Data {
    name?: string;
    options?: any;
    passProps?: any;
}
export interface LayoutNode {
    id: string;
    type: LayoutType;
    data: Data;
    children: LayoutNode[];
}
export declare class LayoutTreeCrawler {
    readonly store: Store;
    private readonly optionsProcessor;
    constructor(store: Store, optionsProcessor: OptionsProcessor);
    crawl(node: LayoutNode): void;
    private handleComponent;
    private savePropsToStore;
    private isComponentWithOptions;
    private applyStaticOptions;
    private staticOptionsIfPossible;
    private assertComponentDataName;
}
