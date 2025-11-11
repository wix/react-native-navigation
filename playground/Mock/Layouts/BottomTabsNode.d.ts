import { Options } from 'react-native-navigation/index';
import ParentNode from './ParentNode';
export default class BottomTabsNode extends ParentNode {
    selectedIndex: number;
    constructor(layout: any, parentNode?: ParentNode);
    mergeOptions(_options: Options): void;
    getVisibleLayout(): import("./ComponentNode").default;
}
