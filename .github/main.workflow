workflow "Verify labels" {
  on = "pull_request"
  resolves = "Enforce PR label"
}

action "Enforce PR label" {
  uses = "yogevbd/enforce-label-action@1.0.0"
  secrets = ["GITHUB_TOKEN"]
  env = {
    VALID_LABELS = "bug,enhancement,feature,skip-changelog"
  }
}