import React from 'react';
import { StyleSheet } from 'react-native';
import { NavigationComponent, NavigationComponentProps, Options } from 'react-native-navigation';
import { WebView } from 'react-native-webview';
import Navigation from '../services/Navigation';
import Screens from './Screens';

const loadOrder: number[] = [];
const listeners: Set<() => void> = new Set();
const notifyListeners = () => listeners.forEach((fn) => fn());

const baseOptions = (title: string): Options => ({
    topBar: {
        title: { text: title },
        leftButtons: [{ id: 'dismiss', icon: require('../../img/clear.png') }],
    },
    bottomTab: {
        text: title,
        icon: require('../../img/layouts.png'),
    },
});

interface WebViewTabProps extends NavigationComponentProps {
    url: string;
    tabIndex: number;
}

class BaseWebViewTab extends NavigationComponent<WebViewTabProps> {
    navigationButtonPressed({ buttonId }: { buttonId: string }) {
        if (buttonId === 'dismiss') {
            setTabsTestActive(false);
            Navigation.dismissModal('TogetherFlagTabTest');
        }
    }

    componentDidMount() {
        Navigation.events().bindComponent(this);
        const update = () => {
            const text = loadOrder.length > 0 ? loadOrder.join('→') : '...';
            Navigation.mergeOptions(this.props.componentId, {
                topBar: {
                    rightButtons: [
                        {
                            id: `loadOrder_${loadOrder.length}`,
                            text,
                            enabled: false,
                        },
                    ],
                },
            });
        };
        listeners.add(update);
        update();
    }

    onLoadEnd = () => {
        loadOrder.push(this.props.tabIndex);
        notifyListeners();
    };

    render() {
        return (
            <WebView
                source={{ uri: this.props.url }}
                style={styles.webview}
                onLoadEnd={this.onLoadEnd}
                startInLoadingState
            />
        );
    }
}

class AppleWebViewTab extends BaseWebViewTab {
    static options(): Options {
        return baseOptions('Apple');
    }
}

class MicrosoftWebViewTab extends BaseWebViewTab {
    static options(): Options {
        return baseOptions('Microsoft');
    }
}

class AmazonWebViewTab extends BaseWebViewTab {
    static options(): Options {
        return baseOptions('Amazon');
    }
}

export { AppleWebViewTab, MicrosoftWebViewTab, AmazonWebViewTab };

export const resetLoadOrder = () => {
    loadOrder.length = 0;
};

export let isTabsTestActive = false;
export const setTabsTestActive = (active: boolean) => {
    isTabsTestActive = active;
};

export const TAB_SCREENS = [
    { name: Screens.AppleWebViewTab, url: 'https://www.apple.com', tabIndex: 0 },
    { name: Screens.MicrosoftWebViewTab, url: 'https://www.microsoft.com', tabIndex: 1 },
    { name: Screens.AmazonWebViewTab, url: 'https://www.amazon.com', tabIndex: 2 },
];

const styles = StyleSheet.create({
    webview: { flex: 1 },
});
