import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';

const { elementById, elementByLabel } = Utils;

describe('Buttons', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.OPTIONS_TAB).tap();
    await elementById(TestIDs.GOTO_BUTTONS_SCREEN).tap();
  });

  it(':android: should not effect left buttons when hiding back button', async () => {
    await elementById(TestIDs.TOGGLE_BACK).tap();
    await expect(elementById(TestIDs.LEFT_BUTTON)).toBeVisible();
    await expect(elementById(TestIDs.TEXTUAL_LEFT_BUTTON)).toBeVisible();
    await expect(elementById(TestIDs.BACK_BUTTON)).toBeVisible();

    await elementById(TestIDs.TOGGLE_BACK).tap();
    await expect(elementById(TestIDs.LEFT_BUTTON)).toBeVisible();
    await expect(elementById(TestIDs.TEXTUAL_LEFT_BUTTON)).toBeVisible();
  });
  it('sets right buttons', async () => {
    await expect(elementById(TestIDs.BUTTON_ONE)).toBeVisible();
    await expect(elementById(TestIDs.ROUND_BUTTON)).toBeVisible();
  });

  it('set left buttons', async () => {
    await expect(elementById(TestIDs.LEFT_BUTTON)).toBeVisible();
  });

  it('pass props to custom button component', async () => {
    await expect(elementByLabel('Two')).toExist();
  });

  it('pass props to custom button component should exist after push pop', async () => {
    await expect(elementByLabel('Two')).toExist();
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.POP_BTN).tap();
    await expect(elementByLabel('Two')).toExist();
  });

  it('custom button is clickable', async () => {
    await elementByLabel('Two').tap();
    await expect(elementByLabel('Times created: 1')).toExist();
  });

  it('Resetting buttons should unmount button react view', async () => {
    await elementById(TestIDs.SHOW_LIFECYCLE_BTN).tap();
    await elementById(TestIDs.RESET_BUTTONS).tap();
    await expect(elementByLabel('Button component unmounted')).toBeVisible();
  });

  it('change button props without rendering all buttons', async () => {
    await elementById(TestIDs.CHANGE_BUTTON_PROPS).tap();
    await expect(elementByLabel('Three')).toBeVisible();
  });

  it('pop using back button', async () => {
    await elementById(TestIDs.PUSH_BTN).tap();
    await elementById(TestIDs.BACK_BUTTON).tap();
    await expect(elementByLabel('Buttons')).toBeVisible();
  });

  it('resizes title component when a button is added with mergeOptions', async () => {
    await elementById(TestIDs.RESET_BUTTONS).tap();
    await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
    await elementById(TestIDs.BUTTON_THREE).tap();
  });

  it('Button component is not recreated if it has a predefined componentId', async () => {
    await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
    await elementById(TestIDs.ROUND_BUTTON).tap();
    await expect(elementByLabel('Times created: 1')).toBeVisible();
    await elementById(TestIDs.OK_BUTTON).tap();

    await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
    await elementById(TestIDs.ROUND_BUTTON).tap();
    await expect(elementByLabel('Times created: 1')).toBeVisible();
    await elementById(TestIDs.OK_BUTTON).tap();

    await elementById(TestIDs.SET_RIGHT_BUTTONS).tap();
    await elementById(TestIDs.ROUND_BUTTON).tap();
    await expect(elementByLabel('Times created: 1')).toBeVisible();
  });

  it('Accepts textual left button', async () => {
    await expect(elementById(TestIDs.TEXTUAL_LEFT_BUTTON)).toBeVisible();
  });

  it('Updates left button', async () => {
    await elementById(TestIDs.ADD_COMPONENT_BUTTON).tap();
    await expect(elementById('leftButton0')).toBeVisible();

    await elementById(TestIDs.ADD_COMPONENT_BUTTON).tap();
    await expect(elementById('leftButton1')).toBeVisible();
  });
});
