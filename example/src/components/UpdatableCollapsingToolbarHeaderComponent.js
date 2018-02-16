import React from 'react';
import {StyleSheet, View, Text} from 'react-native';

class UpdatableCollapsingToolbarHeaderComponent extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <View style={{height: 200}}>
          <Text style={styles.titleTextElement}>Total amount</Text>
          <Text style={styles.valueTextElement}>${this.props.costs},-</Text>
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: '#0f2362',
  },
  titleTextElement: {
    color: '#fff',
    alignSelf: 'center',
    marginTop: 40,
    fontSize: 26,
  },
  valueTextElement: {
    color: '#fff',
    alignSelf: 'center',
    marginTop: 8,
    fontSize: 50,
  },
});

export default UpdatableCollapsingToolbarHeaderComponent;
