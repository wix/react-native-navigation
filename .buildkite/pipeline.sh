#!/bin/bash -e

# Native jobs take ~2.5h on a single agent, so skip them when the change cannot affect them.
# Anything unexpected (diff failed, empty diff, any other path touched) runs the full matrix.
# Master has no PR base: compare with the previous commit (squash merges keep it to one PR).
SKIP_PATHS='^(docs/|website/|\.github/|\.claude/|.*\.md$|LICENSE$)'

if [ -n "$BUILDKITE_PULL_REQUEST_BASE_BRANCH" ]; then
  changed=$(git diff --name-only "origin/$BUILDKITE_PULL_REQUEST_BASE_BRANCH...HEAD" 2>/dev/null) || changed=""
else
  changed=$(git diff --name-only HEAD~1 HEAD 2>/dev/null) || changed=""
fi

echo "steps:"

if [ -n "$changed" ] && ! echo "$changed" | grep -qvE "$SKIP_PATHS"; then
  cat <<'YAML'
  - label: ":fast_forward: Native CI skipped (docs/website/workflows only)"
    command: "echo 'Only non-native paths changed, see .buildkite/pipeline.sh'"
YAML
  exit 0
fi

cat .buildkite/jobs/pipeline.android_rn_78.yml
cat .buildkite/jobs/pipeline.android_rn_84.yml
cat .buildkite/jobs/pipeline.android_rn_85.yml
cat .buildkite/jobs/pipeline.ios_rn_78.yml
cat .buildkite/jobs/pipeline.ios_rn_84.yml
cat .buildkite/jobs/pipeline.ios_rn_85.yml
