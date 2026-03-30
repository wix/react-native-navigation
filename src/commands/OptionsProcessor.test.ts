import { OptionsProcessor } from './OptionsProcessor';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { Store } from '../components/Store';
import { OptionProcessorsStore } from '../processors/OptionProcessorsStore';
import {
  Options,
  OptionsModalPresentationStyle,
  StackAnimationOptions,
} from '../interfaces/Options';
import { mock, when, instance, anyNumber, verify, anything } from 'ts-mockito';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';
import { Deprecations } from './Deprecations';
import { CommandName } from '../interfaces/CommandName';
import { OptionsProcessor as Processor } from '../interfaces/Processors';
import { DynamicColorIOS, Platform } from 'react-native';

describe('navigation options', () => {
  let uut: OptionsProcessor;
  let optionProcessorsRegistry: OptionProcessorsStore;
  const mockedStore = mock(Store) as Store;
  const store = instance(mockedStore) as Store;
  beforeEach(() => {
    const mockedAssetService = mock(AssetService) as AssetService;
    when(mockedAssetService.resolveFromRequire(anyNumber())).thenReturn({
      height: 100,
      scale: 1,
      uri: 'lol',
      width: 100,
    });
    const assetService = instance(mockedAssetService);

    const mockedColorService = mock(ColorService) as ColorService;
    when(mockedColorService.toNativeColor('red')).thenReturn(0xffff0000);
    when(mockedColorService.toNativeColor('green')).thenReturn(0xff00ff00);
    when(mockedColorService.toNativeColor('blue')).thenReturn(0xff0000ff);
    const colorService = instance(mockedColorService);
    optionProcessorsRegistry = new OptionProcessorsStore();

    let uuid = 0;
    const mockedUniqueIdProvider: UniqueIdProvider = mock(UniqueIdProvider);
    when(mockedUniqueIdProvider.generate(anything())).thenCall((prefix) => {
      return `${prefix}${++uuid}`;
    });

    uut = new OptionsProcessor(
      store,
      instance(mockedUniqueIdProvider),
      optionProcessorsRegistry,
      colorService,
      assetService,
      new Deprecations()
    );
  });

  it('processes old setRoot animation value to new enter exit format on Android', () => {
    Platform.OS = 'android';
    const options: Options = {
      animations: {
        setRoot: {
          enabled: false,
          translationY: {
            from: 0,
            to: 1,
            duration: 3,
          },
        },
      },
    };

    const expectedOptions: Options = {
      animations: {
        setRoot: {
          enter: {
            enabled: false,
            translationY: {
              from: 0,
              to: 1,
              duration: 3,
            },
          },
        },
      },
    };

    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual(expectedOptions);
  });

  describe('Modal Animation Options', () => {
    describe('Show Modal', () => {
      it('processes old options into new options,backwards compatibility ', () => {
        const options: Options = {
          animations: {
            showModal: {
              enabled: false,
              translationY: {
                from: 0,
                to: 1,
                duration: 3,
              },
            },
            dismissModal: {
              enabled: true,
              translationY: {
                from: 0,
                to: 1,
                duration: 3,
              },
            },
          },
        };

        const expected: Options = {
          animations: {
            showModal: {
              enter: {
                enabled: false,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
            dismissModal: {
              exit: {
                enabled: true,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
          },
        };
        uut.processOptions(CommandName.ShowModal, options);
        expect(options).toEqual(expected);
      });

      it('processes old enabled options into new options,backwards compatibility ', () => {
        const options: Options = {
          animations: {
            showModal: {
              enabled: false,
            },
            dismissModal: {
              enabled: true,
            },
          },
        };

        const expected: Options = {
          animations: {
            showModal: {
              enter: {
                enabled: false,
              },
            },
            dismissModal: {
              exit: {
                enabled: true,
              },
            },
          },
        };
        uut.processOptions(CommandName.ShowModal, options);
        expect(options).toEqual(expected);
      });

      it('should not process new options', () => {
        const options: Options = {
          animations: {
            showModal: {
              enter: {
                enabled: false,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
            dismissModal: {
              exit: {
                enabled: true,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
          },
        };
        const expected: Options = { ...options };
        uut.processOptions(CommandName.ShowModal, options);
        expect(options).toEqual(expected);
      });
    });

    describe('Dismiss Modal', () => {
      it('processes old options into new options,backwards compatibility ', () => {
        const options: Options = {
          animations: {
            showModal: {
              enabled: false,
              translationY: {
                from: 0,
                to: 1,
                duration: 3,
              },
            },
            dismissModal: {
              enabled: true,
              translationY: {
                from: 0,
                to: 1,
                duration: 3,
              },
            },
          },
        };

        const expected: Options = {
          animations: {
            showModal: {
              enter: {
                enabled: false,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
            dismissModal: {
              exit: {
                enabled: true,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
          },
        };
        uut.processOptions(CommandName.DismissModal, options);
        expect(options).toEqual(expected);
      });

      it('processes old enabled options into new options,backwards compatibility ', () => {
        const options: Options = {
          animations: {
            showModal: {
              enabled: false,
            },
            dismissModal: {
              enabled: true,
            },
          },
        };

        const expected: Options = {
          animations: {
            showModal: {
              enter: {
                enabled: false,
              },
            },
            dismissModal: {
              exit: {
                enabled: true,
              },
            },
          },
        };
        uut.processOptions(CommandName.DismissModal, options);
        expect(options).toEqual(expected);
      });

      it('should not process new options', () => {
        const options: Options = {
          animations: {
            showModal: {
              enter: {
                enabled: false,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
            dismissModal: {
              exit: {
                enabled: true,
                translationY: {
                  from: 0,
                  to: 1,
                  duration: 3,
                },
              },
            },
          },
        };
        const expected: Options = { ...options };
        uut.processOptions(CommandName.DismissModal, options);
        expect(options).toEqual(expected);
      });
    });
  });

  it('keeps original values if values were not processed', () => {
    const options: Options = {
      blurOnUnmount: false,
      popGesture: false,
      modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
    };
    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual({
      blurOnUnmount: false,
      popGesture: false,
      modalPresentationStyle: OptionsModalPresentationStyle.fullScreen,
    });
  });

  it('passes value to registered processor', () => {
    const options: Options = {
      topBar: {
        visible: true,
      },
    };

    optionProcessorsRegistry.addProcessor('topBar.visible', (value: boolean) => {
      return !value;
    });

    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual({
      topBar: {
        visible: false,
      },
    });
  });

  it('process options object with multiple values using registered processor', () => {
    const options: Options = {
      topBar: {
        visible: true,
        background: {
          translucent: true,
        },
      },
    };

    optionProcessorsRegistry.addProcessor('topBar.visible', (value: boolean) => {
      return !value;
    });

    optionProcessorsRegistry.addProcessor('topBar.background.translucent', (value: boolean) => {
      return !value;
    });

    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual({
      topBar: {
        visible: false,
        background: {
          translucent: false,
        },
      },
    });
  });

  it('passes commandName to registered processor', () => {
    const options: Options = {
      topBar: {
        visible: false,
      },
    };

    optionProcessorsRegistry.addProcessor('topBar.visible', (_value, commandName) => {
      expect(commandName).toEqual(CommandName.SetRoot);
    });

    uut.processOptions(CommandName.SetRoot, options);
  });

  it('passes props to registered processor', () => {
    const options: Options = {
      topBar: {
        visible: false,
      },
    };
    const props = {
      prop: 'prop',
    };
    const processor: Processor<boolean> = (_value, _commandName, passProps) => {
      expect(passProps).toEqual(props);
      return _value;
    };

    optionProcessorsRegistry.addProcessor('topBar.visible', processor);
    uut.processOptions(CommandName.SetRoot, options, props);
  });

  it('supports multiple registered processors', () => {
    const options: Options = {
      topBar: {
        visible: true,
      },
    };

    optionProcessorsRegistry.addProcessor('topBar.visible', () => false);
    optionProcessorsRegistry.addProcessor('topBar.visible', () => true);

    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual({
      topBar: {
        visible: true,
      },
    });
  });

  it('supports multiple registered processors deep props', () => {
    const options: Options = {
      topBar: {
        visible: false,
        background: {
          translucent: false,
        },
      },
      bottomTabs: {
        visible: false,
      },
    };

    optionProcessorsRegistry.addProcessor('topBar.visible', () => true);
    optionProcessorsRegistry.addProcessor('bottomTabs.visible', () => true);
    optionProcessorsRegistry.addProcessor('topBar.background.translucent', () => true);

    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual({
      topBar: {
        visible: true,
        background: {
          translucent: true,
        },
      },
      bottomTabs: {
        visible: true,
      },
    });
  });

  describe('color processor', () => {
    describe('Android', () => {
      beforeEach(() => {
        Platform.OS = 'android';
      });

      it('should not process undefined color', () => {
        const options: Options = {
          topBar: { background: { color: undefined } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: undefined } },
        });
      });

      it('PlatformColor should be passed to native as is', () => {
        const options: Options = {
          topBar: {
            background: {
              color: {
                // @ts-ignore
                resource_paths: ['@color/textColor'],
              },
            },
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: { resource_paths: ['@color/textColor'] } } },
        });
      });

      it('processes color keys', () => {
        const options: Options = {
          statusBar: { backgroundColor: 'red' },
          topBar: { background: { color: 'blue' } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          statusBar: { backgroundColor: { light: 0xffff0000, dark: 0xffff0000 } },
          topBar: { background: { color: { light: 0xff0000ff, dark: 0xff0000ff } } },
        });
      });

      it('processes null color', () => {
        const options: Options = {
          topBar: { background: { color: null } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: { light: 'NoColor', dark: 'NoColor' } } },
        });
      });

      it('processes color keys to ThemeColor', () => {
        const options: Options = {
          topBar: {
            background: { color: { light: 'blue', dark: 'red' } },
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: {
            background: { color: { light: 0xff0000ff, dark: 0xffff0000 } },
          },
        });
      });
    });

    describe('iOS', () => {
      beforeEach(() => {
        Platform.OS = 'ios';
      });

      it('processes color keys', () => {
        const options: Options = {
          statusBar: { backgroundColor: 'red' },
          topBar: { background: { color: 'blue' } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          statusBar: { backgroundColor: 0xffff0000 },
          topBar: { background: { color: 0xff0000ff } },
        });
      });

      it('should not process undefined color', () => {
        const options: Options = {
          topBar: { background: { color: undefined } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: undefined } },
        });
      });

      it('processes null color', () => {
        const options: Options = {
          topBar: { background: { color: null } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: 'NoColor' } },
        });
      });

      it('processes color keys to ThemeColor', () => {
        const options: Options = {
          statusBar: { backgroundColor: 'red' },
          topBar: {
            background: { color: { light: 'blue', dark: 'red' } },
            title: {
              color: undefined,
            },
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          statusBar: { backgroundColor: 0xffff0000 },
          topBar: {
            background: { color: { dynamic: { light: 0xff0000ff, dark: 0xffff0000 } } },
            title: {
              color: undefined,
            },
          },
        });
      });

      it('supports DynamicColorIOS', () => {
        const options: Options = {
          topBar: { background: { color: DynamicColorIOS({ light: 'red', dark: 'blue' }) } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: { dynamic: { light: 0xffff0000, dark: 0xff0000ff } } } },
        });
      });

      it('should not process undefined value', () => {
        const options: Options = {
          topBar: { background: { color: undefined } },
        };

        uut.processOptions(CommandName.SetRoot, options);
        expect(options).toEqual({
          topBar: { background: { color: undefined } },
        });
      });
    });
  });

  it('processes image keys', () => {
    const options: Options = {
      backgroundImage: 123,
      rootBackgroundImage: 234,
      bottomTab: { icon: 345, selectedIcon: 345 },
    };
    uut.processOptions(CommandName.SetRoot, options);
    expect(options).toEqual({
      backgroundImage: { height: 100, scale: 1, uri: 'lol', width: 100 },
      rootBackgroundImage: { height: 100, scale: 1, uri: 'lol', width: 100 },
      bottomTab: {
        icon: { height: 100, scale: 1, uri: 'lol', width: 100 },
        selectedIcon: { height: 100, scale: 1, uri: 'lol', width: 100 },
      },
    });
  });

  it('calls store if component has passProps', () => {
    const passProps = { some: 'thing' };
    const options = { topBar: { title: { component: { passProps, name: 'a' } } } };

    uut.processOptions(CommandName.SetRoot, options);

    verify(mockedStore.setPendingProps('CustomComponent1', passProps)).called();
  });

  it('generates componentId for component id was not passed', () => {
    const options = { topBar: { title: { component: { name: 'a' } } } };

    uut.processOptions(CommandName.SetRoot, options);

    expect(options).toEqual({
      topBar: { title: { component: { name: 'a', componentId: 'CustomComponent1' } } },
    });
  });

  it('copies passed id to componentId key', () => {
    const options = { topBar: { title: { component: { name: 'a', id: 'Component1' } } } };

    uut.processOptions(CommandName.SetRoot, options);

    expect(options).toEqual({
      topBar: { title: { component: { name: 'a', id: 'Component1', componentId: 'Component1' } } },
    });
  });

  it('calls store when button has passProps and id', () => {
    const passProps = { prop: 'prop' };
    const options = { topBar: { rightButtons: [{ passProps, id: '1' }] } };

    uut.processOptions(CommandName.SetRoot, options);

    verify(mockedStore.setPendingProps('1', passProps)).called();
  });

  it('do not touch passProps when id for button is missing', () => {
    const passProps = { prop: 'prop' };
    const options = { topBar: { rightButtons: [{ passProps } as any] } };

    uut.processOptions(CommandName.SetRoot, options);

    expect(options).toEqual({ topBar: { rightButtons: [{ passProps }] } });
  });

  it('omits passProps when processing buttons or components', () => {
    const options = {
      topBar: {
        rightButtons: [{ passProps: {}, id: 'btn1' }],
        leftButtons: [{ passProps: {}, id: 'btn2' }],
        title: { component: { name: 'helloThere1', passProps: {} } },
        background: { component: { name: 'helloThere2', passProps: {} } },
      },
    };
    uut.processOptions(CommandName.SetRoot, options);
    expect(options.topBar.rightButtons[0].passProps).toBeUndefined();
    expect(options.topBar.leftButtons[0].passProps).toBeUndefined();
    expect(options.topBar.title.component.passProps).toBeUndefined();
    expect(options.topBar.background.component.passProps).toBeUndefined();
  });

  it('Will ensure the store has a chance to lazily load components in options', () => {
    const options = {
      topBar: {
        title: { component: { name: 'helloThere1', passProps: {} } },
        background: { component: { name: 'helloThere2', passProps: {} } },
      },
    };
    uut.processOptions(CommandName.SetRoot, options);
    verify(mockedStore.ensureClassForName('helloThere1')).called();
    verify(mockedStore.ensureClassForName('helloThere2')).called();
  });

  it('show warning on iOS when toggling bottomTabs visibility through mergeOptions', () => {
    jest.spyOn(console, 'warn');
    uut.processOptions(CommandName.MergeOptions, { bottomTabs: { visible: false } });
    expect(console.warn).toBeCalledWith(
      'toggling bottomTabs visibility is deprecated on iOS. For more information see https://github.com/wix/react-native-navigation/issues/6416',
      {
        bottomTabs: { visible: false },
      }
    );
  });

  describe('searchBar', () => {
    describe('Android', () => {
      beforeEach(() => {
        Platform.OS = 'android';
      });

      it('transform searchBar bool to object', () => {
        const options = { topBar: { searchBar: true as any } };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options.topBar.searchBar).toStrictEqual({
          visible: true,
          hideOnScroll: false,
          hideTopBarOnFocus: false,
          obscuresBackgroundDuringPresentation: false,
          backgroundColor: undefined,
          tintColor: undefined,
          placeholder: '',
        });
      });

      it('transform searchBar bool to object and merges in deprecated values', () => {
        const options = {
          topBar: {
            searchBar: true as any,
            searchBarHiddenWhenScrolling: true,
            hideNavBarOnFocusSearchBar: true,
            searchBarBackgroundColor: 'red',
            searchBarTintColor: 'green',
            searchBarPlaceholder: 'foo',
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options.topBar.searchBar).toStrictEqual({
          visible: true,
          hideOnScroll: true,
          hideTopBarOnFocus: true,
          obscuresBackgroundDuringPresentation: false,
          backgroundColor: { dark: 0xffff0000, light: 0xffff0000 },
          tintColor: { dark: 0xff00ff00, light: 0xff00ff00 },
          placeholder: 'foo',
        });
      });
    });

    describe('iOS', () => {
      beforeEach(() => {
        Platform.OS = 'ios';
      });

      it('transform searchBar bool to object', () => {
        const options = { topBar: { searchBar: true as any } };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options.topBar.searchBar).toStrictEqual({
          visible: true,
          hideOnScroll: false,
          hideTopBarOnFocus: false,
          obscuresBackgroundDuringPresentation: false,
          backgroundColor: undefined,
          tintColor: undefined,
          placeholder: '',
        });
      });

      it('transform searchBar bool to object and merges in deprecated values', () => {
        const options = {
          topBar: {
            searchBar: true as any,
            searchBarHiddenWhenScrolling: true,
            hideNavBarOnFocusSearchBar: true,
            searchBarBackgroundColor: 'red',
            searchBarTintColor: 'green',
            searchBarPlaceholder: 'foo',
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options.topBar.searchBar).toStrictEqual({
          visible: true,
          hideOnScroll: true,
          hideTopBarOnFocus: true,
          obscuresBackgroundDuringPresentation: false,
          backgroundColor: 0xffff0000,
          tintColor: 0xff00ff00,
          placeholder: 'foo',
        });
      });
    });
  });

  describe('process animations options', () => {
    const performOnViewsInvolvedInStackAnimation = (action: (view: string) => void) =>
      ['content', 'topBar', 'bottomTabs'].forEach(action);

    describe('push', () => {
      it('old *.push api is converted into push.*.enter', () => {
        performOnViewsInvolvedInStackAnimation((view: string) => {
          const options: Options = {
            animations: {
              push: {
                [view]: {
                  alpha: {
                    from: 0,
                    to: 1,
                  },
                },
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.push).toStrictEqual({
            [view]: {
              enter: {
                alpha: {
                  from: 0,
                  to: 1,
                },
              },
            },
          });
        });
      });

      it('StackAnimationOptions based push api is left as is', () => {
        performOnViewsInvolvedInStackAnimation((view: string) => {
          const options: Options = {
            animations: {
              push: {
                [view]: {
                  exit: {
                    alpha: {
                      from: 1,
                      to: 0,
                    },
                  },
                  enter: {
                    alpha: {
                      from: 0,
                      to: 1,
                    },
                  },
                },
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.push).toStrictEqual({
            [view]: {
              exit: {
                alpha: {
                  from: 1,
                  to: 0,
                },
              },
              enter: {
                alpha: {
                  from: 0,
                  to: 1,
                },
              },
            },
          });
        });
      });

      it('Options not related to views are left as is', () => {
        performOnViewsInvolvedInStackAnimation(() => {
          const options: Options = {
            animations: {
              push: {
                enabled: false,
                waitForRender: true,
                sharedElementTransitions: [],
                elementTransitions: [],
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.push).toStrictEqual({
            enabled: false,
            waitForRender: true,
            sharedElementTransitions: [],
            elementTransitions: [],
          });
        });
      });
    });

    describe('pop', () => {
      it('old pop.content api is converted into pop.content.exit', () => {
        performOnViewsInvolvedInStackAnimation((view: string) => {
          const options: Options = {
            animations: {
              pop: {
                [view]: {
                  alpha: {
                    from: 0,
                    to: 1,
                  },
                },
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.pop).toStrictEqual({
            [view]: {
              exit: {
                alpha: {
                  from: 0,
                  to: 1,
                },
              },
            },
          });
        });
      });

      it('StackAnimationOptions based pop api is left as is', () => {
        performOnViewsInvolvedInStackAnimation((view: string) => {
          const options: Options = {
            animations: {
              pop: {
                [view]: {
                  exit: {
                    alpha: {
                      from: 1,
                      to: 0,
                    },
                  },
                  enter: {
                    alpha: {
                      from: 0,
                      to: 1,
                    },
                  },
                },
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.pop).toStrictEqual({
            [view]: {
              exit: {
                alpha: {
                  from: 1,
                  to: 0,
                },
              },
              enter: {
                alpha: {
                  from: 0,
                  to: 1,
                },
              },
            },
          });
        });
      });

      it('Options not related to views are left as is', () => {
        performOnViewsInvolvedInStackAnimation(() => {
          const options: Options = {
            animations: {
              pop: {
                enabled: false,
                waitForRender: true,
                sharedElementTransitions: [],
                elementTransitions: [],
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.pop).toStrictEqual({
            enabled: false,
            waitForRender: true,
            sharedElementTransitions: [],
            elementTransitions: [],
          });
        });
      });
    });

    describe('setStackRoot', () => {
      it('ViewAnimationOptions based setStackRoot api is converted to StackAnimationOptions based api', () => {
        const options: Options = {
          animations: {
            setStackRoot: {
              alpha: {
                from: 0,
                to: 1,
              },
            },
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options.animations!!.setStackRoot).toStrictEqual({
          content: {
            enter: {
              alpha: {
                from: 0,
                to: 1,
              },
            },
          },
        });
      });

      it('Disabled ViewAnimationOptions based setStackRoot api is converted to StackAnimationOptions based api', () => {
        const options: Options = {
          animations: {
            setStackRoot: {
              enabled: false,
              waitForRender: true,
            },
          },
        };
        uut.processOptions(CommandName.SetRoot, options);
        expect(options.animations!!.setStackRoot as StackAnimationOptions).toStrictEqual({
          enabled: false,
          waitForRender: true,
          content: {
            enter: {
              enabled: false,
              waitForRender: true,
            },
          },
        });
      });

      it('StackAnimationOptions based setStackRoot api is left as is', () => {
        performOnViewsInvolvedInStackAnimation((view: string) => {
          const options: Options = {
            animations: {
              setStackRoot: {
                [view]: {
                  enter: {
                    alpha: {
                      from: 0,
                      to: 1,
                    },
                  },
                },
              },
            },
          };
          uut.processOptions(CommandName.SetRoot, options);
          expect(options.animations!!.setStackRoot).toStrictEqual({
            [view]: {
              enter: {
                alpha: {
                  from: 0,
                  to: 1,
                },
              },
            },
          });
        });
      });
    });
  });
});
