#!/bin/bash -e

ts-node --typeCheck --compilerOptions '{"types":["node"]}' $1
