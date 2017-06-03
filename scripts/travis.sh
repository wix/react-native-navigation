#!/bin/bash -e

lightCyan='\033[1;36m'
green='\033[0;32m'
nocolor='\033[0m'

run_f () {
  cmd="${1}"
  name=${cmd//[ ]/_}

  echo "travis_fold:start:$name"
  echo -e "${lightCyan}\t\t $cmd ${nocolor}"
  SECONDS=0

  ($cmd)

  duration=$SECONDS
  echo "travis_fold:end:$name"
  echo -e "${green}\t\t\t --> $(($duration / 60)) minutes and $(($duration % 60)) seconds ${nocolor}\n"
}

run_f "yarn install"
run_f "yarn run clean"
run_f "yarn run test-js"
run_f "yarn run test-unit-android -- release"
run_f "yarn run test-unit-ios -- release"
run_f "yarn run test-e2e-android -- release"
run_f "yarn run test-e2e-ios -- release"
run_f "yarn run release"
