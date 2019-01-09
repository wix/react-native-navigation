const React = require('react');
const { PureComponent } = require('react');

const { Text, Button, ScrollView, View, Alert, Platform } = require('react-native');
const { Navigation } = require('react-native-navigation');

const testIDs = require('../testIDs');

class CustomDialog extends PureComponent {
    static get options() {
        return {
            statusBarBackgroundColor: 'green'
        };
    }

    render() {
        return (
            <View style={styles.container}>
                <ScrollView style={styles.root}>
                    <View style={{height: 60, backgroundColor: 'red'}}/>
                    <View style={{height: 60, backgroundColor: 'green'}}/>
                    <View style={{height: 60, backgroundColor: 'red'}}/>
                    <View style={{height: 60, backgroundColor: 'green'}}/>
                    <View style={{height: 60, backgroundColor: 'red'}}/>
                    <View style={{height: 60, backgroundColor: 'green'}}/>
                </ScrollView>
            </View>
        );
    }

    didDisappear() {
        if (Platform.OS === 'android') {
            Alert.alert('Overlay disappeared');
        }
    }

    onCLickOk() {
        Navigation.dismissOverlay(this.props.componentId);
    }

    onCLickSetRoot() {
        Navigation.setRoot({
            root: {
                component: {
                    name: 'navigation.playground.TextScreen'
                }
            }
        });
    }

    onCLickSetInterceptTouch() {
        Navigation.mergeOptions(this.props.componentId, {
            overlay: {
                interceptTouchOutside: false
            }
        });
    }
}

const styles = {
    root: {
        width: 400,
        height: 200,
    },
    container: {

        width: 400,
        height: 200,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        alignSelf: 'center'
    },
    h1: {
        fontSize: 24,
        textAlign: 'center',
        margin: 10
    },
    h2: {
        fontSize: 12,
        textAlign: 'center',
        margin: 10
    },
    footer: {
        fontSize: 10,
        color: '#888',
        marginTop: 10
    }
};

module.exports = CustomDialog;
