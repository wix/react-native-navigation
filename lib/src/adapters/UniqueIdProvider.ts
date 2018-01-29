import * as _ from 'lodash';

export class UniqueIdProvider {
  generate(prefix: string): string {
    return _.uniqueId(prefix);
  }
}
