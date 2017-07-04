#!/bin/sh
# Usage: fetchall.sh branch ...

set -x
git fetch --all
for branch in "$@"; do
    git checkout "$branch"      || exit 1
    git rebase "origin/$branch" || exit 1
done
