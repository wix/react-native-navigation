#!/bin/bash
# Removes private registry URLs from yarn.lock before commit

# Remove ::__archiveUrl=... suffix from resolution lines
sed -i '' 's/::__archiveUrl=[^"]*//g' yarn.lock

# Remove URL-encoded version for patch entries
sed -i '' 's/%3A%3A__archiveUrl=[^#]*//g' yarn.lock

# Stage the cleaned file
git add yarn.lock


