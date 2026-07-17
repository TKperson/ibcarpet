#!/usr/bin/env bash
set -euo pipefail

# Number of commits to show (default: 10)
COUNT="${1:-10}"

# Ensure we're in a git repo
git rev-parse --git-dir >/dev/null

# Check for fzf
if ! command -v fzf >/dev/null; then
    echo "Error: fzf is required."
    echo "Install it: https://github.com/junegunn/fzf"
    exit 1
fi

CURRENT_BRANCH=$(git branch --show-current)

echo "Current branch: $CURRENT_BRANCH"
echo

# Pick commits (TAB to mark multiple, ENTER to confirm)
COMMITS=$(
    git log --oneline --decorate --no-merges -n "$COUNT" |
    fzf --multi \
        --prompt="Select commits > " \
        --header="TAB=select multiple, ENTER=continue"
)

[[ -z "$COMMITS" ]] && {
    echo "No commits selected."
    exit 0
}

# Pick destination branch
TARGET=$(
    git for-each-ref refs/heads \
        --format='%(refname:short)' |
    grep -vx "$CURRENT_BRANCH" |
    fzf --prompt="Target branch > "
)

[[ -z "$TARGET" ]] && {
    echo "No branch selected."
    exit 0
}

echo
echo "Switching to $TARGET..."
git checkout "$TARGET"

echo

# Cherry-pick oldest first
echo "$COMMITS" |
    tac |
    while read -r line; do
        SHA=$(awk '{print $1}' <<<"$line")
        echo "Cherry-picking $SHA..."
        git cherry-pick "$SHA" || {
            echo
            echo "Cherry-pick conflict."
            echo "Resolve the conflict, then run:"
            echo "    git cherry-pick --continue"
            echo "or abort with:"
            echo "    git cherry-pick --abort"
            exit 1
        }
    done

echo
echo "Done."
