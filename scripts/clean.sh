#!/bin/sh -e

watchman watch-del-all 2> /dev/null || true
rm -rf "$TMPDIR/react-packager-*" || true
