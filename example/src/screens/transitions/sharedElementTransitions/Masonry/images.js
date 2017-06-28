const images = [
  require('../../../../../img/masonry/Cfw87359UT.jpeg'),
  require('../../../../../img/masonry/a848dHxA4e.jpeg'),
  require('../../../../../img/masonry/AdGXmD1CH6.jpeg'),
  require('../../../../../img/masonry/5Gi8kova3k.jpeg'),
  require('../../../../../img/masonry/ri90ueind7.jpeg'),
  require('../../../../../img/masonry/kVN0FryOZk.jpeg'),
  require('../../../../../img/masonry/v8KLi2f0Tr.jpeg'),
  require('../../../../../img/masonry/xU42hx19BB.jpeg'),
  require('../../../../../img/masonry/61mpAVRV73.jpeg'),
  require('../../../../../img/masonry/pqgylg80SD.jpeg'),
  require('../../../../../img/masonry/37r6Cqp1B8.jpeg'),
  require('../../../../../img/masonry/N30E32431C.jpeg'),
  require('../../../../../img/masonry/rVOcz7rd0z.jpeg'),
  require('../../../../../img/masonry/A4g0lZ33Z8.jpeg'),
  require('../../../../../img/masonry/j51Pva1P8L.jpeg'),
  require('../../../../../img/masonry/158xD4xbeh.jpeg'),
];

function randomImage() {
  return images[Math.floor(Math.random() * images.length)];
}

export default [
  {
    key: 1,
    images: [[{
      weight: 2,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }, {
      weight: 3,
      source: randomImage(),
    }], [{
      weight: 1,
      source: randomImage(),
    }, {
      weight: 3,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }]],
  },
  {
    key: 2,
    images: [[{
      weight: 1,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }, {
      weight: 3,
      source: randomImage(),
    }], [{
      weight: 3,
      source: randomImage(),
    }, {
      weight: 3,
      source: randomImage(),
    }]],
  },
  {
    key: 3,
    images: [[{
      weight: 1,
      source: randomImage(),
    }, {
      weight: 2,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }, {
      weight: 2,
      source: randomImage(),
    }], [{
      weight: 2,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }, {
      weight: 2,
      source: randomImage(),
    }, {
      weight: 1,
      source: randomImage(),
    }]],
  }
];
