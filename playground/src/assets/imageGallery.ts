import { ImageSourcePropType } from 'react-native';

export type ImageGalleryAsset = {
  weight: number;
  source: ImageSourcePropType;
};

export type ImageGalleryItem = {
  key: number;
  images: [ImageGalleryAsset[], ImageGalleryAsset[]];
};

const images: ImageSourcePropType[] = [
  require('../../img/imageGallery/Cfw87359UT.jpeg'),
  require('../../img/imageGallery/a848dHxA4e.jpeg'),
  require('../../img/imageGallery/AdGXmD1CH6.jpeg'),
  require('../../img/imageGallery/5Gi8kova3k.jpeg'),
  require('../../img/imageGallery/ri90ueind7.jpeg'),
  require('../../img/imageGallery/kVN0FryOZk.jpeg'),
  require('../../img/imageGallery/v8KLi2f0Tr.jpeg'),
  require('../../img/imageGallery/xU42hx19BB.jpeg'),
  require('../../img/imageGallery/61mpAVRV73.jpeg'),
  require('../../img/imageGallery/pqgylg80SD.jpeg'),
  require('../../img/imageGallery/37r6Cqp1B8.jpeg'),
  require('../../img/imageGallery/N30E32431C.jpeg'),
  require('../../img/imageGallery/rVOcz7rd0z.jpeg'),
  require('../../img/imageGallery/A4g0lZ33Z8.jpeg'),
  require('../../img/imageGallery/j51Pva1P8L.jpeg'),
  require('../../img/imageGallery/158xD4xbeh.jpeg'),
];

function randomImage() {
  return images[Math.floor(Math.random() * images.length)];
}

const imageGallery: ImageGalleryItem[] = [
  {
    key: 1,
    images: [
      [
        {
          weight: 2,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 3,
          source: randomImage(),
        },
      ],
      [
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 3,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
      ],
    ],
  },
  {
    key: 2,
    images: [
      [
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 3,
          source: randomImage(),
        },
      ],
      [
        {
          weight: 3,
          source: randomImage(),
        },
        {
          weight: 3,
          source: randomImage(),
        },
      ],
    ],
  },
  {
    key: 3,
    images: [
      [
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 2,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 2,
          source: randomImage(),
        },
      ],
      [
        {
          weight: 2,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
        {
          weight: 2,
          source: randomImage(),
        },
        {
          weight: 1,
          source: randomImage(),
        },
      ],
    ],
  },
];

export default imageGallery;
