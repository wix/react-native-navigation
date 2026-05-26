import React from 'react';
import { Image, StyleSheet, Text, View } from 'react-native';
import { NavigationComponent, NavigationProps, Options } from 'react-native-navigation';
import Button from '../components/Button';
import Root from '../components/Root';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';

const TOP_BAR_COLOR = '#20303C';

type ReproMode = 'transparentVisible' | 'hidden' | 'opaqueVisible' | 'drawBehindOpaque';

const MODE_LABELS: Record<ReproMode, string> = {
  transparentVisible: 'A: transparent + drawBehind + visible',
  hidden: 'B: visible false (pill gone)',
  opaqueVisible: 'C: opaque black, no drawBehind',
  drawBehindOpaque: 'D: drawBehind + visible, top bar color',
};

function optionsForMode(mode: ReproMode): Options {
  const base: Options = {
    statusBar: {
      drawBehind: true,
      backgroundColor: 'transparent',
      style: 'light',
    },
    topBar: {
      title: { text: 'Nav Bar E2E Repro' },
      background: { color: TOP_BAR_COLOR },
    },
  };

  switch (mode) {
    case 'transparentVisible':
      return {
        ...base,
        navigationBar: {
          visible: true,
          drawBehind: true,
          backgroundColor: 'transparent',
        },
      };
    case 'hidden':
      return {
        ...base,
        navigationBar: {
          visible: false,
          drawBehind: false,
          backgroundColor: 'transparent',
        },
      };
    case 'opaqueVisible':
      return {
        ...base,
        navigationBar: {
          visible: true,
          drawBehind: false,
          backgroundColor: '#000000',
        },
      };
    case 'drawBehindOpaque':
      return {
        ...base,
        navigationBar: {
          visible: true,
          drawBehind: true,
          backgroundColor: TOP_BAR_COLOR,
        },
      };
  }
}

interface State {
  mode: ReproMode;
}

export default class NavigationBarEdgeToEdgeReproScreen extends NavigationComponent<
  NavigationProps,
  State
> {
  static options(): Options {
    return optionsForMode('transparentVisible');
  }

  state: State = {
    mode: 'transparentVisible',
  };

  applyMode = (mode: ReproMode) => {
    this.setState({ mode });
    Navigation.mergeOptions(this.props.componentId, optionsForMode(mode));
  };

  render() {
    const { mode } = this.state;

    return (
      <Root componentId={this.props.componentId} style={styles.root}>
        <Image style={styles.image} source={require('../../img/city.png')} fadeDuration={0} />
        <Text style={styles.title}>Android navigation bar edge-to-edge repro</Text>
        <Text style={styles.hint}>
          Edge-to-edge must be enabled in MainActivity. Use gesture navigation on API 35+.
        </Text>
        <Text style={styles.mode} testID={testIDs.NAV_BAR_E2E_REPRO_MODE}>
          {MODE_LABELS[mode]}
        </Text>
        <Button
          label={MODE_LABELS.transparentVisible}
          testID={testIDs.NAV_BAR_E2E_REPRO_TRANSPARENT_BTN}
          onPress={() => this.applyMode('transparentVisible')}
        />
        <Button
          label={MODE_LABELS.hidden}
          testID={testIDs.NAV_BAR_E2E_REPRO_HIDDEN_BTN}
          onPress={() => this.applyMode('hidden')}
        />
        <Button
          label={MODE_LABELS.opaqueVisible}
          testID={testIDs.NAV_BAR_E2E_REPRO_OPAQUE_BTN}
          onPress={() => this.applyMode('opaqueVisible')}
        />
        <Button
          label={MODE_LABELS.drawBehindOpaque}
          testID={testIDs.NAV_BAR_E2E_REPRO_DRAW_BEHIND_OPAQUE_BTN}
          onPress={() => this.applyMode('drawBehindOpaque')}
        />
        <View style={styles.footer} testID={testIDs.NAV_BAR_E2E_REPRO_FOOTER}>
          <Text style={styles.footerText}>BOTTOM MARKER — should show through transparent nav bar</Text>
        </View>
      </Root>
    );
  }
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    paddingHorizontal: 16,
  },
  image: {
    height: 220,
    width: '100%',
    resizeMode: 'cover',
    marginBottom: 12,
  },
  title: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 8,
  },
  hint: {
    fontSize: 13,
    color: '#555',
    marginBottom: 12,
  },
  mode: {
    fontSize: 14,
    fontWeight: '600',
    marginBottom: 12,
  },
  footer: {
    marginTop: 'auto',
    marginBottom: 0,
    paddingVertical: 12,
    backgroundColor: '#FF4081',
    alignItems: 'center',
  },
  footerText: {
    color: '#FFFFFF',
    fontWeight: '700',
    fontSize: 12,
    textAlign: 'center',
  },
});
