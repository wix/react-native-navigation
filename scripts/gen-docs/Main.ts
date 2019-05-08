import * as fs from 'fs';
import { ReflectionsReader } from './ReflectionsReader';
import { ClassParser } from './ClassParser';
import { MarkdownWriter } from './MarkdownWriter';
import { ReflectionKind } from 'typedoc';
import { EnumParser } from './EnumParser';

const INPUT_DIR = `${__dirname}/../../lib/src`;
const OUTPUT_DIR = `${__dirname}/../../docs/api`;
const TEMPLATES_DIR = `${__dirname}/templates`;
const TSCONFIG_PATH = `${__dirname}/../../tsconfig.json`;
const SOURCE_LINK_PREFIX = `https://github.com/wix/react-native-navigation/blob/v2/lib/src`;

class Main {
  public run() {
    const classParser = new ClassParser(SOURCE_LINK_PREFIX);
    const enumParser = new EnumParser();
    const markdownWriter = new MarkdownWriter(TEMPLATES_DIR, OUTPUT_DIR);
    const reflections = new ReflectionsReader(TSCONFIG_PATH).read(INPUT_DIR);

    const parsedClasses = classParser.parseClasses(reflections.classReflections);
    const parsedInterfaces = classParser.parseClasses(reflections.interfaceReflections);
    const parsedEnums = enumParser.parse(reflections.enumReflections);

    markdownWriter.writeClasses(parsedClasses);
    markdownWriter.writeClasses(parsedInterfaces);
    markdownWriter.writeEnums(parsedEnums);
    markdownWriter.writeMenu(parsedClasses, parsedInterfaces, parsedEnums);
  }
}

new Main().run();
