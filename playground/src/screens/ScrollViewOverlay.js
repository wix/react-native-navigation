const React = require('react');
const { PureComponent } = require('react');
const { View, Text, ScrollView, StyleSheet } = require('react-native');

const colors = [
  '#3182C8',
  '#00AAAF',
  '#00A65F',
  '#E2902B',
  '#D9644A',
  '#CF262F',
  '#8B1079',
  '#48217B'
];

class ScrollViewOverlay extends PureComponent {

  render() {
    return (
      <View style={styles.root}>
        <View style={{ height: 200, width: '80%', alignSelf: 'center', flexDirection: 'row' }}>
          <ScrollView style={styles.scrollView} contentContainerStyle={styles.content}>
            {colors.map(this.renderRow)}
          </ScrollView>
        </View>
      </View>
    );
  }

  renderRow = (color) => <Text key={color} style={[styles.row, { backgroundColor: color }]}>{color}</Text>
}

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: 'blue'
  },
  root: {
    flex: 1,
    flexDirection: 'column',
    justifyContent: 'flex-end'
  },
  row: {
    height: 40,
    textAlign: 'center',
    textAlignVertical: 'center',
    color: 'white',
  },
  content: {
    backgroundColor: 'blue'
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
});

module.exports = ScrollViewOverlay;
