import React from 'react';
import { StyleSheet, View, Text } from 'react-native';

class MyClass extends React.Component {

    render() {
        return (
            <View style={styles.container}>

            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        width: 300,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#ffffff',
    },
});

export default MyClass;
