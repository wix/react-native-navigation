import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { NavigationComponent, NavigationProps, Navigation as Nav } from 'react-native-navigation';
import Button from '../components/Button';
import Root from '../components/Root';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const {
    TOPBAR_TITLE_TEXT,
    TOPBAR_TITLE_AVATAR,
    SET_TOPBAR_WITH_SUBTITLE_BTN,
    SET_TOPBAR_WITHOUT_SUBTITLE_BTN,
    SET_TOPBAR_CENTER_WITH_BUTTONS_BTN,
    SET_TOPBAR_FILL_WITH_BUTTONS_BTN,
} = testIDs;

// TopBar title component WITH subtitle
function TopBarWithSubtitle() {
    return (
        <View style={{ flex: 1 }}>
            <View style={{ flexDirection: 'row' }}>
                <View
                    testID={TOPBAR_TITLE_AVATAR}
                    style={{ alignSelf: 'center', marginRight: 20, width: 10, height: 10, backgroundColor: 'red' }}
                />
                <View style={{ flex: 1, justifyContent: 'center' }}>
                    <Text testID={TOPBAR_TITLE_TEXT} numberOfLines={1}>
                        Title - pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas sed
                    </Text>
                    <Text numberOfLines={1}>Subtitle</Text>
                </View>
            </View>
        </View>
    );
}

// TopBar title component WITHOUT subtitle - this triggers the bug on Android
function TopBarWithoutSubtitle() {
    return (
        <View style={{ flex: 1 }}>
            <View style={{ flexDirection: 'row' }}>
                <View
                    testID={TOPBAR_TITLE_AVATAR}
                    style={{ alignSelf: 'center', marginRight: 20, width: 10, height: 10, backgroundColor: 'red' }}
                />
                <View style={{ flex: 1, justifyContent: 'center' }}>
                    <Text testID={TOPBAR_TITLE_TEXT} numberOfLines={1}>
                        Title - pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas sed
                    </Text>
                    {/* No subtitle - this causes the bug on Android */}
                </View>
            </View>
        </View>
    );
}

// Mimics an inbox conversation header: avatar + long name + status line.
// With center alignment + right buttons, ellipsizeMode won't fire because
// TitleBarReactView measures with UNSPECIFIED — the text renders at full
// natural width and gets clipped behind the buttons.
function InboxConversationHeader() {
    return (
        <View style={inboxStyles.row}>
            <View style={inboxStyles.avatar} />
            <View style={inboxStyles.textContainer}>
                <Text numberOfLines={1} ellipsizeMode="tail" style={inboxStyles.name}>
                    John Jacob Jingleheimer Schmidt-Wolfeschlegelsteinhausen
                </Text>
                <Text numberOfLines={1} ellipsizeMode="tail" style={inboxStyles.status}>
                    Online - Last message received 2 minutes ago via mobile
                </Text>
            </View>
        </View>
    );
}

const inboxStyles = StyleSheet.create({
    row: { flexDirection: 'row', alignItems: 'center', flex: 1 },
    avatar: { width: 32, height: 32, borderRadius: 16, backgroundColor: '#4A90D9', marginRight: 8 },
    textContainer: { flex: 1, justifyContent: 'center' },
    name: { fontSize: 16, fontWeight: '600', color: '#000' },
    status: { fontSize: 12, color: '#888' },
});

Nav.registerComponent('TopBarWithSubtitle', () => TopBarWithSubtitle);
Nav.registerComponent('TopBarWithoutSubtitle', () => TopBarWithoutSubtitle);
Nav.registerComponent('InboxConversationHeader', () => InboxConversationHeader);

interface Props extends NavigationProps { }

export default class TopBarTitleTestScreen extends NavigationComponent<Props> {
    static options() {
        return {
            topBar: {
                title: {
                    component: {
                        name: 'TopBarWithSubtitle',
                        alignment: 'fill',
                    },
                },
            },
        };
    }

    constructor(props: Props) {
        super(props);
        Navigation.events().bindComponent(this);
    }

    render() {
        return (
            <Root componentId={this.props.componentId}>
                <Button
                    label="With Subtitle"
                    testID={SET_TOPBAR_WITH_SUBTITLE_BTN}
                    onPress={this.setTopBarWithSubtitle}
                />
                <Button
                    label="Without Subtitle (Bug Test)"
                    testID={SET_TOPBAR_WITHOUT_SUBTITLE_BTN}
                    onPress={this.setTopBarWithoutSubtitle}
                />
                <Button
                    label="Center + Right Buttons (Cut-off Bug)"
                    testID={SET_TOPBAR_CENTER_WITH_BUTTONS_BTN}
                    onPress={this.setCenterWithRightButtons}
                />
                <Button
                    label="Fill + Right Buttons (Working)"
                    testID={SET_TOPBAR_FILL_WITH_BUTTONS_BTN}
                    onPress={this.setFillWithRightButtons}
                />
            </Root>
        );
    }

    setTopBarWithSubtitle = () =>
        Navigation.mergeOptions(this, {
            topBar: {
                title: {
                    component: {
                        name: 'TopBarWithSubtitle',
                        alignment: 'fill',
                    },
                },
            },
        });

    setTopBarWithoutSubtitle = () =>
        Navigation.mergeOptions(this, {
            topBar: {
                title: {
                    component: {
                        name: 'TopBarWithoutSubtitle',
                        alignment: 'fill',
                    },
                },
            },
        });

    setCenterWithRightButtons = () =>
        Navigation.mergeOptions(this, {
            topBar: {
                rightButtons: [
                    { id: 'SEARCH', icon: require('../../img/clear.png') },
                    { id: 'MORE', icon: require('../../img/star.png') },
                ],
                title: {
                    component: {
                        name: 'InboxConversationHeader',
                        alignment: 'center',
                    },
                },
            },
        });

    setFillWithRightButtons = () =>
        Navigation.mergeOptions(this, {
            topBar: {
                rightButtons: [
                    { id: 'SEARCH', icon: require('../../img/clear.png') },
                    { id: 'MORE', icon: require('../../img/star.png') },
                ],
                title: {
                    component: {
                        name: 'InboxConversationHeader',
                        alignment: 'fill',
                    },
                },
            },
        });
}

