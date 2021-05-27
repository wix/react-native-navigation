import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import { Navigation } from 'react-native-navigation';
import { ComponentProps } from './ComponentProps';
import { VISIBLE_SCREEN_TEST_ID } from './constants';
import { LayoutStore } from './LayoutStore';
import { connect } from './connect';
import { TopBar } from './TopBar';

export const ComponentScreen = connect(
  class extends Component<ComponentProps> {
    constructor(props: ComponentProps) {
      super(props);
    }

    componentDidMount() {
      this.props.layoutNode.componentDidMount();
    }

    isVisible(): boolean {
      return LayoutStore.isVisibleLayout(this.props.layoutNode);
    }

    renderTabBar() {
      if (!this.props.bottomTabs) return null;

      const bottomTabsOptions = this.props.bottomTabs.resolveOptions().bottomTabs;
      if (bottomTabsOptions?.visible === false) return null;
      const buttons = this.props.bottomTabs!.children!.map((child, i) => {
        const bottomTabOptions = child.resolveOptions().bottomTab;
        return (
          <View key={`tab-${i}`}>
            <Button
              testID={bottomTabOptions?.testID}
              title={bottomTabOptions?.text || ''}
              onPress={() => LayoutStore.selectTabIndex(this.props.bottomTabs, i)}
            />
            <Text>{bottomTabOptions?.badge}</Text>
          </View>
        );
      });

      return <View testID={bottomTabsOptions?.testID}>{buttons}</View>;
    }

    render() {
      //@ts-ignore
      const Component = Navigation.store.getWrappedComponent(this.props.layoutNode.data.name);
      return (
        <View testID={this.isVisible() ? VISIBLE_SCREEN_TEST_ID : undefined}>
          {this.props.stack && (
            <TopBar
              layoutNode={this.props.layoutNode}
              topBarOptions={this.props.layoutNode.resolveOptions().topBar}
              backButtonOptions={this.props.layoutNode.resolveOptions().topBar?.backButton}
            />
          )}
          {this.renderTabBar()}
          <Component componentId={this.props.layoutNode.nodeId} />
        </View>
      );
    }
  }
);
