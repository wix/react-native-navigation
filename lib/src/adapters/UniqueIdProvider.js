const _ = require('lodash');

class UniqueIdProvider {
  generate(prefix) {
    return _.uniqueId(prefix);
  }
}

module.exports = UniqueIdProvider;
