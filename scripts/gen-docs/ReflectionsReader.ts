import * as fs from 'fs';
import { ReflectionKind, ProjectReflection, DeclarationReflection, Application } from 'typedoc';

const OPTIONS = {
  excludeExternals: true,
  excludePrivate: true,
  includeDeclarations: true,
  mode: 'modules',
  module: 'commonjs',
  readme: 'none',
  target: 'ES6'
};

export interface Reflections {
  classReflections: DeclarationReflection[];
  interfaceReflections: DeclarationReflection[];
  enumReflections: DeclarationReflection[];
}

export class ReflectionsReader {
  private typedocApp: Application;

  constructor(tsconfigPath) {
    const tsconfig = JSON.parse(fs.readFileSync(tsconfigPath).toString());
    this.typedocApp = new Application({ ...OPTIONS, ...tsconfig.compilerOptions });
  }

  public read(rootPath: string): Reflections {
    const expandedFiles = this.typedocApp.expandInputFiles([rootPath]);
    const projectReflection = this.typedocApp.convert(expandedFiles);
    // console.log(JSON.stringify(this.typedocApp.serializer.projectToObject(projectReflection)));

    const externalModules = this.externalModulesWithoutTestsAndMocks(projectReflection);
    const classReflections = this.reflections(externalModules, ReflectionKind.Class);
    const interfaceReflections = this.reflections(externalModules, ReflectionKind.Interface);
    const enumReflections = this.reflections(externalModules, ReflectionKind.Enum);

    return {
      classReflections,
      interfaceReflections,
      enumReflections
    };
  }

  private externalModulesWithoutTestsAndMocks(projectReflection: ProjectReflection): DeclarationReflection[] {
    return projectReflection.getChildrenByKind(ReflectionKind.ExternalModule)
      .filter((m) => !m.name.endsWith('.mock"') && !m.name.endsWith('.test"'));
  }

  private reflections(externalModules: DeclarationReflection[], kind: ReflectionKind): DeclarationReflection[] {
    return externalModules.filter((m) => m.getChildrenByKind(kind).length === 1)
      .map((m) => m.getChildrenByKind(kind)[0]);
  }
}
