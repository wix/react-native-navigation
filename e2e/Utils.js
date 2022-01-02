import { readFileSync } from 'fs';

const utils = {
  elementByLabel: (label) => {
    return element(by.text(label));
  },
  elementById: (id) => {
    return element(by.id(id));
  },
  elementByTraits: (traits) => {
    return element(by.traits(traits));
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
  expectBitmapsToBeEqual: (imagePath, expectedImagePath) => {
    const bitmapBuffer = readFileSync(imagePath);
    const expectedBitmapBuffer = readFileSync(expectedImagePath);
    if (!bitmapBuffer.equals(expectedBitmapBuffer)) {
      throw new Error(
        `Expected image at ${imagePath} to be equal to image at ${expectedImagePath}, but it was different!`
      );
    }
  },
  expectBitmapsToBeNotEqual: (imagePath, expectedImagePath) => {
    const bitmapBuffer = readFileSync(imagePath);
    const expectedBitmapBuffer = readFileSync(expectedImagePath);
    if (bitmapBuffer.equals(expectedBitmapBuffer)) {
      throw new Error(
        `Expected image at ${imagePath} to be not equal to image at ${expectedImagePath}, but it was different!`
      );
    }
  },
};

export default utils;
