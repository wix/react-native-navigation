const React = require('react');
const { Component } = require('react');

const { View, Text, Button } = require('react-native');

class CustomDialog extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <View style={styles.root} backgroundColor='red'>
                <Text style={styles.h1}>Test view</Text>
            </View>
        )
    }
}

const styles = {
    root: {
        flexGrow: 1,
        justifyContent: 'center',
        alignItems: 'center'
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