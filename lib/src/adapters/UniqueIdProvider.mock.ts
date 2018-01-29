export class UniqueIdProvider {
  generate(prefix: string): string {
    return `${prefix}+UNIQUE_ID`;
  }
}
