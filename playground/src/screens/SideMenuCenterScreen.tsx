/* eslint-disable react-native/no-inline-styles */
import React, { useEffect, useState } from 'react';
import { NavigationFunctionComponent } from 'react-native-navigation';
import Root from '../components/Root';
import Button from '../components/Button';
import Navigation from '../services/Navigation';
import testIDs from '../testIDs';
// import Screens from '../Screens';

import { Text } from 'react-native-ui-lib';
const {
  OPEN_LEFT_SIDE_MENU_BTN,
  OPEN_RIGHT_SIDE_MENU_BTN,
  CHANGE_LEFT_SIDE_MENU_WIDTH_BTN,
  CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN,
} = testIDs;
export interface MaProps {
  label: string;
}
const SideMenuCenterScreen: NavigationFunctionComponent = (props) => {
  const [stam, setStam] = useState<undefined | string>(undefined);
  const [topBarVisible, setTopBarVisible] = useState(false);

  useEffect(() => {
    if (stam) {
      Navigation.mergeOptions(props.componentId, {
        statusBar: {
          backgroundColor: '#FFFFFF',
          style: 'dark',
        },
      });
    }
  }, [stam, props.componentId]);

  useEffect(() => {
    setTimeout(() => {
      setStam('sss');
    }, 777);
  }, []);

  useEffect(() => {
    setTimeout(() => {
      //setStam('sss');
    }, 1000);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      //setStam('sss');
    }, 500);
  }, []);

  useEffect(() => {
    setTimeout(() => {
      //setStam('sss');
    }, 666);
  }, []);

  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);
  useEffect(() => {
    setTimeout(() => {
      // setStam('sss');
    }, 777);
  }, []);

  return (
    <Root componentId={props.componentId} style={{ backgroundColor: '#ffffff' }}>
      <Button
        label="Open Left"
        testID={OPEN_LEFT_SIDE_MENU_BTN}
        onPress={async () => {
          await Navigation.push(props.componentId, 'Pushed', {
            statusBar: {
              backgroundColor: '#FFFFFF',
              style: 'dark',
            },
          });
        }}
      />
      <Button
        label="Open Right"
        testID={OPEN_RIGHT_SIDE_MENU_BTN}
        onPress={() => {
          setTopBarVisible(!topBarVisible);
        }}
      />
      <Text color="white">{`top bar visible ${topBarVisible}`}</Text>
      <Button label="Change Left Drawer Width" testID={CHANGE_LEFT_SIDE_MENU_WIDTH_BTN} />
      <Button label="Change Right Drawer Width" testID={CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN} />
    </Root>
  );
};

export default SideMenuCenterScreen;
// @ts-ignore TSC is unhappy as leftButtons is defined as an object instead of an array. Declaring buttons as a single object is not reflected in Options, but still supported.
// export default class SideMenuCenterScreen extends NavigationComponent {
//   static options() {
//     return {
//       sideMenu: {
//         left: {
//           width: 250,
//         },
//         right: {
//           width: 250,
//         },
//       },
//       topBar: {
//         testID: CENTER_SCREEN_HEADER,
//         title: {
//           text: 'Center',
//         },
//         leftButtons: {
//           id: 'sideMenu',
//           icon: require('../../img/menu.png'),
//         },
//       },
//     };
//   }

//   constructor(props: NavigationComponentProps) {
//     super(props);
//     Navigation.events().bindComponent(this);
//   }

//   navigationButtonPressed({ buttonId }: NavigationButtonPressedEvent) {
//     if (buttonId === 'sideMenu') this.open('left');
//   }

//   render() {
//     return (
//       <Root componentId={this.props.componentId}>
//         <Button
//           label="Open Left"
//           testID={OPEN_LEFT_SIDE_MENU_BTN}
//           onPress={() => this.open('left')}
//         />
//         <Button
//           label="Open Right"
//           testID={OPEN_RIGHT_SIDE_MENU_BTN}
//           onPress={() => this.open('right')}
//         />
//         <Button
//           label="Change Left Drawer Width"
//           testID={CHANGE_LEFT_SIDE_MENU_WIDTH_BTN}
//           onPress={() => this.changeDrawerWidth('left', 50)}
//         />
//         <Button
//           label="Change Right Drawer Width"
//           testID={CHANGE_RIGHT_SIDE_MENU_WIDTH_BTN}
//           onPress={() => this.changeDrawerWidth('right', 50)}
//         />
//       </Root>
//     );
//   }

//   open = (side: 'left' | 'right') =>
//     Navigation.mergeOptions(this, {
//       sideMenu: {
//         [side]: {
//           visible: true,
//         },
//       },
//     });

//   changeDrawerWidth = (side: 'left' | 'right', newWidth: number) => {
//     Navigation.mergeOptions(this, {
//       sideMenu: {
//         [side]: {
//           width: newWidth,
//         },
//       },
//     });
//   };
// }
