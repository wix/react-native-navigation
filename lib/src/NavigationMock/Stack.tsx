import React, { Component } from 'react';
import { View } from 'react-native';
import { ComponentProps } from './ComponentProps';
import { LayoutComponent } from './LayoutComponent';
import ParentNode from './Layouts/ParentNode';
import { connect } from './connect';

export const Stack = connect(
  class extends Component<ComponentProps> {
    renderScreens() {
      const children = this.props.layoutNode.children;
      return children.map((child: ParentNode, i: number) => {
        return (
          <LayoutComponent
            key={child.nodeId}
            layoutNode={child}
            bottomTabs={this.props.bottomTabs}
            stack={this.props.layoutNode}
            backButton={i > 0}
          />
        );
      });
    }

    render() {
      return <View>{this.renderScreens()}</View>;
    }
  }
);
