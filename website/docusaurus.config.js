const remarkCodeImport = require('remark-code-import');

const versions = require('./versions.json');

module.exports = {
  title: 'React Native Navigation',
  tagline: '',
  url: 'https://wix.github.io',
  baseUrl: '/react-native-navigation/',
  favicon: 'img/favicon.ico',
  organizationName: 'wix', // Usually your GitHub org/user name.
  projectName: 'react-native-navigation', // Usually your repo name.
  themeConfig: {
    prism: {
      // theme: require('prism-react-renderer/themes/vsDark'),
      theme: require('prism-react-renderer/themes/nightOwl'),
      // theme: require('prism-react-renderer/themes/dracula'),
    },
    navbar: {
      title: 'React Native Navigation',
      logo: {
        alt: 'React Native Navigation Logo',
        src: 'img/logo.png',
      },
      items: [
        {
          type: 'docsVersionDropdown',
          position: 'left',
        },
        {
          type: 'doc',
          position: 'left',
          docId: 'docs/before-you-start',
          label: 'Docs',
        },
        {
          type: 'doc',
          position: 'left',
          docId: 'api/component',
          label: 'API',
        },
        {
          to: 'showcase',
          label: 'Showcase',
          position: 'left',
        },
        {
          href: 'https://github.com/wix/react-native-navigation',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    algolia: {
      apiKey: '6d8c985d9db80241d117497afe2a0e8c',
      indexName: 'wix_react-native-navigation',
      contextualSearch: true
    },
    sidebarCollapsible: false,
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Installation',
              to: 'docs/installing',
            },
            {
              label: 'Basic Navigation',
              to: 'docs/basic-navigation',
            },
            {
              label: 'Contributing',
              to: 'docs/meta-contributing',
            },
          ],
        },
        {
          title: 'Support',
          items: [
            {
              label: 'Ask a question on Stack Overflow',
              href: 'https://stackoverflow.com/questions/tagged/wix-react-native-navigation',
            },
            {
              label: 'Community chat on Discord',
              href: 'https://discord.gg/DhkZjq2',
            },
            {
              label: 'Submit on issue on GitHub',
              href: 'https://github.com/wix/react-native-navigation/issues/new/choose',
            },
          ],
        },
        {
          title: 'Social',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/wix/react-native-navigation',
            },
            {
              label: 'Twitter',
              href: 'https://twitter.com/ReactNativeNav',
            },
          ],
        },
      ],
    },
  },
  plugins: [
    [
      '@docusaurus/plugin-ideal-image',
      {
        quality: 70,
        max: 1030, // max resized image's size.
        min: 640, // min resized image's size. if original is lower, use that size.
        steps: 2, // the max number of images generated between min and max (inclusive)
      },
    ],
  ],
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          routeBasePath: '/',
          path: 'docs',
          editUrl: 'https://github.com/wix/react-native-navigation/edit/master/website',
          remarkPlugins: [remarkCodeImport],
          docLayoutComponent: '@site/src/components/CustomLayout'
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
