import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import { Navigation, OptionsTopBarButton } from '../..';
import { OptionsTopBar, OptionsTopBarBackButton } from '../../interfaces/Options';
import ParentNode from '../Layouts/ParentNode';
import { LayoutStore } from '../Stores/LayoutStore';
import { NavigationButton } from './NavigationButton';

export interface TopBarProps {
  layoutNode: ParentNode;
  topBarOptions?: OptionsTopBar;
  backButtonOptions?: OptionsTopBarBackButton;
  renderBackButton: boolean;
}

export const TopBar = class extends Component<TopBarProps> {
  constructor(props: TopBarProps) {
    super(props);
  }

  render() {
    const topBarOptions = this.props.topBarOptions;
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
          {this.props.renderBackButton && this.renderBackButton()}
        </View>
      );
    }
  }

  renderButtons(buttons: OptionsTopBarButton[] = []) {
    return buttons.map((button, i: number) => {
      return (
        <NavigationButton
          button={button}
          key={button.id || i}
          componentId={this.props.layoutNode.nodeId}
        />
      );
    });
  }

  renderBackButton() {
    const backButtonOptions = this.props.backButtonOptions;
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
    const Component = Navigation.mock.store.getComponentClassForName(name)!();
    const props = Navigation.mock.store.getPropsForId(id);
    return (
      <View key={id} testID={testID}>
        <Component {...props} componentId={id} />
      </View>
    );
  }
};
