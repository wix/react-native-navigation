import React from 'react';
import { StyleSheet } from 'react-native';
import { NavigationComponent, NavigationProps, Options } from 'react-native-navigation';
import { WebView } from 'react-native-webview';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';

const { TABS_TOGETHER_DISMISS } = testIDs;

const webViewLoadedOrder: number[] = [];
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

interface State {
    loadStartTimestamp: number | null;
}

class WebViewTab extends NavigationComponent<Props, State> {
    state: State = { loadStartTimestamp: null };

    static options(passProps: Props): Options {
        return baseOptions(`Tab ${passProps.tabIndex + 1}`);
    }

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
            const text = webViewLoadedOrder.length > 0 ? webViewLoadedOrder.join('→') : '...';
            Navigation.mergeOptions(this.props.componentId, {
                topBar: {
                    subtitle: { text },
                },
            });
        };
        listeners.add(update);
        update();
    }

    onLoadStart = () => {
        if (!webViewLoadedOrder.includes(this.props.tabIndex)) {
            webViewLoadedOrder.push(this.props.tabIndex);
            this.setState({ loadStartTimestamp: Date.now() });
            notifyListeners();
        }
    };

    render() {
        const { loadStartTimestamp } = this.state;
        const timeString = loadStartTimestamp
            ? new Date(loadStartTimestamp).toLocaleTimeString('en-US', {
                hour12: false,
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
                fractionalSecondDigits: 3,
            })
            : 'loading...';

        return (
            <WebView
                source={{ html: `<html><body><h1>Tab ${this.props.tabIndex + 1}: started loading at ${timeString}</h1></body></html>` }}
                style={styles.webview}
                onLoadStart={this.onLoadStart}
            />
        );
    }
}

export { WebViewTab };

export const resetWebViewLoadedOrder = () => {
    webViewLoadedOrder.length = 0;
};

export const TAB_SCREENS = [
    { name: Screens.WebViewTab, tabIndex: 0 },
    { name: Screens.WebViewTab, tabIndex: 1 },
    { name: Screens.WebViewTab, tabIndex: 2 },
];

const styles = StyleSheet.create({
    webview: { flex: 1 },
});
