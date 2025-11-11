"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const tslib_1 = require("tslib");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const EventsStore_1 = require("../Stores/EventsStore");
const ParentNode_1 = tslib_1.__importDefault(require("./ParentNode"));
class ComponentNode extends ParentNode_1.default {
    componentDidMountOnce = false;
    componentDidAppearPending = false;
    constructor(layout, parentNode) {
        super(layout, 'Component', parentNode);
    }
    componentDidMount() {
        this.componentDidMountOnce = true;
        this.componentDidAppearPending && setImmediate(() => this.componentDidAppear());
    }
    componentDidAppear() {
        if (this.componentDidMountOnce) {
            EventsStore_1.events.invokeComponentWillAppear({
                componentName: this.data.name,
                componentId: this.nodeId,
                componentType: 'Component',
            });
            EventsStore_1.events.invokeComponentDidAppear({
                componentName: this.data.name,
                componentId: this.nodeId,
                componentType: 'Component',
            });
            this.buttonsDidAppear(lodash_1.default.concat(this.data.options.topBar?.rightButtons || [], this.data.options.topBar?.leftButtons || []));
            this.titleChanged(undefined, this.data.options.topBar?.title);
        }
        else {
            this.componentDidAppearPending = true;
        }
    }
    componentDidDisappear() {
        EventsStore_1.events.invokeComponentDidDisappear({
            componentName: this.data.name,
            componentId: this.nodeId,
            componentType: 'Component',
        });
        this.buttonsDidDisappear(lodash_1.default.concat(this.data.options.topBar?.rightButtons || [], this.data.options.topBar?.leftButtons || []));
        this.titleChanged(this.data.options.topBar?.title);
    }
    titleChanged(oldTitle, newTitle) {
        if (oldTitle && oldTitle.component) {
            EventsStore_1.events.invokeComponentDidDisappear({
                componentName: oldTitle.component.name,
                componentId: oldTitle.component.componentId,
                componentType: 'TopBarTitle',
            });
        }
        if (newTitle && newTitle.component) {
            EventsStore_1.events.invokeComponentWillAppear({
                componentName: newTitle.component.name,
                componentId: newTitle.component.componentId,
                componentType: 'TopBarTitle',
            });
            EventsStore_1.events.invokeComponentDidAppear({
                componentName: newTitle.component.name,
                componentId: newTitle.component.componentId,
                componentType: 'TopBarTitle',
            });
        }
    }
    buttonsChanged(oldButtons, newButtons) {
        this.buttonsDidDisappear(oldButtons);
        this.buttonsDidAppear(newButtons);
    }
    buttonsDidAppear(buttons = []) {
        buttons.forEach((button) => {
            if (button.component) {
                EventsStore_1.events.invokeComponentWillAppear({
                    componentName: button.component.name,
                    // @ts-ignore
                    componentId: button.component.componentId,
                    componentType: 'TopBarButton',
                });
                EventsStore_1.events.invokeComponentDidAppear({
                    componentName: button.component.name,
                    // @ts-ignore
                    componentId: button.component.componentId,
                    componentType: 'TopBarButton',
                });
            }
        });
    }
    buttonsDidDisappear(buttons = []) {
        buttons.forEach((button) => {
            if (button.component) {
                EventsStore_1.events.invokeComponentDidDisappear({
                    componentName: button.component.name,
                    // @ts-ignore
                    componentId: button.component.componentId,
                    componentType: 'TopBarButton',
                });
            }
        });
    }
    getVisibleLayout() {
        return this;
    }
}
exports.default = ComponentNode;
