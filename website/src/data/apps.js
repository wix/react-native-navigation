const apps = [
  {
    title: 'Spaces by Wix',
    description:
      'Spaces by Wix, the place to connect with your favorite online store, business, website or community',
    image: require('./showcase/spaces.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.wix.android',
    appStore: 'https://apps.apple.com/us/app/wix/id1099748482',
  },
  {
    title: 'Wix Owner',
    description:
      'Use the Wix Owner app to build, customize and manage a website and mobile app experience for your business and community.',
    image: require('./showcase/owner.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.wix.admin',
    appStore: 'https://apps.apple.com/us/app/wix-owner-websites-apps/id1545924344',
  },
  {
    title: 'feastr',
    description: 'Weight loss meal plan.',
    image: require('./showcase/feastr.png'),
    playStore: 'https://play.google.com/store/apps/details?id=de.feastr.plan',
    appStore: 'https://apps.apple.com/us/app/feastr-weight-loss-meal-plan/id1200732621',
  },
  {
    title: 'Amber',
    description: 'Amber is the easiest and safest place to buy and sell bitcoin.',
    image: require('./showcase/amber.png'),
    playStore: 'https://play.google.com/store/apps/details?id=io.getamber.app&hl=en_US&gl=US',
    appStore: 'https://apps.apple.com/au/app/amber-bitcoin-made-easy/id1410639317',
  },
  {
    title: 'Klarna',
    description:
      'Try before you buy and make hassle-free returns, without taking money out of your pocket in the meantime.',
    image: require('./showcase/klarna.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.myklarnamobile',
    appStore: 'https://apps.apple.com/app/klarna-smoooth-shopping/id1115120118',
  },
  {
    title: 'Delta',
    description: 'Investment Portfolio Tracker',
    image: require('./showcase/delta.png'),
    playStore: 'https://play.google.com/store/apps/details?id=io.getdelta.android',
    appStore: 'https://apps.apple.com/us/app/apple-store/id1288676542',
  },
  {
    title: 'Squarespace Scheduling',
    description: `The Squarespace Scheduling app is your appointment-scheduling assistant that helps manage your calendar anytime, anywhere.`,
    image: require('./showcase/acuity.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.acuityscheduling.app.android',
    appStore: 'https://apps.apple.com/us/app/acuity-schedule-admin/id1179146771',
  },
  {
    title: 'Magic Eden',
    description: 'Magic Eden is the leading NFT Marketplace on Solana.',
    image: require('./showcase/magiceden.png'),
    playStore: 'https://play.google.com/store/apps/details?id=io.magiceden.android',
    appStore: 'https://apps.apple.com/us/app/magic-eden-nft-marketplace/id1602924580',
  },
  {
    title: 'Cookomix',
    description: `Cookomix est une application d'échange de recettes adaptées au Thermomix ®. Découvrez-y les meilleures recettes Thermomix ® écrites et commentées par la communauté et présentées de manière simple et lisible comme sur votre sur votre appareil !`,
    image: require('./showcase/cookomix.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.cookomix.mobile',
    appStore: 'https://itunes.apple.com/fr/app/cookomix/id1353826313',
  },
  {
    title: 'Taimi - LGBTQ+ Dating and Chat',
    description: 'World’s Largest LGBTQ+ Platform',
    image: require('./showcase/taimi.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.takimi.android&hl=en&gl=US',
    appStore: 'https://apps.apple.com/us/app/taimi-lgbtq-dating-chat/id1282966364',
  },
  {
    title: 'getAbstract Book Summaries',
    description:
      'Get the key insights from 20,000+ of the best nonfiction books, articles and video talks summarized into 10-minute summaries.',
    image: require('./showcase/getAbstract.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.soxes.getAbstract&hl=en&gl=US',
    appStore: 'https://apps.apple.com/us/app/getabstract-book-summaries/id338552990',
  },
  {
    title: 'HoneyBook',
    description: `HoneyBook helps you get the client you really want. Reply to questions quickly, send professional invoices, contracts, quotes and brochures. Manage your clients and grow your business.`,
    image: require('./showcase/honeybook.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.honeybook.alfred',
    appStore: 'https://apps.apple.com/il/app/honeybook-small-business-crm/id1104772757',
  },
  {
    title: 'Digitail - Smarter Pet Care',
    description: 'Be the best pet parent you can be with Digitail! The app is a digital medical record that safe-guards all the info about your furry family member and allows you to share it with your vet.',
    image: require('./showcase/digitail.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.digitail.digitail&hl=en&gl=US',
    appStore: 'https://apps.apple.com/us/app/digitail-for-pets/id1473042508',
  },
  {
    title: 'BetMentor: Betting Tips',
    description: 'BetMentor Betting Tips is an app that offers several tools to help you to get football predictions increase your success rate at Social Betting Casino!',
    image: require('./showcase/bementor.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.betmentor',
    appStore: 'https://apps.apple.com/az/app/betmentor-football-predictions/id1542592232',
  },
  {
    title: 'Thera: Mood tracker & Journal',
    description: 'Thera – your personal mental health tracker and your diary with fingerprint lock.',
    image: require('./showcase/thera.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.sintylapse.therapydiaries',
    appStore: 'https://apps.apple.com/by/app/id1474161008',
  },
  {
    title: 'hello aurora: forecast app',
    description: 'Hello aurora is a mobile application that will act as your best friend when it comes to finding the Northern Lights.',
    image: require('./showcase/aurora.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.helloaurora',
    appStore: 'https://apps.apple.com/us/app/hello-aurora/id1457810302',
  },
  {
    title: 'Nifty',
    description: `Nifty helps you simplify your teams’ workflow by consolidating all phases of your project's lifecycle into one powerful and intuitive tool.`,
    image: require('./showcase/nifty.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.niftypm',
    appStore: 'https://apps.apple.com/us/app/nifty-manage-projects-tasks/id1366408429',
  },
  {
    title: 'Clubhouse',
    description: 'Clubhouse is collaborative project management without all the management.',
    image: require('./showcase/clubhouse.png'),
    playStore: 'https://play.google.com/store/apps/details?id=io.clubhouse.clubhouse',
    appStore: 'https://apps.apple.com/us/app/clubhouse/id1193784808',
  },
  {
    title: 'Bene',
    description: 'Wellness and sustainable awareness',
    image: require('./showcase/bene.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.jp.bene.app',
    appStore: 'https://apps.apple.com/jp/app/id1498032984',
  },
  {
    title: 'Nox',
    description: 'AI Powered Dream Journal',
    image: require('./showcase/Nox.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.aspect.nox',
    appStore: 'https://apps.apple.com/zw/app/nox-ai-powered-dream-journal/id1543257201',
  },
  {
    title: 'Obitrain',
    description:
      'Create your own exercises and training sessions, track your progress and share it with your community.',
    image: require('./showcase/obitrain.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.obitrain.obiapp',
    appStore: 'https://apps.apple.com/us/app/obitrain/id1462514186',
  },
  {
    title: 'Arkham Cards',
    description: 'Create and manage deck lists for the Arkham Horror LCG card game.',
    image: require('./showcase/arkhamCards.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.arkhamcards',
    appStore: 'https://apps.apple.com/us/app/arkham-cards/id1424000351',
  },
  {
    title: 'Galarm',
    description: 'Alarms and Reminders',
    image: require('./showcase/galarm.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.galarmapp&hl=en&gl=US',
    appStore: 'https://apps.apple.com/us/app/galarm-alarms-and-reminders/id1187849174',
  },
  {
    title: 'bob HR',
    description: "bob's mobile app provides a seamless HR experience on the go.",
    image: require('./showcase/hibob.png'),
    playStore: 'https://play.google.com/store/apps/details?id=com.hibob',
    appStore: 'https://apps.apple.com/us/app/bob-hr/id1297148884',
  },
  {
    title: 'intelinvest',
    image: require('./showcase/intelivest.png'),
    playStore: 'https://play.google.com/store/apps/details?id=ru.intelinvest.portfolio',
    appStore: 'https://apps.apple.com/ru/app/intelinvest-учет-инвестиций/id1422478197',
  },
  {
    title: 'ChurchTools',
    description: '',
    image: require('./showcase/churchtools.png'),
    playStore: 'https://play.google.com/store/apps/details?id=tools.church.app&hl=en&gl=US',
    appStore: 'https://apps.apple.com/de/app/churchtools/id1413263051#?platform=iphone',
  },
  {
    title: 'Deevent',
    description:
      'Highlight the best attractions, places, museums and monuments in the biggest cities!',
    image: require('./showcase/deevent.png'),
    playStore: 'https://play.google.com/store/apps/details?id=app.deevent',
    appStore: 'https://apps.apple.com/us/app/deevent/id1519499216',
  },
  {
    title: 'Cinepicks',
    description:
      'Browse a huge list of movies and see which ones are available on popular streaming services.',
    image: require('./showcase/cinepicks.png'),
    playStore:
      'https://play.google.com/store/apps/details?id=co.uk.jaygould.cinepicks&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1',
    appStore: 'https://apps.apple.com/gb/app/cinepicks-find-swipe-films/id1534621509',
  },
  {
    title: 'StretchMinder',
    description: 'StretchMinder helps to increase productivity while keeping your health in check.',
    image: require('./showcase/stretchminder.png'),
    appStore: 'https://apps.apple.com/us/app/stretchminder-stand-up-move/id1518522560',
  },
  {
    title: 'Подарки и Одежда на Новый Го‪д‬',
    image: require('./showcase/christmas.png'),
    appStore: 'https://apps.apple.com/ru/app/id1446775875',
  }
];

apps.forEach((app) => {
  if (
    !app.image ||
    (app.image instanceof String && (app.image.startsWith('http') || app.image.startsWith('//')))
  ) {
    throw new Error(
      `Bad user site image preview = ${app.image}. The image should be hosted on Docusaurus site, and not use remote HTTP or HTTPS URLs`
    );
  }
});

export default apps;
