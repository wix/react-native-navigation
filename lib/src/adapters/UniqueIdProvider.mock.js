export default class UniqueIdProvider {
  generate(prefix) {
    return `${prefix}+UNIQUE_ID`;
  }
}

