import React, { Component } from 'react';
import { LayoutComponent } from './LayoutComponent';
import { ComponentProps } from '../ComponentProps';
import { observer } from 'remx';

export const BottomTabs = observer(
  class extends Component<ComponentProps> {
    render() {
      return this.props.layoutNode.children.map((child) => {
        return <LayoutComponent key={child.nodeId} layoutNode={child} />;
      });
    }
  }
);
