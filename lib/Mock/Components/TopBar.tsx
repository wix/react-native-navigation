import React, { Component } from 'react';
import { Button, View, Text } from 'react-native';
import {
  Navigation,
  OptionsTopBar,
  OptionsTopBarBackButton,
  OptionsTopBarButton,
} from 'react-native-navigation';
import ParentNode from '../Layouts/ParentNode';
import { LayoutStore } from '../Stores/LayoutStore';
import { NavigationButton } from './NavigationButton';
import { events } from '../Stores/EventsStore';

const DEFAULT_BACK_BUTTON_ID = 'RNN.back';

export interface TopBarProps {
  layoutNode: ParentNode;
  topBarOptions?: OptionsTopBar;
  backButtonOptions?: OptionsTopBarBackButton;
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
          {this.shouldRenderBackButton(this.props.layoutNode) && this.renderBackButton()}
        </View>
      );
    }
  }

  shouldRenderBackButton(layoutNode: ParentNode) {
    const backButtonVisible = layoutNode.resolveOptions().topBar?.backButton?.visible;
    return layoutNode.getStack()!.children.length > 1 && backButtonVisible !== false;
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
          if (backButtonOptions?.popStackOnPress === false) {
            events.invokeNavigationButtonPressed({
              buttonId: backButtonOptions?.id || DEFAULT_BACK_BUTTON_ID,
              componentId: this.props.layoutNode.nodeId,
            });
          } else {
            LayoutStore.pop(this.props.layoutNode.nodeId);
          }
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
