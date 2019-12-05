import uniqueId from 'lodash-es/uniqueId';

export class UniqueIdProvider {
  generate(prefix?: string): string {
    return uniqueId(prefix);
  }
}
