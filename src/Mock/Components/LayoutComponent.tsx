import React, { Component } from 'react';
import { View } from 'react-native';
import { BottomTabs } from './BottomTabs';
import { ComponentProps } from '../ComponentProps';
import { ComponentScreen } from './ComponentScreen';
import { Stack } from './Stack';
import { SideMenuRoot, SideMenuCenter, SideMenuLeft, SideMenuRight } from './SideMenu';

export const LayoutComponent = class extends Component<ComponentProps> {
  render() {
    switch (this.props.layoutNode.type) {
      case 'BottomTabs':
        return <BottomTabs layoutNode={this.props.layoutNode} />;
      case 'Stack':
        return <Stack layoutNode={this.props.layoutNode} />;
      case 'Component':
        return <ComponentScreen layoutNode={this.props.layoutNode} />;
      case 'SideMenuRoot':
        return <SideMenuRoot layoutNode={this.props.layoutNode} />;
      case 'SideMenuLeft':
        return <SideMenuLeft layoutNode={this.props.layoutNode} />;
      case 'SideMenuCenter':
        return <SideMenuCenter layoutNode={this.props.layoutNode} />;
      case 'SideMenuRight':
        return <SideMenuRight layoutNode={this.props.layoutNode} />;
    }

    return <View />;
  }
  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    const err = new Error(
      `Error while trying to render layout ${this.props.layoutNode.nodeId} of type ${this.props.layoutNode.type}: ${error}\n${errorInfo?.componentStack}`
    );
    (err as any).cause = error;
    throw err;
  }
};
