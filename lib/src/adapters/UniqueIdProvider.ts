import uniqueId from 'lodash/uniqueId';

export class UniqueIdProvider {
  generate(prefix?: string): string {
    return uniqueId(prefix);
  }
}
