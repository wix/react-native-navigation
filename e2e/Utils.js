import { readFileSync } from 'fs';
import { PNG } from 'pngjs';
import { ssim } from 'ssim.js';

const SSIM_SCORE_THRESHOLD = 0.997;

function convertToSSIMFormat(image) {
  return {
    data: new Uint8ClampedArray(image.data),
    width: image.width,
    height: image.height
}
  ;
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

async function retry({ retries = 3, delay = 1000 }, func) {
  let tries = 0;
  let result;
  do {
    result = await func();
  } while (!result && ++tries < retries);

  if (!result) {
    throw new Error(`Failed even after ${retries} retries`);
  }
}

const utils = {
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
  sleep: (ms) => new Promise((res) => setTimeout(res, ms)),
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
