import * as Typedoc from 'typedoc';

const OPTIONS = {
  excludeExternals: true,
  excludePrivate: true,
  includeDeclarations: true,
  module: 'commonjs',
  readme: 'none',
  target: 'ES6'
};

export class ReflectionsReader {
  private typedocApp: Typedoc.Application;

  constructor() {
    this.typedocApp = new Typedoc.Application(OPTIONS);
  }

  public read(modulePath: string): Typedoc.DeclarationReflection {
    const expandedFiles = this.typedocApp.expandInputFiles([modulePath]);
    const projectReflection = this.typedocApp.convert(expandedFiles);
    const externalModule = projectReflection.getChildrenByKind(Typedoc.ReflectionKind.ExternalModule)[0];
    const classReflection = externalModule.getChildrenByKind(Typedoc.ReflectionKind.Class)[0];
    return classReflection;
  }
}
