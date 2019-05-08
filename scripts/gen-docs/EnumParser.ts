import { DeclarationReflection, ReflectionKind } from 'typedoc';

export interface EnumContext {
  name: string;
  values: string[];
}

export class EnumParser {
  public parse(enumReflections: DeclarationReflection[]): EnumContext[] {
    return enumReflections.map((r) => this.parseEnum(r));
  }

  private parseEnum(enumReflection: DeclarationReflection): EnumContext {
    return {
      name: enumReflection.name,
      values: enumReflection.getChildrenByKind(ReflectionKind.EnumMember).map((child) => child.name)
    };
  }
}
