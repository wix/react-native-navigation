import { merge } from 'lodash';
import {
  CommandName,
  Layout,
  LayoutComponent,
  Navigation,
  NavigationButtonPressedEvent,
  Options,
  OptionsTopBar,
} from 'react-native-navigation';
import flags from '../flags';
import testIDs from '../testIDs';

const { DISMISS_MODAL_TOPBAR_BTN } = testIDs;

const colors = [
  '#fff1e6',
  '#6d6875',
  '#f4a261',
  '#e76f51',
  '#ffafcc',
  '#e63946',
  '#52b788',
  '#2a9d8f',
  '#a8dadc',
  '#0077b6',
  '#1d3557',
];

export default function addProcessors() {
  addDismissModalProcessor();
  if (flags.randomizeComponentBackgroundColor) addComponentBackgroundColorProcessor();
}

const addDismissModalProcessor = () => {
  Navigation.addOptionProcessor<OptionsTopBar>(
    'topBar',
    (topBar: OptionsTopBar, commandName: string): OptionsTopBar => {
      if (commandName === 'showModal') {
        if (!topBar.leftButtons) {
          topBar.leftButtons = [
            {
              testID: DISMISS_MODAL_TOPBAR_BTN,
              id: 'dismissModalButton',
              icon: require('../../img/x.png'),
            },
          ];
        }
      }
      return topBar;
    }
  );

  Navigation.events().registerNavigationButtonPressedListener(
    (event: NavigationButtonPressedEvent) => {
      if (event.buttonId === 'dismissModalButton') {
        Navigation.dismissModal(event.componentId);
      }
    }
  );
};

const addComponentBackgroundColorProcessor = () => {
  Navigation.addLayoutProcessor((layout: Layout, command: CommandName) => {
    if (command === CommandName.ShowOverlay) return layout;
    if (layout.component) {
      applyColorOnComponent(layout.component);
    }
    if (layout.stack) {
      applyColorOnChildComponent(layout.stack.children as Layout[]);
    }
    return layout;
  });
};
function applyColorOnComponent(component: LayoutComponent<{}>) {
  component.options = merge(component.options, {
    layout: {
      componentBackgroundColor: colors[Math.floor(Math.random() * colors.length)],
    },
  } as Options);
}

function applyColorOnChildComponent(children: Layout[]) {
  children.forEach((child: Layout) => {
    if (child.component) {
      applyColorOnComponent(child.component);
    }
  });
}
