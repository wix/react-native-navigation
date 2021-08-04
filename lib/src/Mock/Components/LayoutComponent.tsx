import React, { Component } from 'react';
import { View } from 'react-native';
import { BottomTabs } from './BottomTabs';
import { ComponentProps } from '../ComponentProps';
import { ComponentScreen } from './ComponentScreen';
import { Stack } from './Stack';

export const LayoutComponent = class extends Component<ComponentProps> {
  render() {
    switch (this.props.layoutNode.type) {
      case 'BottomTabs':
        return <BottomTabs layoutNode={this.props.layoutNode} />;
      case 'Stack':
        return <Stack layoutNode={this.props.layoutNode} />;
      case 'Component':
        return <ComponentScreen layoutNode={this.props.layoutNode} />;
    }

    return <View />;
  }
};
