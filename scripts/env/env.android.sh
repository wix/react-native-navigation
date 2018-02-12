#!/bin/bash -e

scriptdir="$(dirname "${BASH_SOURCE[0]}")"

export ANDROID_HOME=$HOME/android-sdk
export PATH=$PATH:$ANDROID_HOME/bin:$ANDROID_HOME/platform-tools

mkdir $ANDROID_HOME

# fix for https://code.google.com/p/android/issues/detail?id=223424
mkdir -p ~/.android
touch ~/.android/repositories.cfg

echo "Downloading Android SDK"
curl --location https://dl.google.com/android/repository/sdk-tools-linux-3859397.zip | tar -x -z -C $ANDROID_HOME

echo "Accepting Android Licenses"
yes | $ANDROID_HOME/bin/sdkmanager --licenses
