import React, { Component } from 'react';
import { View } from 'react-native';
import { LayoutComponent } from './LayoutComponent';
import { ComponentProps } from './ComponentProps';
import { connect } from 'remx';

export const BottomTabs = connect()(
  class extends Component<ComponentProps> {
    renderScreens() {
      return this.props.layoutNode.children.map((child) => {
        return (
          <LayoutComponent
            key={child.nodeId}
            layoutNode={child}
            bottomTabs={this.props.layoutNode}
            stack={this.props.stack}
          />
        );
      });
    }

    render() {
      return <View>{this.renderScreens()}</View>;
    }
  }
);
