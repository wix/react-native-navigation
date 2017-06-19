import React from 'react';
import PropTypes from 'prop-types';
import { StyleSheet, View, Text, TouchableHighlight, Platform } from 'react-native';

function Row({ title, onPress, platform, testID }) {
    if (platform && platform !== Platform.OS) {
        return <View />;
    }

    return (
        <TouchableHighlight
            onPress={onPress}
            testID={testID}
            underlayColor={'rgba(0, 0, 0, 0.054)'}
        >
            <View style={styles.row}>
                <Text style={styles.text}>{title}</Text>
            </View>
        </TouchableHighlight>
    );
}

Row.propTypes = {
    title: PropTypes.string.isRequired,
    onPress: PropTypes.func.isRequired,
};

const styles = StyleSheet.create({
    row: {
        height: 48,
        paddingHorizontal: 16,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        borderBottomWidth: 1,
        borderBottomColor: 'rgba(0, 0, 0, 0.054)',
    },
    text: {
        fontSize: 16,
    },
});

export default Row;
