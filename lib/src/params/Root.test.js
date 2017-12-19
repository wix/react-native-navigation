const Root = require('./Root');

const CONTAINER = { name: 'myScreen' };
const SIDE_MENU = {
  left: {
    container: {
      name: 'navigation.playground.TextScreen',
      passProps: {
        text: 'This is a left side menu screen'
      }
    }
  },
  right: {
    container: {
      name: 'navigation.playground.TextScreen',
      passProps: {
        text: 'This is a right side menu screen'
      }
    }
  }
};
const BOTTOM_TABS = [
  {
    container: {
      name: 'navigation.playground.TextScreen',
      passProps: {
        text: 'This is a side menu center screen tab 1'
      }
    }
  },
  {
    container: {
      name: 'navigation.playground.TextScreen',
      passProps: {
        text: 'This is a side menu center screen tab 2'
      }
    }
  },
  {
    container: {
      name: 'navigation.playground.TextScreen',
      passProps: {
        text: 'This is a side menu center screen tab 3'
      }
    }
  }
];

describe('Root', () => {
  it('Parses Root', () => {
    const uut = new Root(simpleRoot());
    expect(uut.container.name).toEqual(CONTAINER.name);
  });

  it('parses root with sideMenu', () => {
    const uut = new Root(rootWithSideMenu());
    expect(uut.container.name).toEqual(CONTAINER.name);
    expect(uut.sideMenu).toEqual(SIDE_MENU);
  });

  it('parses root with BottomTabs', () => {
    const uut = new Root(rootWithBottomTabs());
    expect(uut.bottomTabs).toEqual(BOTTOM_TABS);
  });
});

function rootWithBottomTabs() {
  return {
    container: CONTAINER,
    bottomTabs: BOTTOM_TABS
  };
}

function simpleRoot() {
  return { container: CONTAINER };
}

function rootWithSideMenu() {
  return {
    container: CONTAINER,
    sideMenu: SIDE_MENU
  };
}
