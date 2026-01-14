import React from 'react';
import { StyleSheet } from 'react-native';
import { NavigationComponent, NavigationProps, Options } from 'react-native-navigation';
import { WebView } from 'react-native-webview';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const { TABS_TOGETHER_LOAD_ORDER, TABS_TOGETHER_DISMISS } = testIDs;

const loadOrder: number[] = [];
const listeners: Set<() => void> = new Set();
const notifyListeners = () => listeners.forEach((fn) => fn());

const baseOptions = (title: string): Options => ({
    topBar: {
        title: { text: title },
        leftButtons: [
            { id: 'dismiss', testID: TABS_TOGETHER_DISMISS, icon: require('../../img/clear.png') },
        ],
    },
    bottomTab: {
        text: title,
        icon: require('../../img/layouts.png'),
    },
});

interface Props extends NavigationProps {
    tabIndex: number;
}

class BaseWebViewTab extends NavigationComponent<Props> {
    private mountTimestamp = Date.now();

    constructor(props: Props) {
        super(props);
        Navigation.events().bindComponent(this);
    }

    navigationButtonPressed({ buttonId }: { buttonId: string }) {
        if (buttonId === 'dismiss') {
            Navigation.dismissModal('TogetherFlagTabTest');
        }
    }

    componentDidMount() {
        const update = () => {
            const text = loadOrder.length > 0 ? loadOrder.join('→') : '...';
            Navigation.mergeOptions(this.props.componentId, {
                topBar: {
                    rightButtons: [
                        {
                            id: `loadOrder_${loadOrder.length}`,
                            testID: TABS_TOGETHER_LOAD_ORDER,
                            text,
                            enabled: false,
                            showAsAction: 'always',
                        },
                    ],
                },
            });
        };
        listeners.add(update);
        update();
    }

    onLoadStart = () => {
        if (!loadOrder.includes(this.props.tabIndex)) {
            loadOrder.push(this.props.tabIndex);
            notifyListeners();
        }
    };

    render() {
        return (
            <WebView
                source={{ html: `<html><body><h1>Tab ${this.props.tabIndex + 1}: started loading at ${new Date(this.mountTimestamp).toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit', second: '2-digit', fractionalSecondDigits: 3 })}</h1></body></html>` }}
                style={styles.webview}
                onLoadStart={this.onLoadStart}
            />
        );
    }
}

class WebViewTab1 extends BaseWebViewTab {
    static options(): Options {
        return baseOptions('Tab 1');
    }
}

class WebViewTab2 extends BaseWebViewTab {
    static options(): Options {
        return baseOptions('Tab 2');
    }
}

class WebViewTab3 extends BaseWebViewTab {
    static options(): Options {
        return baseOptions('Tab 3');
    }
}

export { WebViewTab1, WebViewTab2, WebViewTab3 };

export const resetLoadOrder = () => {
    loadOrder.length = 0;
};

export const TAB_SCREENS = [
    { name: Screens.WebViewTab1, tabIndex: 0 },
    { name: Screens.WebViewTab2, tabIndex: 1 },
    { name: Screens.WebViewTab3, tabIndex: 2 },
];

const styles = StyleSheet.create({
    webview: { flex: 1 },
});
