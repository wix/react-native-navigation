import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import { Navigation } from 'react-native-navigation';
import { ComponentProps } from '../ComponentProps';
import { VISIBLE_SCREEN_TEST_ID } from '../constants';
import { LayoutStore } from '../Stores/LayoutStore';
import { connect } from '../connect';
import { TopBar } from './TopBar';
import { events } from '../Stores/EventsStore';
import _ from 'lodash';

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
      const bottomTabs = this.props.layoutNode.getBottomTabs();
      if (!bottomTabs) return null;

      const bottomTabsOptions = bottomTabs.resolveOptions().bottomTabs;
      if (bottomTabsOptions?.visible === false) return null;
      const buttons = bottomTabs!.children!.map((child, i) => {
        const bottomTabOptions = child.resolveOptions().bottomTab;
        return (
          <View key={`tab-${i}`}>
            <Button
              testID={bottomTabOptions?.testID}
              title={bottomTabOptions?.text || ''}
              onPress={() => {
                events.invokeBottomTabPressed({
                  tabIndex: i,
                });
                if (_.defaultTo(bottomTabOptions?.selectTabOnPress, true))
                  LayoutStore.selectTabIndex(this.props.layoutNode.getBottomTabs(), i);
              }}
            />
            <Text>{bottomTabOptions?.badge}</Text>
          </View>
        );
      });

      return <View testID={bottomTabsOptions?.testID}>{buttons}</View>;
    }

    render() {
      const Component = Navigation.mock.store.getWrappedComponent(this.props.layoutNode.data.name);
      if (!Component)
        throw new Error(`${this.props.layoutNode.data.name} has not been registered.`);

      return (
        <View testID={this.isVisible() ? VISIBLE_SCREEN_TEST_ID : undefined}>
          {this.props.layoutNode.getStack() && (
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
