const passProps = {
  strProp: 'string prop',
  numProp: 12345,
  objProp: { inner: { foo: 'bar' } },
  fnProp: () => 'Hello from a function'
};

const options = {
  topBar: {
    title: 'Hello1'
  }
};

const singleComponent = {
  component: {
    name: 'MyReactComponent',
    options,
    passProps
  }
};

const stackWithTopBar = {
  stack: {
    children: [
      {
        component: {
          name: 'MyReactComponent1'
        }
      },
      {
        component: {
          name: 'MyReactComponent2',
          options
        }
      }
    ],
    options
  }
};

const bottomTabs = {
  bottomTabs: {
    children: [
      stackWithTopBar,
      stackWithTopBar,
      {
        component: {
          name: 'MyReactComponent1'
        }
      }
    ]
  }
};

const sideMenu = {
  sideMenu: {
    left: singleComponent,
    center: stackWithTopBar,
    right: singleComponent
  }
};

const topTabs = {
  topTabs: {
    children: [
      singleComponent,
      singleComponent,
      singleComponent,
      singleComponent,
      stackWithTopBar
    ],
    options
  }
};

const complexLayout = {
  sideMenu: {
    left: singleComponent,
    center: {
      bottomTabs: {
        children: [
          stackWithTopBar,
          stackWithTopBar,
          {
            stack: {
              children: [
                {
                  topTabs: {
                    children: [
                      stackWithTopBar,
                      stackWithTopBar,
                      {
                        topTabs: {
                          options,
                          children: [
                            singleComponent,
                            singleComponent,
                            singleComponent,
                            singleComponent,
                            stackWithTopBar
                          ]
                        }
                      }
                    ]
                  }
                }
              ]
            }
          }
        ]
      }
    }
  }
};

module.exports = {
  passProps,
  options,
  singleComponent,
  stackWithTopBar,
  bottomTabs,
  sideMenu,
  topTabs,
  complexLayout
};
