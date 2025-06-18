import React, { Component } from 'react';
import { View, Text, TouchableOpacity, Image, ImageURISource } from 'react-native';
import { Navigation, ImageResource } from 'react-native-navigation';
import { ComponentProps } from '../ComponentProps';
import { VISIBLE_SCREEN_TEST_ID } from '../constants';
import { LayoutStore } from '../Stores/LayoutStore';
import { connect } from '../connect';
import { TopBar } from './TopBar';
import { events } from '../Stores/EventsStore';
import _ from 'lodash';
import { switchTabByIndex } from '../actions/layoutActions';

function isURISource(src: ImageResource | undefined): src is ImageURISource {
  return !!src && typeof src === 'object' && 'uri' in src;
}

export const ComponentScreen = connect(
  class extends Component<ComponentProps> {
    constructor(props: ComponentProps) {
      super(props);
    }

    componentDidMount() {
      this.props.layoutNode.componentDidMount();
    }

    isVisible(): boolean {
      const isVisible = LayoutStore.isVisibleLayout(this.props.layoutNode);
      return isVisible;
    }

    renderTabBar() {
      const bottomTabs = this.props.layoutNode.getBottomTabs();
      if (!bottomTabs) return null;
      const bottomTabsOptions = bottomTabs.resolveOptions().bottomTabs;
      if (bottomTabsOptions?.visible === false) return null;
      const buttons = bottomTabs!.children!.map((child, i) => {
        const bottomTabOptions = child.resolveOptions().bottomTab;
        const icon =
          (bottomTabs as any).selectedIndex === i
            ? bottomTabOptions?.selectedIcon
            : bottomTabOptions?.icon;
        const iconURI = isURISource(icon) ? icon.uri : undefined;
        return (
          <View key={`tab-${i}`}>
            <TouchableOpacity
              style={{ padding: 10 }}
              testID={bottomTabOptions?.testID}
              onPress={() => {
                events.invokeBottomTabPressed({
                  tabIndex: i,
                });
                if (_.defaultTo(bottomTabOptions?.selectTabOnPress, true))
                  switchTabByIndex(this.props.layoutNode.getBottomTabs(), i);
              }}
            >
              <View style={{ justifyContent: 'center', alignItems: 'center' }}>
                <Text>{bottomTabOptions?.badge}</Text>
                {iconURI && (
                  <Image
                    style={{ width: 18, height: 18, marginBottom: 5 }}
                    source={{ uri: iconURI }}
                  />
                )}
                <Text style={{ fontSize: 12 }}>{bottomTabOptions?.text || ''}</Text>
              </View>
            </TouchableOpacity>
          </View>
        );
      });

      return (
        <View
          testID={bottomTabsOptions?.testID}
          style={{
            flexDirection: 'row',
            justifyContent: 'center',
            width: '100%',
            backgroundColor: '#F0F2F5',
          }}
        >
          {buttons}
        </View>
      );
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
          <Component componentId={this.props.layoutNode.nodeId} />
          {this.renderTabBar()}
        </View>
      );
    }
  }
);
