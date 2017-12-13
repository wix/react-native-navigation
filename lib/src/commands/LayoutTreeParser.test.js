const SimpleLayouts = require('./SimpleLayouts');
const LayoutTreeParser = require('./LayoutTreeParser');

describe('LayoutTreeParser', () => {
  let uut;

  beforeEach(() => {
    uut = new LayoutTreeParser();
  });

  describe('parseFromSimpleJSON', () => {
    it('parses single screen', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.singleScreenApp))
        .toEqual({
          type: 'ContainerStack',
          children: [
            {
              type: 'Container',
              data: {
                name: 'com.example.MyScreen'
              },
              children: []
            }
          ]
        });
    });

    it('parses single screen with props', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.singleScreenWithAditionalParams))
        .toEqual({
          type: 'ContainerStack',
          children: [
            {
              type: 'Container',
              children: [],
              data: {
                name: 'com.example.MyScreen',
                passProps: SimpleLayouts.passProps,
                style: {},
                buttons: {}
              }
            }
          ]
        });
      const parsedPropsFn = uut.parseFromSimpleJSON(SimpleLayouts.singleScreenWithAditionalParams)
        .children[0].data.passProps.fnProp;
      expect(parsedPropsFn).toBe(SimpleLayouts.passProps.fnProp);
      expect(parsedPropsFn()).toEqual('Hello from a function');
    });

    it('parses tab based', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.tabBasedApp))
        .toEqual({
          type: 'BottomTabs',
          children: [
            {
              type: 'ContainerStack',
              children: [
                {
                  type: 'Container',
                  children: [],
                  data: {
                    name: 'com.example.ATab'
                  }
                }
              ]
            },
            {
              type: 'ContainerStack',
              children: [
                {
                  type: 'Container',
                  children: [],
                  data: {
                    name: 'com.example.SecondTab'
                  }
                }
              ]
            },
            {
              type: 'ContainerStack',
              children: [
                {
                  type: 'Container',
                  children: [],
                  data: {
                    name: 'com.example.ATab'
                  }
                }
              ]
            }
          ]
        });
    });

    it('parses side menus', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.singleWithSideMenu))
        .toEqual({
          type: 'SideMenuRoot',
          children: [
            {
              type: 'SideMenuLeft',
              children: [
                {
                  type: 'Container',
                  data: {
                    name: 'com.example.SideMenu'
                  },
                  children: []
                }
              ]
            },
            {
              type: 'SideMenuCenter',
              children: [
                {
                  type: 'ContainerStack',
                  children: [
                    {
                      type: 'Container',
                      data: {
                        name: 'com.example.MyScreen'
                      },
                      children: []
                    }
                  ]
                }
              ]
            }
          ]
        });
    });

    it('parses side menu right', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.singleWithRightSideMenu))
        .toEqual({
          type: 'SideMenuRoot',
          children: [
            {
              type: 'SideMenuCenter',
              children: [
                {
                  type: 'ContainerStack',
                  children: [
                    {
                      type: 'Container',
                      data: {
                        name: 'com.example.MyScreen'
                      },
                      children: []
                    }
                  ]
                }
              ]
            },
            {
              type: 'SideMenuRight',
              children: [
                {
                  type: 'Container',
                  data: {
                    name: 'com.example.SideMenu'
                  },
                  children: []
                }
              ]
            }
          ]
        });
    });

    it('parses both side menus', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.singleWithBothMenus))
        .toEqual({
          type: 'SideMenuRoot',
          children: [
            {
              type: 'SideMenuLeft',
              children: [
                {
                  type: 'Container',
                  data: {
                    name: 'com.example.Menu1'
                  },
                  children: []
                }
              ]
            },
            {
              type: 'SideMenuCenter',
              children: [
                {
                  type: 'ContainerStack',
                  children: [
                    {
                      type: 'Container',
                      data: {
                        name: 'com.example.MyScreen'
                      },
                      children: []
                    }
                  ]
                }
              ]
            },
            {
              type: 'SideMenuRight',
              children: [
                {
                  type: 'Container',
                  data: {
                    name: 'com.example.Menu2'
                  },
                  children: []
                }
              ]
            }
          ]
        });
    });

    it('parses bottomTabs with side menus', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.tabBasedWithBothSideMenus))
        .toEqual({
          type: 'SideMenuRoot',
          children: [
            {
              type: 'SideMenuLeft',
              children: [
                {
                  type: 'Container',
                  data: {
                    name: 'com.example.Menu1'
                  },
                  children: []
                }
              ]
            },
            {
              type: 'SideMenuCenter',
              children: [
                {
                  type: 'BottomTabs',
                  children: [
                    {
                      type: 'ContainerStack',
                      children: [
                        {
                          type: 'Container',
                          data: {
                            name: 'com.example.FirstTab'
                          },
                          children: []
                        }
                      ]
                    },
                    {
                      type: 'ContainerStack',
                      children: [
                        {
                          type: 'Container',
                          data: {
                            name: 'com.example.SecondTab'
                          },
                          children: []
                        }
                      ]
                    }
                  ]
                }
              ]
            },
            {
              type: 'SideMenuRight',
              children: [
                {
                  type: 'Container',
                  data: {
                    name: 'com.example.Menu2'
                  },
                  children: []
                }
              ]
            }
          ]
        });
    });

    it('parses bottomTabs with side menus', () => {
      expect(uut.parseFromSimpleJSON(SimpleLayouts.singleScreenWithTopTabs))
        .toEqual({
          type: 'TopTabsContainer',
          children: [
            {
              type: 'TopTab',
              data: {
                name: 'navigation.playground.TextScreen'
              },
              children: []
            },
            {
              type: 'TopTab',
              data: {
                name: 'navigation.playground.TextScreen'
              },
              children: []
            },
            {
              type: 'TopTab',
              data: {
                name: 'navigation.playground.TextScreen'
              },
              children: []
            }
          ]
        });
    });
  });

  describe('createContainer', () => {
    it('creates container object with passed data', () => {
      expect(uut._createContainer({ foo: 'bar' })).toEqual({ type: 'Container', data: { foo: 'bar' }, children: [] });
    });
  });

  describe('createDialogContainer', () => {
    it('creates dialog container object with passed data', () => {
      expect(uut.createDialogContainer({ foo: 'bar' })).toEqual({ type: 'CustomDialog', data: { foo: 'bar' }, children: [] });
    });
  });
});
