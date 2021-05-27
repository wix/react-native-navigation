import React, { Component } from 'react';
import { View } from 'react-native';
import { BottomTabs } from './Components/BottomTabs';
import { ComponentProps } from './ComponentProps';
import { ComponentScreen } from './Components/ComponentScreen';
import { Stack } from './Components/Stack';

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
