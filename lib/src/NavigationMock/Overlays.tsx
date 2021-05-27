import React, { Component } from 'react';
import { View } from 'react-native';
import { ComponentProps } from './ComponentProps';
import { VISIBLE_OVERLAY_TEST_ID } from './constants';
import { LayoutComponent } from './LayoutComponent';
import ParentNode from './Layouts/ParentNode';
import { LayoutStore } from './LayoutStore';
import { connect } from './connect';

export const Overlays = connect(
  class extends Component<ComponentProps> {
    render() {
      return <View testID={VISIBLE_OVERLAY_TEST_ID}>{this.renderOverlays()}</View>;
    }

    renderOverlays() {
      const children = LayoutStore.getOverlays();
      return children.map((child: ParentNode) => {
        return <LayoutComponent key={child.nodeId} layoutNode={child} />;
      });
    }
  }
);
