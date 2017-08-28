import React from 'react';
import {StyleSheet, View, Text, ScrollView} from 'react-native';
import Row from '../../components/Row';

class UpdatableCollapsingHeader extends React.Component {
  static navigatorStyle = {
    drawUnderTabBar: true,
    navBarButtonColor: '#ffffff',
    navBarTextColor: '#ffffff',
    collapsingToolBarCollapsedColor: '#0f2362',
    collapsingToolBarComponent: 'example.Transitions.UpdatableCollapsingHeader.component',
    collapsingToolBarComponentInitialProps: {costs: 10},
    navBarBackgroundColor: '#eeeeee'
  };

  constructor (props) {
    super(props)

    // Initial state
    this.state = { costs: 10 };
  };

  addTenDollars = () => {
    this.setState({
      costs: this.state.costs + 10,
    });

    this.props.navigator.setStyle({
      collapsingToolBarComponentInitialProps: {costs: this.state.costs},
    });
  };

  render() {
    return (
      <ScrollView style={styles.container}>
        <View>
          {[...new Array(20)].map((a, index) => (
            <Row key={'Click row ' + index + ' to add $10,-'} title={'Row ' + index} onPress={this.addTenDollars}/>
          ))}
        </View>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

export default UpdatableCollapsingHeader;
