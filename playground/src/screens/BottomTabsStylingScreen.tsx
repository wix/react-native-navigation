import React, { Component } from 'react';
import { NavigationProps, Options } from 'react-native-navigation';
import { Text, StyleSheet, View, ScrollView, Platform } from 'react-native';

import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import Screens from './Screens';
import { stack } from '../commons/Layouts';

const isIOS26 = Platform.OS === 'ios' && parseInt(Platform.Version as string, 10) >= 26;

const COLORS = [
  { label: 'Red', value: '#E74C3C' },
  { label: 'Blue', value: '#2980B9' },
  { label: 'Green', value: '#27AE60' },
  { label: 'Dark', value: '#2C3E50' },
  { label: 'White', value: '#FFFFFF' },
  { label: 'Semi-transparent Pink', value: 'rgba(255, 182, 193, 0.5)' },
  { label: 'Semi-transparent Blue', value: 'rgba(52, 152, 219, 0.5)' },
];

interface State {
  currentBg: string;
  drawBehind: boolean;
  translucent: boolean;
}

export default class BottomTabsStylingScreen extends Component<NavigationProps, State> {
  static options(): Options {
    return {
      topBar: {
        title: { text: 'Tab Bar Styling' },
      },
      bottomTab: {
        text: 'Styling',
        icon: require('../../img/whatshot.png'),
      },
    };
  }

  state: State = {
    currentBg: 'default',
    drawBehind: false,
    translucent: false,
  };

  render() {
    return (
      <View style={styles.root}>
        <ScrollView contentContainerStyle={isIOS26 ? styles.scrollContent : styles.scrollContentLegacy}>
        <Text style={styles.header}>Background Color</Text>
        {COLORS.map((c) => (
          <Button key={c.value} label={`BG: ${c.label}`} onPress={() => this.setBgColor(c.value)} />
        ))}
        <Button label="Reset BG (default)" onPress={this.resetBg} />

        <Text style={styles.header}>Translucent</Text>
        <Button label="Translucent: ON" onPress={() => this.setTranslucent(true)} />
        <Button label="Translucent: OFF" onPress={() => this.setTranslucent(false)} />

        <Text style={styles.header}>Transparent</Text>
        <Button label="BG: Fully Transparent" onPress={this.setTransparent} />

        <Text style={styles.header}>Draw Behind</Text>
        <Button label="drawBehind: ON" onPress={() => this.setDrawBehind(true)} />
        <Button label="drawBehind: OFF" onPress={() => this.setDrawBehind(false)} />

        <Text style={styles.header}>Combined</Text>
        <Button label="Opaque Red + drawBehind OFF" onPress={this.opaqueRedNormal} />
        <Button label="Translucent + drawBehind ON" onPress={this.translucentDrawBehind} />
        <Button label="Opaque Dark + drawBehind OFF" onPress={this.opaqueDarkNormal} />

        <Text style={styles.header}>Border & Shadow</Text>
        <Button label="Red border + shadow" onPress={this.addBorderAndShadow} />

        <View style={styles.statusBox}>
          <Text style={styles.statusText}>BG: {this.state.currentBg}</Text>
          <Text style={styles.statusText}>drawBehind: {String(this.state.drawBehind)}</Text>
          <Text style={styles.statusText}>translucent: {String(this.state.translucent)}</Text>
        </View>
        </ScrollView>
      </View>
    );
  }

  setBgColor = (color: string) => {
    this.setState({ currentBg: color });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: { backgroundColor: color },
    });
  };

  resetBg = () => {
    this.setState({ currentBg: 'default' });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: { backgroundColor: '#F8F8F8' },
    });
  };

  setTranslucent = (translucent: boolean) => {
    this.setState({ translucent });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: { translucent },
    });
  };

  setTransparent = () => {
    this.setState({ currentBg: 'transparent' });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: { backgroundColor: 'transparent' },
    });
  };

  setDrawBehind = (drawBehind: boolean) => {
    this.setState({ drawBehind });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: { drawBehind },
    });
  };

  opaqueRedNormal = () => {
    this.setState({ currentBg: '#E74C3C', drawBehind: false, translucent: false });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        backgroundColor: '#E74C3C',
        translucent: false,
        drawBehind: false,
      },
    });
  };

  translucentDrawBehind = () => {
    this.setState({ currentBg: 'default', drawBehind: true, translucent: true });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        translucent: true,
        drawBehind: true,
        backgroundColor: 'rgba(255,255,255,0.5)',
      },
    });
  };

  opaqueDarkNormal = () => {
    this.setState({ currentBg: '#2C3E50', drawBehind: false, translucent: false });
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        backgroundColor: '#2C3E50',
        translucent: false,
        drawBehind: false,
      },
    });
  };

  addBorderAndShadow = () => {
    Navigation.mergeOptions(this.props.componentId, {
      bottomTabs: {
        borderColor: 'red',
        borderWidth: 1,
        shadow: { color: '#E74C3C', radius: 10, opacity: 0.6 },
      },
    });
  };
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
  },
  scrollContent: {
    alignItems: 'center',
    padding: 16,
    paddingBottom: 120,
  },
  scrollContentLegacy: {
    alignItems: 'center',
    padding: 16,
  },
  header: {
    fontSize: 14,
    fontWeight: '700',
    color: '#555',
    marginTop: 16,
    marginBottom: 4,
    alignSelf: 'flex-start',
  },
  statusBox: {
    marginTop: 20,
    padding: 12,
    backgroundColor: '#F0F0F0',
    borderRadius: 8,
    width: '100%',
  },
  statusText: {
    fontSize: 12,
    fontFamily: 'Courier',
    color: '#333',
  },
});
