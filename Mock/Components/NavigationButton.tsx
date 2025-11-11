import React, { Component } from 'react';
import { Button, TouchableOpacity } from 'react-native';
import { Navigation, OptionsTopBarButton } from 'react-native-navigation';
import { events } from '../Stores/EventsStore';

interface ButtonProps {
  button: OptionsTopBarButton;
  componentId: string;
}

export const NavigationButton = class extends Component<ButtonProps> {
  ref = undefined;
  render() {
    const { button, componentId } = this.props;
    if (button.component) return this.renderButtonComponent();
    return (
      <Button
        testID={button.testID}
        key={button.id}
        title={button.text || ''}
        disabled={button.enabled === false}
        onPress={() =>
          button.enabled !== false &&
          events.invokeNavigationButtonPressed({
            buttonId: button.id,
            componentId,
          })
        }
      />
    );
  }

  renderButtonComponent() {
    const { button, componentId } = this.props;
    // @ts-ignore
    const buttonComponentId = button.component!.componentId;
    // @ts-ignore
    const ComponentClass = Navigation.mock.store.getComponentClassForName(button.component.name);
    if (!ComponentClass) {
      throw new Error(`Cannot find registered component for: ${button.component?.name}`);
    }
    const ButtonComponent = ComponentClass();
    const props = Navigation.mock.store.getPropsForId(buttonComponentId);
    return (
      <TouchableOpacity
        onPress={() => {
          if (this.ref) {
            this.invokeOnClick(
              // @ts-ignore
              (this.ref!._reactInternalFiber || this.ref!._reactInternals).return.stateNode
            );
          }

          events.invokeNavigationButtonPressed({
            buttonId: button.id,
            componentId: componentId,
          });
        }}
        testID={button.testID}
      >
        <ButtonComponent
          key={buttonComponentId}
          {...props}
          componentId={buttonComponentId}
          ref={(ref: any) => (this.ref = ref)}
        />
      </TouchableOpacity>
    );
  }

  invokeOnClick(stateNode: any) {
    if (stateNode.children) {
      // @ts-ignore
      stateNode.children.forEach((instance) => {
        if (
          instance.internalInstanceHandle &&
          instance.internalInstanceHandle.stateNode.props.onClick
        ) {
          instance.internalInstanceHandle.stateNode.props.onClick();
        }

        this.invokeOnClick(instance);
      });
    }
  }
};
