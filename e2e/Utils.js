import { readFileSync } from 'fs';
function bitmapDiff(imagePath, expectedImagePath)  {
    const PNG = require('pngjs').PNG;
    const pixelmatch = require('pixelmatch');
    const img1 = PNG.sync.read(readFileSync(imagePath));
    const img2 = PNG.sync.read(readFileSync(expectedImagePath));
    const {width, height} = img1;
    const diff = new PNG({width, height});

    return pixelmatch(img1.data, img2.data, diff.data, width, height, {threshold: 0.0})
}
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
  expectImagesToBeEqual:(imagePath, expectedImagePath)=>{
      let diff = bitmapDiff(imagePath,expectedImagePath);
      if(diff!==0){
          throw Error(`${imagePath} should be the same as ${expectedImagePath}, with diff: ${diff}`)
      }
  } ,
    expectImagesToBeNotEqual:(imagePath, expectedImagePath)=>{
        let diff = bitmapDiff(imagePath,expectedImagePath);
        if(diff===0){
            throw Error(`${imagePath} should be the same as ${expectedImagePath}, with diff: ${diff}`)
        }
    }
};

export default utils;
