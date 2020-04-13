module.exports = {
  api: [
    {
      type: 'category',
      label: 'Navigation',
      items: [
        'component',
        'root',
        'stack',
        'modal',
        'overlay'
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
    'events'
  ]
};