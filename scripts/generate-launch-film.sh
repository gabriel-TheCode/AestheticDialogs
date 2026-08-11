#!/usr/bin/env bash
#
# Renders the launch film into docs/video/aestheticdialogs-2.0.mp4.
#
#   scripts/generate-launch-film.sh
#
# Three stages, all reproducible:
#
#   1. DocsFilmPlatesTest  — each component alone, captured twice: on white and
#                            on black
#   2. cut-out             — the two passes solve for the component's real alpha,
#                            which is what lets the shadow be dropped and the
#                            surface be composited on any background
#   3. DocsLaunchFilmTest  — 288 frames, the film being a pure function of time
#   4. ffmpeg              — H.264, 1920x1080, 24fps
#
# The dialogs in the film are rendered by the library itself. Nothing in the
# animation can restyle them: it only moves, scales and fades finished pixels.

set -euo pipefail

cd "$(dirname "$0")/.."

BUILD=aestheticdialogs/build/film
OUT=docs/video/aestheticdialogs-2.0.mp4

command -v ffmpeg >/dev/null || { echo "ffmpeg is required (brew install ffmpeg)"; exit 1; }

echo "==> 1/3  Rendering component plates"
rm -rf "$BUILD/plates" "$BUILD/cut" "$BUILD/frames"
./gradlew --quiet :aestheticdialogs:recordRoborazziDebug --rerun-tasks \
  --tests 'com.thecode.aestheticdialogs.DocsFilmPlatesTest'

echo "==> 2/3  Cutting the components out of their background"
python3 - "$BUILD" <<'PY'
import glob, os, struct, subprocess, sys, zlib

build = sys.argv[1]
os.makedirs(f'{build}/cut', exist_ok=True)

def rgb(path):
    return subprocess.run(
        ['ffmpeg', '-v', 'quiet', '-i', path, '-f', 'rawvideo', '-pix_fmt', 'rgb24', '-'],
        capture_output=True,
    ).stdout

def write_rgba(path, w, h, px):
    def chunk(tag, data):
        body = tag + data
        return struct.pack('>I', len(data)) + body + struct.pack('>I', zlib.crc32(body) & 0xffffffff)
    rows = b''.join(b'\x00' + px[y * w * 4:(y + 1) * w * 4] for y in range(h))
    open(path, 'wb').write(
        b'\x89PNG\r\n\x1a\n'
        + chunk(b'IHDR', struct.pack('>IIBBBBB', w, h, 8, 6, 0, 0, 0))
        + chunk(b'IDAT', zlib.compress(rows, 9))
        + chunk(b'IEND', b'')
    )

for white_path in sorted(glob.glob(f'{build}/plates/*.png')):
    if '@black' in white_path:
        continue
    black_path = white_path.replace('.png', '@black.png')
    header = open(white_path, 'rb').read(24)
    w = int.from_bytes(header[16:20], 'big')
    h = int.from_bytes(header[20:24], 'big')
    over_white, over_black = rgb(white_path), rgb(black_path)

    out = bytearray(w * h * 4)
    left, top, right, bottom = w, h, -1, -1
    for i in range(w * h):
        j = i * 3
        # A pixel of colour C with alpha a over background B reads as C*a + B*(1-a).
        # Over white and over black that is two equations: a = 1 - (white - black).
        a = sum(255 - (over_white[j + k] - over_black[j + k]) for k in range(3)) // 3
        # The shadow sits well below the surface's own alpha. A steep curve drops
        # it while keeping the antialiased edge of the rounded corners.
        af = min(1.0, max(0.0, (a - 200) / 55))
        k4 = i * 4
        for k in range(3):
            out[k4 + k] = min(255, int(over_black[j + k] / af)) if af > 0.01 else 255
        out[k4 + 3] = int(af * 255)
        if af * 255 > 8:
            x, y = i % w, i // w
            left, right = min(left, x), max(right, x)
            top, bottom = min(top, y), max(bottom, y)

    if right < 0:
        continue
    cw = (right - left + 1) & ~1
    ch = (bottom - top + 1) & ~1
    crop = bytearray(cw * ch * 4)
    for y in range(ch):
        start = ((top + y) * w + left) * 4
        crop[y * cw * 4:(y + 1) * cw * 4] = out[start:start + cw * 4]
    write_rgba(f'{build}/cut/{os.path.basename(white_path)}', cw, ch, crop)
    print(f'    {os.path.basename(white_path):24} -> {cw}x{ch}')
PY

echo "==> 3/3  Rendering frames and encoding"
./gradlew --quiet :aestheticdialogs:recordRoborazziDebug --rerun-tasks \
  --tests 'com.thecode.aestheticdialogs.DocsLaunchFilmTest'

mkdir -p "$(dirname "$OUT")"
ffmpeg -y -v error -framerate 24 -i "$BUILD/frames/frame_%04d.png" \
  -c:v libx264 -preset slow -crf 18 -pix_fmt yuv420p -movflags +faststart "$OUT"

echo
echo "==> $OUT"
ffprobe -v error -show_entries format=duration,size \
  -show_entries stream=width,height,r_frame_rate \
  -of default=noprint_wrappers=1 "$OUT"
