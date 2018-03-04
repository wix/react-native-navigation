import * as _ from 'lodash';
import * as Typedoc from 'typedoc';
import * as Handlebars from 'handlebars';
import * as fs from 'fs';

const ROOT_DIR = `${__dirname}/..`;
const SRC_DIR = `${ROOT_DIR}/lib/src`;
const DOCS_DIR = `${ROOT_DIR}/docs`;
const TEMPLATES_DIR = `${DOCS_DIR}/templates`;
const API_DIR = `${DOCS_DIR}/api`;

class Main {
  public generateApiDocsMarkdown() {
    const moduleName = 'Navigation';
    const reflections = new ReflectionsReader().read(moduleName);
    const classRef = new Parser().parseClass(reflections);
    new MarkdownCreator().create(classRef);
  }
}

class MarkdownCreator {
  private handlebarsFn: HandlebarsTemplateDelegate<any>;
  constructor() {
    this.handlebarsFn = this.setupHandlebars();
  }

  public create(context: ClassReflection) {

    const result = this.handlebarsFn(context);

    // console.log(result);
    API_DIR.toString();
  }

  private setupHandlebars(): HandlebarsTemplateDelegate<any> {
    const mainTemplate = readFile(`${TEMPLATES_DIR}/main.hbs`);
    const classTemplate = readFile(`${TEMPLATES_DIR}/class.hbs`);
    const methodTemplate = readFile(`${TEMPLATES_DIR}/method.hbs`);
    Handlebars.registerPartial('class', classTemplate);
    Handlebars.registerPartial('method', methodTemplate);
    return Handlebars.compile(mainTemplate, { strict: true });
  }
}

interface ClassReflection {
  name: string;
  methods: MethodReflection[];
}

interface MethodReflection {
  name: string;
  argumentValues: string[];
  returnValue: string;
  comment: string;
  source: string;
}

class Parser {
  public parseClass(reflections): ClassReflection {
    const theModuleRaw = reflections.children[0];
    const theClassRaw = theModuleRaw.children[0];

    const methods = this.parseMethods(theClassRaw);
    const result = {
      name: theClassRaw.name,
      methods
    };
    return result;
  }

  private parseMethods(theClassRaw): MethodReflection[] {
    const methodsRaw = _.filter(theClassRaw.children, (child) => child.kind === Typedoc.ReflectionKind.Method);
    methodsRaw.toString();
    const result = [{
      name: 'theName',
      argumentValues: ['asd', 'zxc'],
      returnValue: 'returnVal',
      comment: 'bla bla bla',
      source: 'fromHere'
    }];

    return result;
  }
}

class ReflectionsReader {
  private typedocApp: Typedoc.Application;
  constructor() {
    const tsconfig = JSON.parse(readFile(`${ROOT_DIR}/tsconfig.json`));
    this.typedocApp = new Typedoc.Application({
      excludeExternals: true,
      excludePrivate: true,
      excludeProtected: true,
      includeDeclarations: true,
      module: 'commonjs',
      readme: 'none',
      target: 'ES6',
      ...tsconfig.compilerOptions
    });
  }
  read(moduleName) {
    return this.typedocApp.serializer.projectToObject(
      this.typedocApp.convert(
        this.typedocApp.expandInputFiles(
          [`${SRC_DIR}/${moduleName}.ts`]
        )
      )
    );
  }
}

function readFile(f) {
  return fs.readFileSync(f).toString();
}

new Main().generateApiDocsMarkdown();
