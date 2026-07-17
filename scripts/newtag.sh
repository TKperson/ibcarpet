#!/usr/bin/env bash
set -euo pipefail

TAG="$(git branch --show-current)-$(date +%y.%m.%d)"

# If the tag already exists, delete it so it can be recreated at HEAD.
if git rev-parse -q --verify "refs/tags/$TAG" >/dev/null; then
    echo "Replacing existing tag: $TAG"
    git tag -d "$TAG"
fi

# Create the tag at the current commit.
git tag "$TAG"

# Only update gradle.properties if needed.
if grep -qx "mod_version=$TAG" gradle.properties; then
    echo "mod_version is already $TAG"
else
    sed -i "s/^mod_version=.*/mod_version=$TAG/" gradle.properties

    git add gradle.properties
    git commit -m "chore: bump version"

    # Move the tag to the new commit containing the version bump.
    git tag -f "$TAG"
fi
