class UniqueIdProvider {
  generate(prefix) {
    return `${prefix}+UNIQUE_ID`;
  }
}

module.exports = UniqueIdProvider;
