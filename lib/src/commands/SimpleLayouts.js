const singleScreenApp = {
  container: {
    name: 'com.example.MyScreen'
  }
};

const passProps = {
  strProp: 'string prop',
  numProp: 12345,
  objProp: { inner: { foo: 'bar' } },
  fnProp: () => 'Hello from a function'
};

const singleScreenWithAditionalParams = {
  container: {
    name: 'com.example.MyScreen',
    passProps,
    style: {},
    buttons: {}
  }
};

const tabBasedApp = {
  bottomTabs: [
    {
      container: {
        name: 'com.example.ATab'
      },
      tabItem: {
        label: 'Tab 1',
        labelColor: '#ccc',
        selectedLabelColor: 'blue',
        icon: 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
        selectedIcon: 'data:image/png;base64, iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==',
        iconColor: '#ccc',
        selectedIconColor: 'blue'
      }
    },
    {
      container: {
        name: 'com.example.SecondTab'
      }
    },
    {
      container: {
        name: 'com.example.ATab'
      }
    }
  ]
};

const singleWithSideMenu = {
  container: {
    name: 'com.example.MyScreen'
  },
  sideMenu: {
    left: {
      container: {
        name: 'com.example.SideMenu'
      }
    }
  }
};

const singleWithRightSideMenu = {
  container: {
    name: 'com.example.MyScreen'
  },
  sideMenu: {
    right: {
      container: {
        name: 'com.example.SideMenu'
      }
    }
  }
};

const singleWithBothMenus = {
  container: {
    name: 'com.example.MyScreen'
  },
  sideMenu: {
    left: {
      container: {
        name: 'com.example.Menu1'
      }
    },
    right: {
      container: {
        name: 'com.example.Menu2'
      }
    }
  }
};

const tabBasedWithBothSideMenus = {
  bottomTabs: [
    {
      container: {
        name: 'com.example.FirstTab'
      }
    },
    {
      container: {
        name: 'com.example.SecondTab'
      }
    }
  ],
  sideMenu: {
    left: {
      container: {
        name: 'com.example.Menu1'
      }
    },
    right: {
      container: {
        name: 'com.example.Menu2'
      }
    }
  }
};

module.exports = {
  singleScreenApp,
  passProps,
  singleScreenWithAditionalParams,
  tabBasedApp,
  singleWithSideMenu,
  singleWithRightSideMenu,
  singleWithBothMenus,
  tabBasedWithBothSideMenus
};
