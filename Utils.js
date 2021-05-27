import React from 'react';
import { fireEvent, render, RenderAPI, within } from '@testing-library/react-native';
const { Application } = require('./lib/src/Mock');

const mockJest = () => {
  it.e2e = (name: string, fn: () => void) => {
    if (process.env.DETOX_START_TIMESTAMP) {
      it(name, fn);
    }
  };

  describe.e2e = (name: string, fn: () => void) => {
    if (process.env.DETOX_START_TIMESTAMP) {
      describe(name, fn);
    } else {
      xdescribe(name, fn);
    }
  };
};

const mockDetox = (entrypoint: () => any) => {
  let App: RenderAPI;

  const origExpect = expect;
  // @ts-ignore
  expect = (e: any) => {
    const match = origExpect(e);
    // @ts-ignore
    match.toBeNotVisible = () => {
      return match.toBe(null);
    };
    // @ts-ignore
    match.toBeVisible = () => match.toBeTruthy();
    // @ts-ignore
    match.toExist = match.toBeVisible;
    return match;
  };

  // @ts-ignore
  global.device = {
    launchApp: () => {
      App = render(<Application entryPoint={entrypoint} />);
      return App;
    },
  };

  // @ts-ignore
  global.element = (e: any) => e;
  // @ts-ignore
  global.by = {
    text: (text: string) => elementByLabel(text, App),
    id: (id: string) => {
      return elementById(id, App);
    },
  };
};

function elementById(id: string, App: RenderAPI) {
  // @ts-ignore
  let element: any = null;
  if (within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).queryByTestId(id)) {
    element = within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).getByTestId(id);
  } else if (within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).queryByTestId(id)) {
    element = within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).getByTestId(id);
  }

  if (element)
    // @ts-ignore
    element.tap = async () => {
      // @ts-ignore
      await fireEvent.press(element);
    };

  return element;
}

function elementByLabel(label: string, App: RenderAPI) {
  // @ts-ignore
  let element = null;
  if (within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).queryByText(label)) {
    element = within(App.getByTestId(Application.VISIBLE_SCREEN_TEST_ID)).getByText(label);
  } else if (within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).queryByText(label)) {
    element = within(App.getByTestId(Application.VISIBLE_OVERLAY_TEST_ID)).getByText(label);
  }

  if (element)
    // @ts-ignore
    element.tap = async () => {
      // @ts-ignore
      await fireEvent.press(element);
    };

  return element;
}

const mockUILib = () => {
  const NativeModules = require('react-native').NativeModules;
  NativeModules.KeyboardTrackingViewTempManager = {};
  NativeModules.StatusBarManager = {
    getHeight: () => 40,
  };
};

export { mockDetox, mockUILib, mockJest };
