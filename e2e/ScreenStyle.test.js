const Utils = require('./Utils');

const { elementByLabel, elementById } = Utils;

describe('screen style', () => {
  beforeEach(async () => {
    await device.relaunchApp();
  });

  it('declare a navigationOptions on container component', async () => {
    await elementByLabel('Push Options Screen').tap();
    await expect(element(by.label('Static Title').and(by.type('UILabel')))).toBeVisible();
  });

  it('change title on container component', async () => {
    await elementByLabel('Push Options Screen').tap();
    await expect(element(by.label('Static Title').and(by.type('UILabel')))).toBeVisible();
    await elementByLabel('Dynamic Options').tap();
    await expect(element(by.label('Dynamic Title').and(by.type('UILabel')))).toBeVisible();
  });

  it('set dynamic options with valid options will do something and not crash', async () => {
    // we have no way of testing individual styles for the screen
    await elementByLabel('Push Options Screen').tap();
    await elementByLabel('Dynamic Options').tap();
    await expect(element(by.label('Options Screen'))).toBeVisible();
  });

  it('hides Tab Bar when pressing on Hide Top Bar and shows it when pressing on Show Top Bar', async () => {
    await elementByLabel('Push Options Screen').tap();
    await elementByLabel('Hide Top Bar').tap();
    await expect(element(by.type('UINavigationBar'))).toBeNotVisible();
    await elementByLabel('Show Top Bar').tap();
    await expect(element(by.type('UINavigationBar'))).toBeVisible();
  });

  it('hides topBar onScroll down and shows it on scroll up', async () => {
    await elementByLabel('Push Options Screen').tap();
    await elementByLabel('scrollView Screen').tap();
    await elementByLabel('Toggle Top Bar Hide On Scroll').tap();
    await expect(element(by.type('UINavigationBar'))).toBeVisible();
    await element(by.id('scrollView')).swipe('up', 'fast');
    await expect(element(by.type('UINavigationBar'))).toBeNotVisible();
    await element(by.id('scrollView')).swipe('down', 'fast');
    await expect(element(by.type('UINavigationBar'))).toBeVisible();
  });

  it('set Tab Bar badge on a current Tab', async () => {
    await elementByLabel('Switch to tab based app').tap();
    await elementByLabel('Set Tab Badge').tap();
    await expect(element(by.text('EnCyClOpEdIa'))).toBeVisible();
  });

  it('set right buttons', async () => {
    await elementByLabel('Push Options Screen').tap();
    await expect(elementById('buttonOne')).toBeVisible();
    await elementById('buttonOne').tap();
    await expect(elementById('buttonTwo')).toBeVisible();
    await elementById('buttonTwo').tap();
    await expect(elementById('buttonOne')).toBeVisible();
  });

  it('set left buttons', async () => {
    await elementByLabel('Push Options Screen').tap();
    await expect(elementById('buttonLeft')).toBeVisible();
  });
});
