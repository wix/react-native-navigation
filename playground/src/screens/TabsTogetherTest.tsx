import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Navigation, NavigationFunctionComponent } from 'react-native-navigation';
import { WebView } from 'react-native-webview';
import testIDs from '../testIDs';

const loadOrder: number[] = [];
const listeners: Set<() => void> = new Set();

const notifyListeners = () => listeners.forEach((fn) => fn());

interface Props {
    componentId: string;
    tabIndex: number;
    title: string;
    url: string;
}

const WebViewTab: NavigationFunctionComponent<Props> = ({ componentId, tabIndex, title, url }) => {
    const [isLoaded, setIsLoaded] = useState(false);
    const [progress, setProgress] = useState(0);

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
        setIsLoaded(true);
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.headerText}>Tab {tabIndex}: {title}</Text>
                <View style={[styles.status, isLoaded ? styles.loaded : styles.loading]}>
                    <Text style={styles.statusText}>{isLoaded ? '✓' : `${progress}%`}</Text>
                </View>
            </View>
            <WebView
                source={{ uri: url }}
                style={styles.webview}
                onLoadEnd={onLoadEnd}
                onLoadProgress={(e) => setProgress(Math.round(e.nativeEvent.progress * 100))}
                startInLoadingState
            />
        </View>
    );
};

WebViewTab.options = {
    topBar: {
        title: { text: 'WebView Tab' },
        leftButtons: [{ id: 'back', text: 'Back' }],
    },
};

Navigation.registerComponent('TabsTogetherTest.WebViewTab', () => WebViewTab);

const goBackToPlayground = () => {
    Navigation.setRoot({
        root: {
            bottomTabs: {
                options: { bottomTabs: { testID: testIDs.MAIN_BOTTOM_TABS } },
                children: [
                    { stack: { children: [{ component: { name: 'Layouts' } }], options: { bottomTab: { text: 'Layouts', icon: require('../../img/layouts.png') } } } },
                    { stack: { children: [{ component: { name: 'Options' } }], options: { bottomTab: { text: 'Options', icon: require('../../img/options.png') } } } },
                    { stack: { id: 'NavigationTabStack', children: [{ component: { name: 'Navigation' } }] } },
                ],
            },
        },
    });
};

const TABS = [
    { url: 'https://www.apple.com', title: 'Apple' },
    { url: 'https://www.microsoft.com', title: 'Microsoft' },
    { url: 'https://www.amazon.com', title: 'Amazon' },
];

const launchTest = (mode: 'together' | 'onSwitchToTab') => {
    loadOrder.length = 0;
    Navigation.setRoot({
        root: {
            bottomTabs: {
                id: 'TabsTest',
                options: { bottomTabs: { tabsAttachMode: mode, titleDisplayMode: 'alwaysShow' } },
                children: TABS.map((item, i) => ({
                    stack: {
                        id: `Tab${i}`,
                        children: [{ component: { name: 'TabsTogetherTest.WebViewTab', passProps: { tabIndex: i, ...item } } }],
                        options: { bottomTab: { text: item.title, icon: require('../../img/layouts.png') } },
                    },
                })),
            },
        },
    });
};

export const launchTabsTogetherTest = () => launchTest('together');
export const launchTabsOnSwitchTest = () => launchTest('onSwitchToTab');

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: '#f5f6fa' },
    header: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 12, backgroundColor: '#2c3e50' },
    headerText: { color: '#fff', fontSize: 16, fontWeight: '700' },
    status: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16, minWidth: 50, alignItems: 'center' },
    loading: { backgroundColor: '#e67e22' },
    loaded: { backgroundColor: '#27ae60' },
    statusText: { color: '#fff', fontSize: 12, fontWeight: '600' },
    webview: { flex: 1 },
});

export default WebViewTab;
