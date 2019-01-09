import {ScrollView} from "react-native";

const React = require('react');
const { Component } = require('react');
const {
    StyleSheet,
    View,
    TouchableOpacity,
    Text,
    Alert,
    Platform
} = require('react-native');
const { Navigation } = require('react-native-navigation');

class CustomTopBar extends Component {

    constructor(props) {
        super(props);
        this.state = {};
        this.subscription = Navigation.events().bindComponent(this);
    }

    componentDidAppear() {
        console.log('RNN', 'CTB.componentDidAppear');
    }

    componentDidDisappear() {
        console.log('RNN', `CTB.componentDidDisappear`);
    }

    componentDidMount() {
        console.log('RNN', `CTB.componentDidMount`);
    }

    componentWillUnmount() {
        console.log('RNN', `CTB.componentWillUnmount`);
        this.subscription.remove();
    }

    render() {
        return (
            <View style={styles.container}>
                <ScrollView style={styles.root}>
                <View style={{height:60,backgroundColor: 'red'}}/>
                <View style={{height:60,backgroundColor: 'green'}}/>
                <View style={{height:60,backgroundColor: 'red'}}/>
                <View style={{height:60,backgroundColor: 'green'}}/>
                <View style={{height:60,backgroundColor: 'red'}}/>
                <View style={{height:60,backgroundColor: 'green'}}/>
            </ScrollView>
            </View>
        );
    }
}

module.exports = CustomTopBar;

const styles = StyleSheet.create({
    root:{
      width:400,
      height:200,
    },
    container: {

        width:400,
        height:200,
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        alignSelf: 'center'
    },
    text: {
        alignSelf: 'center',
        color: 'black',
    }
});
