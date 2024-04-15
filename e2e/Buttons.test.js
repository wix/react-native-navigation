import Utils from './Utils';
import TestIDs from '../playground/src/testIDs';
import fs from 'fs';
const exec = require('shell-utils').exec;

const {
  elementById,
  elementByLabel,
  elementTopBar,
  expectImagesToBeEqual,
} = Utils;

describe('Buttons', () => {
  beforeEach(async () => {
    await device.launchApp({ newInstance: true });
    await elementById(TestIDs.OPTIONS_TAB).tap();
    await elementById(TestIDs.GOTO_BUTTONS_SCREEN).tap();
  });

  it.e2e('should render top/navigation-bar buttons in the right order', async () => {
    const snapshottedImagePath = `./e2e/assets/buttons_navbar.${device.getPlatform()}.png`;
    const actual = await elementTopBar().takeScreenshot('buttons_navbar');

    // save file from path `actual` to `snapshottedImagePath`
    fs.copyFileSync(actual, snapshottedImagePath);

    // push commit to git
    pushSnapshots();
    // const remoteUrl = new RegExp(`https?://(\\S+)`).exec(exec.execSyncRead(`git remote -v`))[1];
    // await exec.execSync(`git commit -am "chore: update snapshot ${snapshottedImagePath}"`);
    // await exec.execSync(`git remote add deploy "https://${process.env.GIT_USER}:${process.env.GIT_TOKEN}@${remoteUrl}"`);
    // await exec.execSync(`git push deploy ${BRANCH}`);


    function pushSnapshots() {
      setupGit();
      exec.execSync(`git checkout aws-m2-ci`);
      exec.execSync(`git add ${snapshottedImagePath}`);
      exec.execSync(`git commit -m "Update snapshots [ci skip]"`);
      exec.execSync(`git push deploy aws-m2-ci`);
    }

    function setupGit() {
      exec.execSyncSilent(`git config --global push.default simple`);
      exec.execSyncSilent(`git config --global user.email "${process.env.GIT_EMAIL}"`);
      exec.execSyncSilent(`git config --global user.name "${process.env.GIT_USER}"`);
      const remoteUrl = new RegExp(`https?://(\\S+)`).exec(exec.execSyncRead(`git remote -v`))[1];
      exec.execSyncSilent(
        `git remote add deploy "https://${process.env.GIT_USER}:${process.env.GIT_TOKEN}@${remoteUrl}"`
      );
    }


    expectImagesToBeEqual(actual, snapshottedImagePath)
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
