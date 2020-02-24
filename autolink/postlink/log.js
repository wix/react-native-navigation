const bColors = {
  HEADER    : '\033[95m',
  OKBLUE    : '\033[94m',
  OKGREEN   : '\033[92m',
  WARNING   : '\033[93m',
  FAIL      : '\033[91m',
  ENDC      : '\033[0m', 
  BOLD      : '\033[1m',   
  UNDERLINE : '\033[4m'
}

const log = (text) => process.stdout.write(text);
const logn = (text) => process.stdout.write(text + "\n");
const warn = (text) => process.stdout.write(`${bColors.WARNING}${text}${bColors.ENDC}`)
const warnn = (text) => warn(text + "\n");
const error = (text) => process.stdout.write(`${bColors.FAIL}${text}${bColors.ENDC}`)
const errorn = (text) => error(text + "\n");
const info = (text) => process.stdout.write(`${bColors.OKGREEN}${text}${bColors.ENDC}`)
const infon = (text) => info(text + "\n");
const debug = (text) => process.stdout.write(`${bColors.OKBLUE}${text}${bColors.ENDC}`)
const debugn = (text) => debug(text + "\n");

module.exports = {
  log,
  logn,
  warn,
  warnn,
  info,
  infon,
  debug,
  debugn,
  errorn
}