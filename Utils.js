import React from 'react';
import { fireEvent, render, within } from '@testing-library/react-native';

const isDetox = () => !!process.env.DETOX_START_TIMESTAMP;

const extendDetox = () => {
  it.e2e = (name, fn) => {
    if (isDetox()) {
      it(name, fn);
    }
  };

  describe.e2e = (name, fn) => {
    if (isDetox()) {
      describe(name, fn);
    } else {
      xdescribe(name, fn);
    }
  };
};

const mockDetox = (entrypoint) => {
  extendDetox();
  let App;
  let { ApplicationMock } = require('./lib/src/Mock');

  global.device = {
    launchApp: () => {
      ApplicationMock = require('./lib/src/Mock').ApplicationMock;
      App = render(<ApplicationMock entryPoint={entrypoint} />);
      return App;
    },
  };

  global.element = (e) => e;
  global.by = {
    text: (text) => elementByLabel(text, App),
    id: (id) => {
      return elementById(id, App);
    },
  };

  const origExpect = expect;
  expect = (e) => {
    const match = origExpect(e);
    match.toBeNotVisible = () => {
      return match.toBe(null);
    };
    match.toBeVisible = () => match.toBeTruthy();
    match.toExist = match.toBeVisible;
    return match;
  };

  function elementById(id, App) {
    let element = null;
    if (within(App.getByTestId(ApplicationMock.VISIBLE_SCREEN_TEST_ID)).queryByTestId(id)) {
      element = within(App.getByTestId(ApplicationMock.VISIBLE_SCREEN_TEST_ID)).getByTestId(id);
    } else if (within(App.getByTestId(ApplicationMock.VISIBLE_OVERLAY_TEST_ID)).queryByTestId(id)) {
      element = within(App.getByTestId(ApplicationMock.VISIBLE_OVERLAY_TEST_ID)).getByTestId(id);
    }

    if (element)
      element.tap = async () => {
        await fireEvent.press(element);
      };

    return element;
  }

  function elementByLabel(label, App) {
    let element = null;
    if (within(App.getByTestId(ApplicationMock.VISIBLE_SCREEN_TEST_ID)).queryByText(label)) {
      element = within(App.getByTestId(ApplicationMock.VISIBLE_SCREEN_TEST_ID)).getByText(label);
    } else if (
      within(App.getByTestId(ApplicationMock.VISIBLE_OVERLAY_TEST_ID)).queryByText(label)
    ) {
      element = within(App.getByTestId(ApplicationMock.VISIBLE_OVERLAY_TEST_ID)).getByText(label);
    }

    if (element)
      element.tap = async () => {
        await fireEvent.press(element);
      };

    return element;
  }
};

export { mockDetox, extendDetox };
