import React from 'react';
import { StyleSheet, View, Text } from 'react-native';

class Push extends React.Component {

    render() {
        return (
            <View style={styles.container}>
                <Text>Push Screen</Text>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: '#ffffff',
    },
});

export default Push;
