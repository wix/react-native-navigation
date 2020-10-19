import { ImageRequireSource } from 'react-native';

export type CarItem = {
  id: string;
  name: string;
  image: ImageRequireSource;
  color: string;
  description: string;
};

const cars: CarItem[] = [
  {
    id: '1',
    name: 'Lamborghini Diablo',
    image: require('../../img/cars/diablo.jpg'),
    color: '#b00001',
    description: `The Lamborghini Diablo is a high-performance mid-engine sports car that was built by Italian automotive manufacturer Lamborghini between 1990 and 2001. It is the first production Lamborghini capable of attaining a top speed in excess of 320 kilometres per hour (200 mph). After the end of its production run in 2001, the Diablo was replaced by the Lamborghini Murciélago. The name Diablo means "devil" in Spanish.`,
  },
  {
    id: '2',
    name: 'Lamborghini Countach',
    image: require('../../img/cars/countach.jpg'),
    color: '#c7cfb7',
    description: `The Lamborghini About this soundCountach (help·info) is a rear mid-engine, rear-wheel-drive sports car produced by the Italian automobile manufacturer Lamborghini from 1974 to 1990. It is one of the many exotic designs developed by Italian design house Bertone, which pioneered and popularized the sharply angled "Italian Wedge" shape.

      The style was introduced to the public in 1970 as the Lancia Stratos Zero concept car. The first showing of the Countach prototype was at the 1971 Geneva Motor Show, as the Lamborghini LP500 concept.[6] The Countach also popularized the "cab forward" design concept, which pushes the passenger compartment forward for a more aggressive look.`,
  },
  {
    id: '3',
    name: 'Lamborghini Aventador',
    image: require('../../img/cars/aventador.jpg'),
    color: '#9dd888',
    description:
      'The Lamborghini Aventador (Spanish pronunciation: [aβentaˈðoɾ]) is a mid-engine sports car produced by the Italian automotive manufacturer Lamborghini. In keeping with Lamborghini tradition, the Aventador is named after a fighting bull.',
  },
];

export default cars;
