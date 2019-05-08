import * as _ from 'lodash';
import * as Handlebars from 'handlebars';
import * as fs from 'fs';
import { ClassContext } from './ClassParser';
import { EnumContext } from './EnumParser';

export class MarkdownWriter {
  private classFn;
  private enumFn;
  private menuFn;
  constructor(private templatesDir: string, private outputDir: string) {
    this.classFn = this.setupClassHandlebars();
    this.enumFn = this.setupEnumHandlebars();
    this.menuFn = this.setupMenuHandlebars();
  }

  public writeClasses(classContexts: ClassContext[]) {
    classContexts.forEach((c) => {
      const markdown = this.classFn(c);
      fs.writeFileSync(`${this.outputDir}/${c.name}.md`, markdown, { encoding: 'utf8' });
    });
  }

  public writeEnums(enumContexts: EnumContext[]) {
    enumContexts.forEach((c) => {
      const markdown = this.enumFn(c);
      fs.writeFileSync(`${this.outputDir}/${c.name}.md`, markdown, { encoding: 'utf8' });
    });
  }

  public writeMenu(classContexts: ClassContext[], interfaceContexts: ClassContext[], enumContexts: EnumContext[]) {
    const mapper = (c) => ({ name: c.name, path: `/api/${c.name}` });
    const files = classContexts.map(mapper);
    const files2 = interfaceContexts.map(mapper);
    const files3 = enumContexts.map(mapper);

    const menuMarkdown = this.menuFn({ files: _.concat(files, files2, files3) });
    fs.writeFileSync(`${this.outputDir}/_sidebar.md`, menuMarkdown, { encoding: 'utf8' });
    fs.writeFileSync(`${this.outputDir}/README.md`, menuMarkdown, { encoding: 'utf8' });
  }

  private setupClassHandlebars() {
    const classTemplate = fs.readFileSync(`${this.templatesDir}/class.hbs`).toString();
    const methodTemplate = fs.readFileSync(`${this.templatesDir}/method.hbs`).toString();
    const propertyTemplate = fs.readFileSync(`${this.templatesDir}/property.hbs`).toString();

    Handlebars.registerPartial('class', classTemplate);
    Handlebars.registerPartial('method', methodTemplate);
    Handlebars.registerPartial('property', propertyTemplate);

    return Handlebars.compile('{{> class}}', { strict: true, noEscape: true });
  }

  private setupEnumHandlebars() {
    const enumTemplate = fs.readFileSync(`${this.templatesDir}/enum.hbs`).toString();
    return Handlebars.compile(enumTemplate, { strict: true, noEscape: true });
  }

  private setupMenuHandlebars() {
    const template = fs.readFileSync(`${this.templatesDir}/menu.hbs`).toString();
    return Handlebars.compile(template, { strict: true, noEscape: true });
  }
}
