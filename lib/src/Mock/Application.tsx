import React, { Component } from 'react';
import { View } from 'react-native';
import {
  DEFAULT_BACK_BUTTON_TEST_ID,
  VISIBLE_OVERLAY_TEST_ID,
  VISIBLE_SCREEN_TEST_ID,
} from './constants';
import { connect } from './connect';

interface ApplicationProps {
  entryPoint: () => void;
}

export const Application = connect(
  class extends Component<ApplicationProps> {
    static VISIBLE_SCREEN_TEST_ID = VISIBLE_SCREEN_TEST_ID;
    static VISIBLE_OVERLAY_TEST_ID = VISIBLE_OVERLAY_TEST_ID;
    static DEFAULT_BACK_BUTTON_TEST_ID = DEFAULT_BACK_BUTTON_TEST_ID;
    constructor(props: ApplicationProps) {
      super(props);
      props.entryPoint();
    }

    render() {
      const { LayoutComponent } = require('./LayoutComponent');
      const { LayoutStore } = require('./LayoutStore');
      const { Modals } = require('./Modals');
      const { Overlays } = require('./Overlays');
      return (
        <View testID={'Application'}>
          <LayoutComponent layoutNode={LayoutStore.getLayout()} />
          <Modals />
          <Overlays />
        </View>
      );
    }
  }
);
