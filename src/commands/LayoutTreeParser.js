"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.LayoutTreeParser = void 0;
const LayoutType_1 = require("./LayoutType");
class LayoutTreeParser {
    uniqueIdProvider;
    constructor(uniqueIdProvider) {
        this.uniqueIdProvider = uniqueIdProvider;
        this.parse = this.parse.bind(this);
    }
    parse(api) {
        if (api.topTabs) {
            return this.topTabs(api.topTabs);
        }
        else if (api.sideMenu) {
            return this.sideMenu(api.sideMenu);
        }
        else if (api.bottomTabs) {
            return this.bottomTabs(api.bottomTabs);
        }
        else if (api.stack) {
            return this.stack(api.stack);
        }
        else if (api.component) {
            return this.component(api.component);
        }
        else if (api.externalComponent) {
            return this.externalComponent(api.externalComponent);
        }
        else if (api.splitView) {
            return this.splitView(api.splitView);
        }
        throw new Error(`unknown LayoutType "${Object.keys(api)}"`);
    }
    topTabs(api) {
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.TopTabs),
            type: LayoutType_1.LayoutType.TopTabs,
            data: { options: api.options },
            children: api.children ? api.children.map(this.parse) : [],
        };
    }
    sideMenu(api) {
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.SideMenuRoot),
            type: LayoutType_1.LayoutType.SideMenuRoot,
            data: { options: api.options },
            children: this.sideMenuChildren(api),
        };
    }
    sideMenuChildren(api) {
        const children = [];
        if (api.left) {
            children.push({
                id: this.uniqueIdProvider.generate(LayoutType_1.LayoutType.SideMenuLeft),
                type: LayoutType_1.LayoutType.SideMenuLeft,
                data: {},
                children: [this.parse(api.left)],
            });
        }
        children.push({
            id: this.uniqueIdProvider.generate(LayoutType_1.LayoutType.SideMenuCenter),
            type: LayoutType_1.LayoutType.SideMenuCenter,
            data: {},
            children: [this.parse(api.center)],
        });
        if (api.right) {
            children.push({
                id: this.uniqueIdProvider.generate(LayoutType_1.LayoutType.SideMenuRight),
                type: LayoutType_1.LayoutType.SideMenuRight,
                data: {},
                children: [this.parse(api.right)],
            });
        }
        return children;
    }
    bottomTabs(api) {
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.BottomTabs),
            type: LayoutType_1.LayoutType.BottomTabs,
            data: { options: api.options },
            children: api.children ? api.children.map(this.parse) : [],
        };
    }
    stack(api) {
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.Stack),
            type: LayoutType_1.LayoutType.Stack,
            data: { options: api.options },
            children: api.children ? api.children.map(this.parse) : [],
        };
    }
    component(api) {
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.Component),
            type: LayoutType_1.LayoutType.Component,
            data: {
                name: api.name.toString(),
                options: api.options,
                passProps: api.passProps,
            },
            children: [],
        };
    }
    externalComponent(api) {
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.ExternalComponent),
            type: LayoutType_1.LayoutType.ExternalComponent,
            data: {
                name: api.name.toString(),
                options: api.options,
                passProps: api.passProps,
            },
            children: [],
        };
    }
    splitView(api) {
        const master = api.master ? this.parse(api.master) : undefined;
        const detail = api.detail ? this.parse(api.detail) : undefined;
        return {
            id: api.id || this.uniqueIdProvider.generate(LayoutType_1.LayoutType.SplitView),
            type: LayoutType_1.LayoutType.SplitView,
            data: { options: api.options },
            children: master && detail ? [master, detail] : [],
        };
    }
}
exports.LayoutTreeParser = LayoutTreeParser;
