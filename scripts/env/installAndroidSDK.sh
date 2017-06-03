#!/bin/bash -e

scriptdir="$(dirname "${BASH_SOURCE[0]}")"

export ANDROID_HOME=$HOME/android-sdk-macosx
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# fix for https://code.google.com/p/android/issues/detail?id=223424
mkdir -p ~/.android

# download android SDK
echo "Downloading Android SDK"
curl --location https://dl.google.com/android/android-sdk_r24.4.1-macosx.zip | tar -x -z -C $HOME

# copy licenses
echo "Copying Android licenses"
mkdir -p "${ANDROID_HOME}"/licenses
cp "$scriptdir/android-sdk-licenses/"* "${ANDROID_HOME}"/licenses
