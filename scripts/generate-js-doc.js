const jsdoc2md = require('jsdoc-to-markdown');
const fs = require('fs');
const path = require('path');

const BASE_DIR = './lib/src/params/';
const OPTIONS_DIR = BASE_DIR + 'options/';
const CONTAINERS_DIR = BASE_DIR + 'containers/';
const OUTPUT_DIR = './docs/docs/';
const PARAMS_PARTIALS = ['./docs/templates/header.hbs', './docs/templates/sig-name.hbs'];
const PARTIALS = [
  './docs/templates/scope.hbs',
  './docs/templates/docs.hbs',
  './docs/templates/param-table-name.hbs',
  '/docs/templates/linked-type-list.hbs',
  './docs/templates/link.hbs',
  './docs/templates/params-table.hbs'
];

const generateMarkdownForFile = ({ file, outputDir, partial, separator }) => {
  const templateData = jsdoc2md.getTemplateDataSync({ files: file });
  const classNames = getClassesInFile(templateData);
  classNames.forEach((className) => createDocFileForClass({ className, templateData, outputDir, partial, separator }));
};

function getClassesInFile(templateData) {
  const classNames = templateData.reduce((classNames, identifier) => {
    if (identifier.kind === 'class') {
      classNames.push(identifier.name);
    }
    return classNames;
  }, []);
  return classNames;
}

function createDocFileForClass({ className, templateData, outputDir, partial = [], separator = true }) {
  const template = `{{#class name="${className}"}}{{>docs}}{{/class}}`;
  const options = {
    data: templateData,
    template,
    separators: separator,
    'heading-depth': 1,
    helper: ['./docs/linkify.js', './docs/stringify.js'],
    partial: [...PARTIALS, ...partial]
  };
  console.log(`rendering ${className}`);
  const output = jsdoc2md.renderSync(options);
  fs.writeFileSync(path.resolve(outputDir, `${className}.md`), output);
}

function inputFiles() {
  return [
    { file: './lib/src/Navigation.js', outputDir: OUTPUT_DIR },
    ...fs.readdirSync(OPTIONS_DIR).map((file) => {
      return {
        file: OPTIONS_DIR + file,
        outputDir: OUTPUT_DIR + 'options/',
        partial: PARAMS_PARTIALS,
        separator: false
      };
    }),
    ...fs.readdirSync(CONTAINERS_DIR)
        .map((file) => {
          return {
            file: CONTAINERS_DIR + file,
            outputDir: OUTPUT_DIR,
            partial: PARAMS_PARTIALS,
            separator: false
          };
        }),
    ...fs.readdirSync(BASE_DIR)
        .filter((file) => fs.statSync(BASE_DIR + file).isFile())
        .map((file) => {
          return {
            file: BASE_DIR + file,
            outputDir: OUTPUT_DIR
          };
        })
  ];
}

inputFiles().forEach((inputFile) => generateMarkdownForFile(inputFile));
