import { NavigationComponent, Options } from 'react-native-navigation';
import Navigation from '../services/Navigation';
import React from 'react';

import Root from '../components/Root';
import Button from '../components/Button';
import Screens from './Screens';
import testIDs from '../testIDs';

export default class TooltipsScreen extends NavigationComponent {
  static options(): Options {
    return {
      topBar: {
        title: {
          text: 'Tooltips screen',
        },
        rightButtons: [
          {
            text: 'Hit',
            id: 'HitRightButton',
          },
        ],
      },
      layout: {
        orientation: ['portrait', 'landscape'],
      },
    };
  }

  constructor(props: any) {
    super(props);
    Navigation.events().bindComponent(this);
  }

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button
          label="showToolTips on BottomTabs TopBar"
          onPress={async () => this.showTooltips('bottomTabs', 'HitRightButton', 'bottom')}
        />
        <Button
          label="showToolTips on inner BottomTabs bottomTab"
          onPress={async () => this.showTooltips('innerBottomTabs', 'non-press-tab')}
        />
        <Button
          label="showToolTips on LayoutsBottomTab BottomTab"
          onPress={async () => this.showTooltips('bottomTabs', 'LayoutsBottomTab')}
        />
        <Button
          label="showToolTips on OptionsBottomTab BottomTab"
          onPress={async () => this.showTooltips('bottomTabs', 'OptionsBottomTab')}
        />
        <Button
          label="showToolTips on NavigationStack BottomTab"
          onPress={async () => this.showTooltips('bottomTabs', 'NavigationBottomTab')}
        />
        <Button
          label="showToolTips on Stack TopBar"
          onPress={async () => this.showTooltips('LayoutsStack', 'HitRightButton', 'bottom')}
        />
        <Button
          label="showToolTips on Component"
          onPress={async () => this.showTooltips('LayoutsTabMainComponent', 'LayoutsBottomTab')}
        />
        <Button
          label="Pushed BottomTabs"
          testID={testIDs.BOTTOM_TABS_BTN}
          onPress={this.pushBottomTabs}
        />
      </Root>
    );
  }
  dismissTooltip = async (compId: string) => {
    return await Navigation.dismissOverlay(compId);
  };

  showTooltips = async (
    layoutId: string,
    anchor: string,
    gravity: 'top' | 'bottom' | 'left' | 'right' = 'top'
  ) => {
    const res = await Navigation.showOverlay(
      Screens.Tooltip,
      {
        overlay: {
          attach: {
            layoutId: layoutId,
            anchor: {
              id: anchor,
              gravity: gravity,
            },
          },
        },
      },
      {
        dismissTooltip: this.dismissTooltip,
      }
    );
    console.log('tooltip ', res);
  };

  pushBottomTabs = async () => {
    await Navigation.push(this.props.componentId, {
      bottomTabs: {
        id: 'innerBottomTabs',
        children: [
          {
            component: {
              name: Screens.Layouts,
              options: {
                bottomTab: {
                  icon: require('../../img/whatshot.png'),
                  id: 'innerLayoutsBottomTab',
                  text: 'Layouts',
                  testID: testIDs.LAYOUTS_TAB,
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
