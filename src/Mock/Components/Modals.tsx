import React, { Component } from 'react';
import { View } from 'react-native';
import { ComponentProps } from '../ComponentProps';
import { LayoutComponent } from './LayoutComponent';
import ParentNode from '../Layouts/ParentNode';
import { LayoutStore } from '../Stores/LayoutStore';
import { connect } from '../connect';

export const Modals = connect(
  class extends Component<ComponentProps> {
    render() {
      const children = LayoutStore.getModals();
      return (
        <View testID={'MODALS'}>
          {children.map((child: ParentNode) => {
            return <LayoutComponent key={child.nodeId} layoutNode={child} />;
          })}
        </View>
      );
    }
  }
);
