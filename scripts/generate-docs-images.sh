#!/usr/bin/env bash
#
# Regenerates every image the documentation shows, into docs/images/.
#
#   scripts/generate-docs-images.sh              # header, gallery stills and motion gifs
#   scripts/generate-docs-images.sh --baselines  # the above plus the screenshot-test baselines
#
# The images are rendered by Robolectric on the JVM, so this needs no emulator,
# no device and no screen recorder — and it produces the same bytes on every
# machine, which is what makes the documentation reviewable in a pull request.
#
# The sources are in aestheticdialogs/src/test/java/.../Docs*.kt. Add a dialog
# there and it appears here.

set -euo pipefail

cd "$(dirname "$0")/.."

FILTERS=(
  --tests 'com.thecode.aestheticdialogs.DocsHeroImageTest'
  --tests 'com.thecode.aestheticdialogs.DocsGalleryTest'
  --tests 'com.thecode.aestheticdialogs.DocsMotionGifTest'
)

if [[ "${1:-}" == "--baselines" ]]; then
  # No filter: record the visual-regression baselines in the same pass.
  FILTERS=()
  shift
fi

echo "==> Rendering documentation images"
./gradlew :aestheticdialogs:recordRoborazziDebug "${FILTERS[@]}" "$@"

echo
echo "==> docs/images"
ls -1 docs/images | sed 's/^/    /'
echo
echo "Done. Review the diff before committing — these files are the README."
