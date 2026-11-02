import { readFileSync } from 'fs';
import { config as detoxConfig } from 'detox/internals';
import { PNG } from 'pngjs';
import { ssim } from 'ssim.js';

const SSIM_SCORE_THRESHOLD = 0.99;

function convertToSSIMFormat(image) {
  return {
    data: new Uint8ClampedArray(image.data),
    width: image.width,
    height: image.height
  };
}

function loadImage(path) {
  const image = PNG.sync.read(readFileSync(path));

  return convertToSSIMFormat(image);
}

function bitmapDiff(imagePath, expectedImagePath, ssimThreshold = SSIM_SCORE_THRESHOLD) {
  const image = loadImage(imagePath);
  const expectedImage = loadImage(expectedImagePath);

  const { mssim, performance } = ssim(image, expectedImage);

  if (mssim < ssimThreshold) {
    throw new Error(
      `Expected bitmaps at '${imagePath}' and '${expectedImagePath}' to have an SSIM score ` +
      `of at least ${SSIM_SCORE_THRESHOLD}, but got ${mssim}. This means the snapshots are different ` +
      `(comparison took ${performance}ms)`,
    );
  }
}

const sleep = (ms) =>
  new Promise((res) => setTimeout(res, ms));

/**
 * @param tries Total tries to attempt (retries + 1)
 * @param delay Delay between retries, in milliseconds
 * @param {Function<Promise<Boolean>>} func
 * @returns {Promise<void>}
 * @throws {Error} if the function fails after all retries
 */
async function retry({ tries = 3, delay = 1000 }, func) {
  for (let i = 0; i < tries; i++) {
    const result = await func();
    if (result) {
      return;
    }

    await sleep(delay);
  }

  throw new Error(`Failed even after ${tries} attempts`);
}

// The `*.scene` configurations run the playgroundScene app (UIScene life cycle).
const isSceneLifecycle = () => /\.scene$/.test(detoxConfig.configurationName || '');

const utils = {
  isSceneLifecycle,
  /**
   * Opens a URL in the running app. Detox's `device.openURL` calls the app delegate's
   * `application:openURL:options:` directly, which UIKit never does under the UIScene life
   * cycle. Scene runs instead tap a notification carrying the URL: the playground re-opens its
   * own scheme, so iOS delivers it to `scene:openURLContexts:` with a real `UIOpenURLContext`.
   * (`xcrun simctl openurl` takes the same path but shows an "Open in ...?" system prompt that
   * Detox can't accept.)
   */
  openURL: async (url) => {
    if (device.getPlatform() === 'ios' && isSceneLifecycle()) {
      await device.sendUserNotification({
        trigger: { type: 'push' },
        title: 'Open URL',
        body: url,
        payload: { url },
        'content-available': 0,
        'action-identifier': 'default',
      });
      return;
    }
    await device.openURL({ url });
  },
  elementByLabel: (label) => {
    // uncomment for running tests with rn's new arch
    // return element(by.label(label)).atIndex(0);
    return element(by.text(label));
  },
  elementById: (id) => element(by.id(id)),
  elementByTraits: (traits) => element(by.traits(traits)),
  elementByType: (type) => element(by.type(type)),
  elementTopBar: () => {
    const elementType = (device.getPlatform() === 'ios') ?
      'UINavigationBar' :
      'com.reactnativenavigation.views.stack.topbar.TopBar';
    return utils.elementByType(elementType);
  },
  tapBackIos: () => {
    try {
      return element(by.traits(['button']).and(by.label('Back')))
        .atIndex(0)
        .tap();
    } catch (err) {
      return element(by.type('_UIModernBarButton').and(by.label('Back'))).tap();
    }
  },
  sleep,
  retry,
  expectImagesToBeEqual: (imagePath, expectedImagePath) => {
    bitmapDiff(imagePath, expectedImagePath);

  },
  expectImagesToBeNotEqual: (imagePath, expectedImagePath) => {
    try {
      bitmapDiff(imagePath, expectedImagePath);
    } catch (error) {
      return
    }

    throw new Error(
      `Expected bitmaps at '${imagePath}' and '${expectedImagePath}' to be different`,
    );
  },
};

export default utils;
