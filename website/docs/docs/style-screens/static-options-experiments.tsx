import { NavigationComponent, Options } from 'react-native-navigation';

class ExperimentScreen extends NavigationComponent {
  static options(): Options {
    const ExperimentsManager = require('./ExperimentsManager');
    const food = ExperimentsManager.isActive('VeganExperiment') ? 'Tofu' : 'Hamburger';

    return {
      topBar: {
        title: {
          text: `Hello ${food}!`,
        },
      },
    };
  }
}
