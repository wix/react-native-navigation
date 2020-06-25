import { ImageRequireSource } from 'react-native';

export type CocktailItem = {
  id: string;
  name: string;
  image: ImageRequireSource;
  color: string;
  ingredients: {
    name: string;
    measurement?: string;
  }[];
  description: string;
};

const cocktails: CocktailItem[] = [
  {
    id: '1',
    name: 'Negroni',
    image: require('../../img/cocktails/negroni.jpg'),
    color: '#e00001',
    ingredients: [
      {
        name: 'Gin',
        measurement: '30ml',
      },
      {
        name: 'Campari',
        measurement: '30ml',
      },
      {
        name: 'Sweet Vermouth',
        measurement: '30ml',
      },
    ],
    description: `The Negroni is a popular Italian cocktail, made of one part gin, one part vermouth rosso (red, semi-sweet), and one part Campari, garnished with orange peel. It is considered an apéritif.
    
    A properly made Negroni is stirred, not shaken, and (classically) built over ice in an old fashioned or ‘rocks’ glass and garnished with a slice of orange. Outside of Italy an orange peel is often used in place of an orange slice, either is acceptable though an orange slice is more traditional.`,
  },
  {
    id: '2',
    name: 'Last Word',
    image: require('../../img/cocktails/lastWord.jpg'),
    color: '#c7cfb7',
    ingredients: [
      {
        name: 'Gin',
        measurement: '50ml',
      },
      {
        name: 'Noilly Prat dry vermouth',
        measurement: '20ml',
      },
      {
        name: 'St. Germain',
        measurement: '15ml',
      },
    ],
    description:
      'The Last Word is a gin-based prohibition-era cocktail originally developed at the Detroit Athletic Club. While the drink eventually fell out of favor, it enjoyed a renewed popularity after being rediscovered by the bartender Murray Stenson in 2004 during his tenure at the Zig Zag Café and becoming a cult hit in the Seattle area.',
  },
  {
    id: '3',
    name: 'Basil Smash',
    image: require('../../img/cocktails/cucumberBasilSmash.jpg'),
    color: '#9dd888',
    ingredients: [
      {
        name: 'Gin',
        measurement: '50ml',
      },
      {
        name: 'Lemon Juice',
        measurement: '1tbsp',
      },
      {
        name: 'Tonic',
      },
    ],
    description:
      'The Basil Smash is a relatively new drink (well, new in the context of cocktails) with an alluring Hulk-green tint and a real fresh kick. In fact, the addition of fresh basil means that this drink is basically a vegetable, and certainly a welcome way to get one of your five-a-day.',
  },
  {
    id: '4',
    name: 'Bloody Marry',
    image: require('../../img/cocktails/bloodyMarry.jpg'),
    color: '#e73e05',
    ingredients: [
      {
        name: 'Vodka',
        measurement: '45ml',
      },
      {
        name: 'Tomato Juice',
        measurement: '75ml',
      },
      {
        name: 'Worcestershire Sauce',
        measurement: '2 dashes',
      },
      {
        name: 'Hot Sauce',
        measurement: '3 dashes',
      },
      {
        name: 'Lemon Juice',
        measurement: '30ml',
      },
      {
        name: 'Ground black pepper',
        measurement: '1 pinch',
      },
      {
        name: 'Salt',
        measurement: '1 pinch',
      },
    ],
    description: `A Bloody Mary is a cocktail containing vodka, tomato juice, and other spices and flavorings including Worcestershire sauce, hot sauces, garlic, herbs, horseradish, celery, olives, salt, black pepper, lemon juice, lime juice and/or celery salt. In the United States, it is usually consumed in the morning or early afternoon, and is popular as a hangover cure.
    
    The Bloody Mary was invented in the 1920s or 1930s. There are various theories as to the origin of the drink and its name. It has many variants, most notably the Red Snapper, the Virgin Mary, the Caesar, and the Michelada.`,
  },
  {
    id: '5',
    name: 'Gimlet',
    image: require('../../img/cocktails/gimlet.jpg'),
    color: '#d3d6cf',
    ingredients: [
      {
        name: 'Gin',
        measurement: '75ml',
      },
      {
        name: 'Lime Juice',
        measurement: '30ml',
      },
      {
        name: 'Simple Syrup',
        measurement: '15ml',
      },
    ],
    description: `The gimlet (pronounced with a hard 'g') is a cocktail typically made of 2 parts gin and 1 part lime juice. A 1928 description of the drink was: \"gin, a spot of lime, and soda.\" The description in the 1953 Raymond Chandler novel The Long Goodbye stated that \"a real gimlet is half gin and half Rose's lime juice and nothing else.\" This is in line with the proportions suggested by The Savoy Cocktail Book (1930), which specifies one half Plymouth Gin and one half Rose\'s Lime Juice Cordial. However, modern tastes are less sweet, and generally provide for at least two parts gin to one part of the lime and other non-alcoholic elements (see recipes below).`,
  },
  {
    id: '6',
    name: 'French Martini',
    image: require('../../img/cocktails/frenchMartini.jpg'),
    color: '#c03500',
    ingredients: [
      {
        name: 'Vodka',
        measurement: '60ml',
      },
      {
        name: 'Pineapple Juice',
        measurement: '50ml',
      },
      {
        name: 'Chambrod',
        measurement: '7.5ml',
      },
    ],
    description: `The French martini was invented in the 1980s at one of Keith McNally's New York City bars. It next appeared on the drinks menu at McNally's Balthazar in SoHo in 1996. The cocktail was produced during the 1980s–1990s cocktail renaissance. The key ingredient that makes a martini \"French\" is Chambord, a black raspberry liqueur that has been produced in France since 1685.`,
  },
];

export default cocktails;
