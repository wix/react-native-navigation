import testIDs from '../testIDs';
import Screens from './Screens';

import { stack } from '../commons/Layouts';

export default {
  children: [
    stack(Screens.FirstBottomTabsScreen),
    stack(
      {
        component: {
          name: Screens.SecondBottomTabsScreen,
        },
      },
      'SecondTab'
    ),
    {
      component: {
        name: Screens.Pushed,
        options: {
          bottomTab: {
            selectTabOnPress: false,
            text: 'Tab 3',
            testID: testIDs.THIRD_TAB_BAR_BTN,
          },
        },
      },
    },
  ],
};
