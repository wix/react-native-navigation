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

const singleScreenWithTopTabs = {
  topTabs: [
    {
      name: 'navigation.playground.TextScreen'
    },
    {
      name: 'navigation.playground.TextScreen'
    },
    {
      name: 'navigation.playground.TextScreen'
    }
  ]
};

module.exports = {
  singleScreenApp,
  passProps,
  singleScreenWithAditionalParams,
  tabBasedApp,
  singleWithSideMenu,
  singleWithRightSideMenu,
  singleWithBothMenus,
  tabBasedWithBothSideMenus,
  singleScreenWithTopTabs
};
