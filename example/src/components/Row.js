import React from 'react';
import PropTypes from 'prop-types';
import {StyleSheet, View, Text, TouchableHighlight, Platform} from 'react-native';

class Row extends React.PureComponent {
  render() {
    const {title, onPress, onPressIn, platform, testID} = this.props;
    if (platform && platform !== Platform.OS) {
      return <View />;
    }

    return (
      <TouchableHighlight
        onPress={onPress}
        onPressIn={onPressIn}
        testID={testID}
        underlayColor={'rgba(0, 0, 0, 0.054)'}
      >
        <View style={styles.row}>
          <Text style={styles.text}>{title}</Text>
        </View>
      </TouchableHighlight>
    );
  }
}

Row.propTypes = {
  title: PropTypes.string.isRequired,
  onPress: PropTypes.func.isRequired,
  onPressIn: PropTypes.func
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
