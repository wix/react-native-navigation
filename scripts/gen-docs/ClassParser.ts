import * as Typedoc from 'typedoc';

export interface PropertyContext {
  name: string;
  type: string;
}

export interface ArgumentContext {
  name: string;
  type: string;
}

export interface MethodContext {
  name: string;
  arguments: ArgumentContext[];
  returnType: string;
  source: string;
  line: number;
  comment?: string;
}

export interface ClassContext {
  name: string;
  properties: PropertyContext[];
  methods: MethodContext[];
}

export class ClassParser {
  constructor(private sourceLinkPrefix: string) { }

  public parseClasses(classReflections: Typedoc.DeclarationReflection[]): ClassContext[] {
    return classReflections.map((r) => this.parseClass(r));
  }

  private parseClass(classReflection: Typedoc.DeclarationReflection): ClassContext {
    return {
      name: classReflection.name,
      properties: this.parseProperties(classReflection),
      methods: this.parseMethods(classReflection)
    };
  }

  private parseMethods(reflection: Typedoc.DeclarationReflection): MethodContext[] {
    const methodReflections = reflection.getChildrenByKind(Typedoc.ReflectionKind.Method);
    const methods = methodReflections.map((m) => this.parseMethod(m))
      .filter((m) => !m.source.includes('/node_modules/'))
      .sort((a, b) => a.line - b.line);
    return methods;
  }

  private parseMethod(methodReflection: Typedoc.DeclarationReflection) {
    const name = methodReflection.name;
    const line = methodReflection.sources[0].line;
    const fileName = methodReflection.sources[0].fileName;
    const source = `${this.sourceLinkPrefix}/${fileName}#L${line}`;
    const comment = methodReflection.signatures[0].comment ? methodReflection.signatures[0].comment.shortText : '';
    const args = this.parseArguments(methodReflection.signatures[0].parameters || []);
    const returnType = methodReflection.signatures[0].type.toString();
    return {
      name,
      arguments: args,
      returnType,
      source,
      line,
      comment
    };
  }

  private parseArguments(parameters: Typedoc.ParameterReflection[]): ArgumentContext[] {
    return parameters.map((parameter) => ({
      name: parameter.name,
      type: parameter.type.toString()
    }));
  }

  private parseProperties(reflection: Typedoc.DeclarationReflection): PropertyContext[] {
    const propsReflections = reflection.getChildrenByKind(Typedoc.ReflectionKind.Property);
    return propsReflections.map((propReflection) => ({
      name: propReflection.name,
      type: propReflection.type.toString()
    }));
  }
}
