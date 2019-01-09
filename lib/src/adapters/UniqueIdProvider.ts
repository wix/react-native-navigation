import * as _ from 'lodash';
import { Service } from 'typedi';

@Service('UniqueIdProvider')
export class UniqueIdProvider {
  generate(prefix?: string): string {
    return _.uniqueId(prefix);
  }
}
