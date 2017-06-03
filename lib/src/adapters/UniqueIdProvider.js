import _ from 'lodash';

export default class UniqueIdProvider {
  generate(prefix) {
    return _.uniqueId(prefix);
  }
}
