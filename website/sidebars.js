module.exports = {
  docs: {
    'Getting Started': [
      'before-you-start',
      'installing',
      'minimalSetup',
      'showcases'
    ],
    'Using the app': [
      'app-launch',
      'basic-navigation',
      'advance-navigation',
      'screen-lifecycle',
      'passing-data-to-components',
      'using-react-context',
      'using-mobx',
    ],
    Layouts: [
      'stack-doc',
      'bottomTabs-docs',
      'side-menu',
      'external-component',
    ],
    Hierarchy: [
      'root',
      'modal-docs',
      'overlay-docs'
    ],
    Styling: [
      'theme',
      'statusBar-docs',
      'orientation',
      'screen-animations',
      'fonts',
      'constants-docs'
    ],
    Meta: [
      'contributing'
    ]
  },
  API: [
    {
      type: 'category',
      label: 'Navigation',
      items: [
        'component-api',
        'root-api',
        'stack-api',
        'modal-api',
        'overlay-api'
      ]
    },
    {
      type: 'category',
      label: 'Layouts',
      items: [
        'layout',
        'component',
        'stack',
        'bottomTabs',
        'sideMenu',
        'splitView'
      ]
    },
    {
      type: 'category',
      label: 'Options',
      items: [
        'options-api',
        'options-root',
        'bottomTabs-options',
        'bottomTab-options',
        {
          'type': 'category',
          'label': 'Stack',
          'items': [
            'stack-options',
            'title-options',
            'subtitle-options',
            'background-options',
            'backButton-options',
            'button-options',
            'iconInsets-options',
            'largeTitle-options'
          ]
        },
        'statusBar-options',
        'layout-options',
        'overlay-options',
        'sideMenu-options',
        'sideMenuSide-options',
        'splitView-options'
      ]
    },
    'events-api'
  ]
};