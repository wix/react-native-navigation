"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.ComponentScreen = void 0;
const tslib_1 = require("tslib");
const react_1 = tslib_1.__importStar(require("react"));
const react_native_1 = require("react-native");
const src_1 = require("src");
const constants_1 = require("../constants");
const LayoutStore_1 = require("../Stores/LayoutStore");
const connect_1 = require("../connect");
const TopBar_1 = require("./TopBar");
const EventsStore_1 = require("../Stores/EventsStore");
const lodash_1 = tslib_1.__importDefault(require("lodash"));
const layoutActions_1 = require("../actions/layoutActions");
function isURISource(src) {
    return !!src && typeof src === 'object' && 'uri' in src;
}
exports.ComponentScreen = (0, connect_1.connect)(class extends react_1.Component {
    constructor(props) {
        super(props);
    }
    componentDidMount() {
        this.props.layoutNode.componentDidMount();
    }
    isVisible() {
        const isVisible = LayoutStore_1.LayoutStore.isVisibleLayout(this.props.layoutNode);
        return isVisible;
    }
    renderTabBar() {
        const bottomTabs = this.props.layoutNode.getBottomTabs();
        if (!bottomTabs)
            return null;
        const bottomTabsOptions = bottomTabs.resolveOptions().bottomTabs;
        if (bottomTabsOptions?.visible === false)
            return null;
        const buttons = bottomTabs.children.map((child, i) => {
            const bottomTabOptions = child.resolveOptions().bottomTab;
            const icon = bottomTabs.selectedIndex === i
                ? bottomTabOptions?.selectedIcon
                : bottomTabOptions?.icon;
            const iconURI = isURISource(icon) ? icon.uri : undefined;
            return (react_1.default.createElement(react_native_1.View, { key: `tab-${i}` },
                react_1.default.createElement(react_native_1.TouchableOpacity, { style: { padding: 10 }, testID: bottomTabOptions?.testID, onPress: () => {
                        EventsStore_1.events.invokeBottomTabPressed({
                            tabIndex: i,
                        });
                        if (lodash_1.default.defaultTo(bottomTabOptions?.selectTabOnPress, true))
                            (0, layoutActions_1.switchTabByIndex)(this.props.layoutNode.getBottomTabs(), i);
                    } },
                    react_1.default.createElement(react_native_1.View, { style: { justifyContent: 'center', alignItems: 'center' } },
                        react_1.default.createElement(react_native_1.Text, null, bottomTabOptions?.badge),
                        iconURI && (react_1.default.createElement(react_native_1.Image, { style: { width: 18, height: 18, marginBottom: 5 }, source: { uri: iconURI } })),
                        react_1.default.createElement(react_native_1.Text, { style: { fontSize: 12 } }, bottomTabOptions?.text || '')))));
        });
        return (react_1.default.createElement(react_native_1.View, { testID: bottomTabsOptions?.testID, style: {
                flexDirection: 'row',
                justifyContent: 'center',
                width: '100%',
                backgroundColor: '#F0F2F5',
            } }, buttons));
    }
    render() {
        const Component = src_1.Navigation.mock.store.getWrappedComponent(this.props.layoutNode.data.name);
        if (!Component)
            throw new Error(`${this.props.layoutNode.data.name} has not been registered.`);
        return (react_1.default.createElement(react_native_1.View, { testID: this.isVisible() ? constants_1.VISIBLE_SCREEN_TEST_ID : undefined },
            this.props.layoutNode.getStack() && (react_1.default.createElement(TopBar_1.TopBar, { layoutNode: this.props.layoutNode, topBarOptions: this.props.layoutNode.resolveOptions().topBar, backButtonOptions: this.props.layoutNode.resolveOptions().topBar?.backButton })),
            react_1.default.createElement(Component, { componentId: this.props.layoutNode.nodeId }),
            this.renderTabBar()));
    }
});
