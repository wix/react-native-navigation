import React, { Component } from 'react';
import { LayoutComponent } from './LayoutComponent';
import { ComponentProps } from '../ComponentProps';
import { connect } from '../connect';

export const BottomTabs = connect(
  class extends Component<ComponentProps> {
    render() {
      return this.props.layoutNode.children.map((child) => {
        return <LayoutComponent key={child.nodeId} layoutNode={child} />;
      });
    }
  }
);
