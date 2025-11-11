import { OptionsTopBarButton } from '../../src';
import ParentNode from './ParentNode';
export default class ComponentNode extends ParentNode {
    componentDidMountOnce: boolean;
    componentDidAppearPending: boolean;
    constructor(layout: any, parentNode?: ParentNode);
    componentDidMount(): void;
    componentDidAppear(): void;
    componentDidDisappear(): void;
    titleChanged(oldTitle: any, newTitle?: any): void;
    buttonsChanged(oldButtons: OptionsTopBarButton[], newButtons: OptionsTopBarButton[]): void;
    buttonsDidAppear(buttons?: OptionsTopBarButton[]): void;
    buttonsDidDisappear(buttons?: OptionsTopBarButton[]): void;
    getVisibleLayout(): this;
}
