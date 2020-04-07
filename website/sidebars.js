module.exports = {
  docs: {
    'Getting Started': [
      'docs-before-you-start',
      'docs-installing',
      'docs-playground-app',
      'docs-showcases'
    ],
    'Using the app': [
      'docs-app-launch',
      'docs-basic-navigation',
      'docs-advanced-navigation',
      'docs-screen-lifecycle',
      'docs-passing-data-to-components',
    ],
    Layouts: [
      'docs-stack',
      'docs-bottomTabs',
      'docs-sideMenu',
      'docs-externalComponent',
    ],
    Hierarchy: [
      'docs-root',
      'docs-modal',
      'docs-overlay'
    ],
    Styling: [
      'style-theme',
      'style-statusBar',
      'style-orientation',
      'style-animations',
      'style-fonts',
      'style-constants'
    ],
    Meta: [
      'meta-contributing'
    ]
  },
  api: [
    {
      type: 'category',
      label: 'Navigation',
      items: [
        'api-component',
        'api-root',
        'api-stack',
        'api-modal',
        'api-overlay'
      ]
    },
    {
      type: 'category',
      label: 'Layouts',
      items: [
        'layout-layout',
        'layout-component',
        'layout-stack',
        'layout-bottomTabs',
        'layout-sideMenu',
        'layout-splitView'
      ]
    },
    {
      type: 'category',
      label: 'Options',
      items: [
        'options-api',
        'options-root',
        'options-bottomTabs',
        'options-bottomTab',
        {
          'type': 'category',
          'label': 'Stack',
          'items': [
            'options-stack',
            'options-title',
            'options-subtitle',
            'options-background',
            'options-backButton',
            'options-button',
            'options-iconInsets',
            'options-largeTitle'
          ]
        },
        'options-statusBar',
        'options-layout',
        'options-overlay',
        'options-sideMenu',
        'options-sideMenuSide',
        'options-splitView'
      ]
    },
    'api-events'
  ]
};