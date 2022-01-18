import { NavigationComponent, NavigationComponentProps, Options } from 'react-native-navigation';
import Navigation from '../services/Navigation';
import React from 'react';

import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';
import { stack } from '../commons/Layouts';
interface Props extends NavigationComponentProps {
  enablePushBottomTabs: boolean;
}
export default class AttachedOverlaysScreen extends NavigationComponent<Props> {
  public static defaultProps = {
    enablePushBottomTabs: true,
  };

  static options(): Options {
    return {
      topBar: {
        title: {
          text: 'Attached Overlays screen',
        },
        rightButtons: [
          {
            text: 'Hit',
            id: 'HitRightButton',
          },
        ],
        backButton: {
          testID: testIDs.BACK_BUTTON,
        },
      },
      layout: {
        orientation: ['portrait', 'landscape'],
      },
    };
  }

  constructor(props: Props) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button
          label="Show on BottomTabs under TopBar Button"
          testID={testIDs.SHOW_TOOLTIP_MAIN_BTMTABS_TPBAR_HIT}
          onPress={async () => this.showAttachedOverlay('bottomTabs', 'HitRightButton', 'bottom')}
        />
        <Button
          label="Show on LayoutsStack under TopBar Button"
          testID={testIDs.SHOW_TOOLTIP_LAYOUT_STACK_TPBAR_HIT}
          onPress={async () => this.showAttachedOverlay('LayoutsStack', 'HitRightButton', 'bottom')}
        />
        <Button
          label="Show on this component under TopBar Button"
          testID={testIDs.SHOW_TOOLTIP_COMPONENT_TPBAR_HIT}
          onPress={async () =>
            this.showAttachedOverlay(this.props.componentId, 'HitRightButton', 'bottom')
          }
        />
        <Button
          label="Show on this component no anchor"
          testID={testIDs.SHOW_TOOLTIP_COMPONENT_NO_ANCHOR_TPBAR_HIT}
          onPress={async () => this.showAttachedOverlay(this.props.componentId, undefined)}
        />
        <Button label="Push a screen " testID={testIDs.PUSH_BTN} onPress={this.push} />
        <Button
          label="Push a screen different TopBar Buttons "
          testID={testIDs.PUSH_PUSHED_SCREEN}
          onPress={this.pushNoButtons}
        />
        <Button label="showModal" testID={testIDs.MODAL_BTN} onPress={this.showModal} />
        <Button label="Extra Flows" onPress={this.pushExtraFlows} />
      </Root>
    );
  }
  pushExtraFlows = async () => {
    return await Navigation.push(this.props.componentId, Screens.AttachedOverlaysExtra);
  };
  dismissTooltip = async (compId: string) => {
    return await Navigation.dismissOverlay(compId);
  };

  showAttachedOverlay = async (
    layoutId: string,
    anchor?: string,
    gravity: 'top' | 'bottom' | 'left' | 'right' = 'top'
  ) => {
    const screen = !anchor ? Screens.OverlayAlert : Screens.Tooltip;
    await Navigation.showOverlay(
      screen,
      {
        layout: { componentBackgroundColor: 'transparent' },
        overlay: {
          interceptTouchOutside: false,
          attach: {
            layoutId: layoutId,
            anchor: !anchor ? undefined : { id: anchor!, gravity },
          },
        },
      },
      {
        dismissTooltip: this.dismissTooltip,
      }
    );
  };

  showModal = async () => {
    await Navigation.showModal(stack(Screens.AttachedOverlaysScreen));
  };

  push = async () => {
    await Navigation.push(this.props.componentId, Screens.AttachedOverlaysScreen);
  };

  pushNoButtons = async () => {
    await Navigation.push(this.props.componentId, Screens.Pushed);
  };
  pushBottomTabs = async () => {
    await Navigation.push(this.props.componentId, {
      bottomTabs: {
        id: 'innerBottomTabs',
        children: [
          {
            component: {
              name: Screens.AttachedOverlaysScreen,
              passProps: {
                enablePushBottomTabs: false,
              },
              options: {
                bottomTab: {
                  icon: require('../../img/whatshot.png'),
                  id: 'innerTooltipsScreenBottomTab',
                  text: 'TooltipsScreen',
                  testID: testIDs.ATTACHED_OVERLAYS_SCREEN,
                },
              },
            },
          },
          {
            component: {
              name: Screens.Pushed,
              options: {
                bottomTab: {
                  icon: require('../../img/plus.png'),
                  id: 'non-press-tab',
                  selectTabOnPress: false,
                  text: 'Tab 3',
                  testID: testIDs.THIRD_TAB_BAR_BTN,
                },
              },
            },
          },
        ],
        options: {
          hardwareBackButton: {
            bottomTabsOnPress: 'previous',
          },
          bottomTabs: {
            testID: testIDs.BOTTOM_TABS,
          },
        },
      },
    });
  };
}
