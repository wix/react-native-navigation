import React, { Component } from 'react';
import { connect } from '../connect';
import { ComponentProps } from '../ComponentProps';
import { LayoutComponent } from './LayoutComponent';
import ParentNode from '../Layouts/ParentNode';

export const SideMenuRoot = connect(
  class extends Component<ComponentProps> {
    render() {
      const children = this.props.layoutNode.children;
      return children.map((child: ParentNode) => {
        return <LayoutComponent key={child.nodeId} layoutNode={child} />;
      });
    }
  }
);

const SideMenuComponent = connect(class extends Component<ComponentProps> {
  render() {
    const children = this.props.layoutNode.children;
    const component = children[0];
    return <LayoutComponent key={component.nodeId} layoutNode={component} />;
  }
});
export const SideMenuLeft = SideMenuComponent;
export const SideMenuCenter = SideMenuComponent;
export const SideMenuRight = SideMenuComponent;
