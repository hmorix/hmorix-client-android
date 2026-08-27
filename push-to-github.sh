#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────────
#  HMorix Client Android – GitHub Push Helper Script
#  Usage: ./push-to-github.sh <your-github-username> <repo-name>
#  Example: ./push-to-github.sh johndoe hmorix-client-android
# ─────────────────────────────────────────────────────────────────────────────

set -e

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Usage: ./push-to-github.sh <github-username> <repo-name>"
  echo "Example: ./push-to-github.sh johndoe hmorix-client-android"
  exit 1
fi

GITHUB_USER="$1"
REPO_NAME="$2"
REMOTE_URL="https://github.com/${GITHUB_USER}/${REPO_NAME}.git"

echo "──────────────────────────────────────────────"
echo " HMorix Client Android - GitHub Push Setup"
echo "──────────────────────────────────────────────"
echo " User:   ${GITHUB_USER}"
echo " Repo:   ${REPO_NAME}"
echo " Remote: ${REMOTE_URL}"
echo ""

# Make gradlew executable
chmod +x gradlew
echo "✅  gradlew marked executable"

# Initialize git repo if not already initialized
if [ ! -d ".git" ]; then
  git init
  echo "✅  Git repository initialized"
fi

# Stage all files
git add .
echo "✅  Files staged"

# Commit (handle case where already committed)
git diff --staged --quiet || git commit -m "feat: HMorix Kotlin Android Client App v1.0 — Jetpack Compose + Material3 Cyber Theme"
echo "✅  Initial commit ready"

# Rename branch
git branch -M main
echo "✅  Branch set to 'main'"

# Remove old remote and add new one
git remote remove origin 2>/dev/null || true
git remote add origin "${REMOTE_URL}"
echo "✅  Remote set to: ${REMOTE_URL}"

echo ""
echo "──────────────────────────────────────────────"
echo " NEXT STEPS:"
echo "──────────────────────────────────────────────"
echo " 1. Create a NEW EMPTY repo on GitHub:"
echo "    → https://github.com/new"
echo "    Name: ${REPO_NAME}"
echo "    ⚠️  DO NOT initialize with README, .gitignore, or license"
echo ""
echo " 2. Push with:"
echo "    git push -u origin main"
echo ""
echo " 3. After pushing, go to:"
echo "    https://github.com/${GITHUB_USER}/${REPO_NAME}/actions"
echo "    → Click 'Build HMorix Client APK (Free)'"
echo "    → Click 'Run workflow' to build your APK for free!"
echo ""
echo " 4. To open in Google Project IDX (Cloud Android Studio):"
echo "    → https://idx.dev → Import Repo → ${REMOTE_URL}"
echo "──────────────────────────────────────────────"
