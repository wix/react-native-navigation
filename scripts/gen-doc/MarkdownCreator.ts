import * as Handlebars from 'handlebars';
import * as Typedoc from 'typedoc';
import * as fs from 'fs';

const TEMPLATES_DIR = `./src/templates`;

export class MarkdownCreator {
  constructor(private handlebarsFn: HandlebarsTemplateDelegate<any>) { }

  public create(reflection: Typedoc.DeclarationReflection) {
    const context = {
      name: reflection.name,
      properties: this.readProperties(reflection),
      methods: this.readMethods(reflection)
    };
    return this.handlebarsFn(context);
  }

  private readMethods(reflection: Typedoc.DeclarationReflection) {
    const methodReflections = reflection.getChildrenByKind(Typedoc.ReflectionKind.Method);
    methodReflections.sort((a, b) => a.sources[0].line - b.sources[0].line);
    return methodReflections.map((methodReflection) => ({
      name: methodReflection.name,
      arguments: this.readArguments(methodReflection.signatures[0].parameters || []),
      returnType: methodReflection.signatures[0].type.toString(),
      source: `${methodReflection.sources[0].fileName}#${methodReflection.sources[0].line}`,
      comment: methodReflection.signatures[0].comment ? methodReflection.signatures[0].comment.shortText : ''
    }));
  }

  private readArguments(parameters: Typedoc.ParameterReflection[]) {
    return parameters.map((parameter) => ({
      name: parameter.name,
      type: parameter.type.toString()
    }));
  }

  private readProperties(reflection: Typedoc.DeclarationReflection) {
    const propsReflections = reflection.getChildrenByKind(Typedoc.ReflectionKind.Property);
    return propsReflections.map((propReflection) => ({
      name: propReflection.name,
      type: propReflection.type.toString()
    }));
  }

  private setupTemplates() {
    const mainTemplate = fs.readFileSync(`${TEMPLATES_DIR}/main.hbs`).toString();
    const classTemplate = fs.readFileSync(`${TEMPLATES_DIR}/class.hbs`).toString();
    const methodTemplate = fs.readFileSync(`${TEMPLATES_DIR}/method.hbs`).toString();
    Handlebars.registerPartial('class', classTemplate);
    Handlebars.registerPartial('method', methodTemplate);
    return Handlebars.compile(mainTemplate, { strict: true });
  }
}
