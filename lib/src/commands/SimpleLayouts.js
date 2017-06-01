export const singleScreenApp = {
  container: {
    name: 'com.example.MyScreen'
  }
};

export const passProps = {
  strProp: 'string prop',
  numProp: 12345,
  objProp: { inner: { foo: 'bar' } },
  fnProp: () => 'Hello from a function'
};

export const singleScreenWithAditionalParams = {
  container: {
    name: 'com.example.MyScreen',
    passProps: passProps,
    style: {},
    buttons: {}
  }
};

export const tabBasedApp = {
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

export const singleWithSideMenu = {
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

export const singleWithRightSideMenu = {
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

export const singleWithBothMenus = {
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

export const tabBasedWithBothSideMenus = {
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
