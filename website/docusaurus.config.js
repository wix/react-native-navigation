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
        { to: 'docs/before-you-start', label: 'Docs', position: 'left', activeBasePath: 'docs' },
        { to: 'api/component', label: 'API', position: 'left', activeBasePath: 'api' },
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
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          routeBasePath: '/',
          path: 'docs',
          editUrl: 'https://github.com/wix/react-native-navigation/edit/master/website',
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
