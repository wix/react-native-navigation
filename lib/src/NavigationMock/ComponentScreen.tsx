import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import { Navigation, OptionsTopBarButton } from 'react-native-navigation';
import { ComponentProps } from './ComponentProps';
import { VISIBLE_SCREEN_TEST_ID } from './constants';
import { LayoutStore } from './LayoutStore';
import { NavigationButton } from './NavigationButton';
import { connect } from 'remx';

export const ComponentScreen = connect()(
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

    renderTopBar() {
      if (!this.props.stack) return null;

      const topBarOptions = this.props.layoutNode.resolveOptions().topBar;
      if (topBarOptions?.visible === false) return null;
      else {
        const component = topBarOptions?.title?.component;
        return (
          <View testID={topBarOptions?.testID}>
            <Text>{topBarOptions?.title?.text}</Text>
            <Text>{topBarOptions?.subtitle?.text}</Text>
            {this.renderButtons(topBarOptions?.leftButtons)}
            {this.renderButtons(topBarOptions?.rightButtons)}
            {component &&
              //@ts-ignore
              this.renderComponent(component.componentId!, component.name)}
          </View>
        );
      }
    }

    renderButtons(buttons: OptionsTopBarButton[] = []) {
      return buttons.map((button) => {
        return (
          <NavigationButton
            button={button}
            key={button.id}
            componentId={this.props.layoutNode.nodeId}
          />
        );
      });
    }

    renderBackButton() {
      const backButtonOptions = this.props.layoutNode.resolveOptions().topBar?.backButton;
      return (
        <Button
          testID={backButtonOptions?.testID}
          title={backButtonOptions && backButtonOptions.title ? backButtonOptions.title : ''}
          onPress={() => {
            LayoutStore.pop(this.props.layoutNode.nodeId);
          }}
        />
      );
    }

    renderComponent(id: string, name: string, testID?: string) {
      //@ts-ignore
      const Component = Navigation.store.getComponentClassForName(name)!();
      //@ts-ignore
      const props = Navigation.store.getPropsForId(id);
      return (
        <View key={id} testID={testID}>
          <Component {...props} componentId={id} />
        </View>
      );
    }

    render() {
      //@ts-ignore
      const Component = Navigation.store.getWrappedComponent(this.props.layoutNode.data.name);
      return (
        <View testID={this.isVisible() ? VISIBLE_SCREEN_TEST_ID : undefined}>
          {this.props.backButton && this.renderBackButton()}
          {this.renderTopBar()}
          {this.renderTabBar()}
          {this.isVisible() && <View>{/* <Overdlays /> */}</View>}
          <Component componentId={this.props.layoutNode.nodeId} />
        </View>
      );
    }
  }
);
