import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import { Navigation, OptionsTopBarButton } from 'react-native-navigation';
import { DEFAULT_BACK_BUTTON_TEST_ID } from './constants';
import { ComponentProps } from './ComponentProps';
import { LayoutStore } from './LayoutStore';
import { NavigationButton } from './NavigationButton';

export const TopBar = class extends Component<ComponentProps> {
  constructor(props: ComponentProps) {
    super(props);
  }

  render() {
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
          {this.props.backButton && this.renderBackButton()}
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
        testID={backButtonOptions?.testID || DEFAULT_BACK_BUTTON_TEST_ID}
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
};
