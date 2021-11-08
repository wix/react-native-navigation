import React from 'react';
import { NavigationComponentProps } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from './../services/Navigation';
import Screens from './Screens';
import testIDs from '../testIDs';
import { logLifecycleEvent } from './StaticLifecycleOverlay';

const {
  NAVIGATION_TAB,
  SET_MULTIPLE_ROOTS_BTN,
  SET_ROOT_BTN,
  LAYOUTS_TAB,
  SET_ROOT_HIDES_BOTTOM_TABS_BTN,
  SET_ROOT_WITH_STACK_HIDES_BOTTOM_TABS_BTN,
  SET_ROOT_WITHOUT_STACK_HIDES_BOTTOM_TABS_BTN,
  SET_ROOT_WITH_BUTTONS,
  ROUND_BUTTON,
} = testIDs;

export default class SetRootScreen extends React.Component<NavigationComponentProps> {
  static options() {
    return {
      topBar: {
        title: {
          text: 'Navigation',
        },
        rightButtons: [
          {
            id: 'ROUND',
            testID: ROUND_BUTTON,
            component: {
              id: 'ROUND_COMPONENT',
              name: Screens.RoundButton,
              passProps: {
                title: 'Two',
                timesCreated: 1,
              },
            },
          },
        ],
      },
      bottomTab: {
        text: 'Navigation',
        icon: require('../../img/navigation.png'),
        testID: NAVIGATION_TAB,
      },
    };
  }

  unmounted = false;

  render() {
    return (
      <Root componentId={this.props.componentId}>
        <Button label="Set Root" testID={SET_ROOT_BTN} onPress={this.setSingleRoot} />
        <Button
          label="Set Multiple Roots"
          testID={SET_MULTIPLE_ROOTS_BTN}
          onPress={this.setMultipleRoot}
        />
        <Button
          label="Set Root - hides bottomTabs"
          testID={SET_ROOT_HIDES_BOTTOM_TABS_BTN}
          onPress={this.setRootHidesBottomTabs}
        />
        <Button
          label="Set Root with deep stack - hides bottomTabs"
          testID={SET_ROOT_WITH_STACK_HIDES_BOTTOM_TABS_BTN}
          onPress={this.setRootWithStackHidesBottomTabs}
        />
        <Button
          label="Set Root without stack - hides bottomTabs"
          testID={SET_ROOT_WITHOUT_STACK_HIDES_BOTTOM_TABS_BTN}
          onPress={this.setRootWithoutStackHidesBottomTabs}
        />
        <Button
          label="Set Root with buttons"
          testID={SET_ROOT_WITH_BUTTONS}
          onPress={this.setRootWithButtons}
        />
      </Root>
    );
  }

  componentWillUnmount() {
    this.unmounted = true;
  }

  setSingleRoot = async () => {
    await this.setRoot();
    logLifecycleEvent({
      text: `setRoot complete - previous root is${this.unmounted ? '' : ' not'} unmounted`,
    });
  };

  setMultipleRoot = async () => {
    await this.setRoot();
    await this.setRoot();
  };

  setRoot = async () =>
    await Navigation.setRoot({
      root: {
        stack: {
          id: 'stack',
          children: [
            {
              component: {
                id: 'component',
                name: Screens.Pushed,
              },
            },
          ],
        },
      },
    });

  setRootHidesBottomTabs = async () =>
    await Navigation.setRoot({
      root: {
        bottomTabs: {
          children: [
            {
              stack: {
                id: 'stack',
                children: [
                  {
                    component: {
                      id: 'component',
                      name: Screens.Pushed,
                      options: {
                        bottomTabs: {
                          visible: false,
                          animate: false,
                        },
                      },
                    },
                  },
                ],
              },
            },
          ],
          options: {
            bottomTabs: {
              testID: LAYOUTS_TAB,
            },
          },
        },
      },
    });

  setRootWithStackHidesBottomTabs = async () =>
    await Navigation.setRoot({
      root: {
        bottomTabs: {
          children: [
            {
              stack: {
                id: 'stack',
                children: [
                  {
                    component: {
                      id: 'component',
                      name: Screens.Pushed,
                    },
                  },
                  {
                    component: {
                      id: 'component2',
                      name: Screens.Pushed,
                      options: {
                        bottomTabs: {
                          visible: false,
                          animate: false,
                        },
                      },
                    },
                  },
                ],
              },
            },
          ],
          options: {
            bottomTabs: {
              testID: LAYOUTS_TAB,
            },
          },
        },
      },
    });

  setRootWithoutStackHidesBottomTabs = async () =>
    await Navigation.setRoot({
      root: {
        bottomTabs: {
          children: [
            {
              component: {
                id: 'component',
                name: Screens.Pushed,
                options: {
                  bottomTabs: {
                    visible: false,
                    animate: false,
                  },
                },
              },
            },
            {
              component: {
                id: 'component2',
                name: Screens.Pushed,
              },
            },
          ],
          options: {
            bottomTabs: {
              testID: LAYOUTS_TAB,
            },
          },
        },
      },
    });

  setRootWithButtons = () =>
    Navigation.setRoot({
      root: {
        stack: {
          options: {},
          children: [
            {
              component: {
                name: Screens.SetRoot,
                options: {
                  animations: {
                    setRoot: {
                      waitForRender: true,
                    },
                  },
                  topBar: {
                    rightButtons: [
                      {
                        id: 'ROUND',
                        testID: ROUND_BUTTON,
                        component: {
                          id: 'ROUND_COMPONENT',
                          name: Screens.RoundButton,
                          passProps: {
                            title: 'Two',
                            timesCreated: 1,
                          },
                        },
                      },
                    ],
                  },
                },
              },
            },
          ],
        },
      },
    });
}
