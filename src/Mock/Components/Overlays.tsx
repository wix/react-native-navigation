import React, { Component } from 'react';
import { View } from 'react-native';
import { ComponentProps } from '../ComponentProps';
import { VISIBLE_OVERLAY_TEST_ID } from '../constants';
import { LayoutComponent } from './LayoutComponent';
import ParentNode from '../Layouts/ParentNode';
import { LayoutStore } from '../Stores/LayoutStore';
import { connect } from '../connect';

export const Overlays = connect(
  class extends Component<ComponentProps> {
    render() {
      const children = LayoutStore.getOverlays();
      return (
        <View testID={VISIBLE_OVERLAY_TEST_ID}>
          {children.map((child: ParentNode) => {
            return <LayoutComponent key={child.nodeId} layoutNode={child} />;
          })}
        </View>
      );
    }
  }
);
