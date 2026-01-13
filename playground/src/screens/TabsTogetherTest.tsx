import React, { useEffect } from 'react';
import { StyleSheet } from 'react-native';
import { Navigation, NavigationFunctionComponent } from 'react-native-navigation';
import { WebView } from 'react-native-webview';

const loadOrder: number[] = [];
const listeners: Set<() => void> = new Set();

const notifyListeners = () => listeners.forEach((fn) => fn());

interface Props {
    componentId: string;
    tabIndex: number;
    title: string;
    url: string;
}

const WebViewTab: NavigationFunctionComponent<Props> = ({ componentId, tabIndex, url }) => {

    useEffect(() => {
        const update = () => {
            const text = loadOrder.length > 0 ? loadOrder.join('→') : '...';
            Navigation.mergeOptions(componentId, {
                topBar: {
                    rightButtons: [{
                        id: `loadOrder_${loadOrder.length}`,
                        text,
                        enabled: false,
                    }],
                },
            });
        };
        listeners.add(update);
        update();
        return () => { listeners.delete(update); };
    }, [componentId]);

    useEffect(() => {
        const sub = Navigation.events().registerNavigationButtonPressedListener(
            (e: { buttonId: string }) => {
                if (e.buttonId === 'back') goBackToPlayground();
            }
        );
        return () => sub.remove();
    }, []);

    const onLoadEnd = () => {
        loadOrder.push(tabIndex);
        notifyListeners();
    };

    return (
        <WebView
            source={{ uri: url }}
            style={styles.webview}
            onLoadEnd={onLoadEnd}
            startInLoadingState
        />
    );
};

WebViewTab.options = {
    topBar: {
        title: { text: 'WebView Tab' },
        leftButtons: [{ id: 'back', icon: require('../../img/clear.png') }],
    },
};

Navigation.registerComponent('TabsTogetherTest.WebViewTab', () => WebViewTab);

const goBackToPlayground = () => {
    Navigation.dismissModal('TabsTest');
};

export const resetLoadOrder = () => { loadOrder.length = 0; };

const styles = StyleSheet.create({
    webview: { flex: 1 },
});

export default WebViewTab;
