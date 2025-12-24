import React from 'react';
import { View, Text } from 'react-native';
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

Nav.registerComponent('TopBarWithSubtitle', () => TopBarWithSubtitle);
Nav.registerComponent('TopBarWithoutSubtitle', () => TopBarWithoutSubtitle);

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
                    platform="android"
                    onPress={this.setTopBarWithoutSubtitle}
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
}

