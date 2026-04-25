#!/usr/bin/env python3
"""
Generate Tamil Bible launcher icons for all Android mipmap densities.
Requires: pip install Pillow

Run: python3 scripts/generate_icons.py
"""

import os
import math

try:
    from PIL import Image, ImageDraw, ImageFont
except ImportError:
    print("Installing Pillow...")
    os.system("pip install Pillow --quiet")
    from PIL import Image, ImageDraw, ImageFont

SIZES = {
    "mipmap-mdpi":    48,
    "mipmap-hdpi":    72,
    "mipmap-xhdpi":   96,
    "mipmap-xxhdpi":  144,
    "mipmap-xxxhdpi": 192,
}

BASE = os.path.join(os.path.dirname(__file__), "..", "android", "app", "src", "main", "res")

def draw_icon(size):
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)

    # Background circle — deep navy
    margin = size * 0.04
    d.ellipse([margin, margin, size - margin, size - margin], fill="#1a237e")

    # Cross
    cx, cy = size / 2, size / 2
    arm_w = size * 0.10
    v_h = size * 0.52
    h_w = size * 0.42
    v_top = cy - v_h / 2
    h_left = cx - h_w / 2
    cross_color = "#FFD700"   # gold

    # Vertical bar
    d.rectangle([cx - arm_w/2, v_top, cx + arm_w/2, v_top + v_h], fill=cross_color)
    # Horizontal bar
    d.rectangle([h_left, cy - arm_w/2 - size*0.04, h_left + h_w, cy + arm_w/2 - size*0.04], fill=cross_color)

    # Subtle glow ring
    glow = size * 0.04
    d.ellipse([margin - glow, margin - glow, size - margin + glow, size - margin + glow],
              outline="#3949ab", width=max(1, int(size * 0.015)))

    return img

def main():
    print("Generating launcher icons...")
    for folder, size in SIZES.items():
        out_dir = os.path.join(BASE, folder)
        os.makedirs(out_dir, exist_ok=True)

        icon = draw_icon(size)
        icon.save(os.path.join(out_dir, "ic_launcher.png"))

        # Round icon (same design, just save again — Android clips to circle anyway)
        icon.save(os.path.join(out_dir, "ic_launcher_round.png"))

        print(f"  ✅ {folder}/ic_launcher.png ({size}x{size})")

    print("\nDone! Icons written to android/app/src/main/res/mipmap-*/")

if __name__ == "__main__":
    main()
