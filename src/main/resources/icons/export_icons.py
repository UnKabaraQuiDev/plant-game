#!/usr/bin/python

import glob
import os
import cairosvg
import re

sizes = [16, 24, 32, 48, 64, 128, 256]

svg_files = glob.glob("__*.svg")

def get_svg_dimensions(svg_path):
    with open(svg_path, "r", encoding="utf-8") as f:
        content = f.read()

    width_match = re.search(r'width="([\d.]+)', content)
    height_match = re.search(r'height="([\d.]+)', content)

    if width_match and height_match:
        width = float(width_match.group(1))
        height = float(height_match.group(1))
    else:
        viewbox_match = re.search(r'viewBox="[\d.\s]+([\d.]+)\s+([\d.]+)"', content)
        if viewbox_match:
            width = float(viewbox_match.group(1))
            height = float(viewbox_match.group(2))
        else:
            raise ValueError(f"Cannot find dimensions for {svg_path}")

    return width, height


for svg_file in svg_files:
    base_name = os.path.splitext(svg_file)[0].lstrip("_")

    try:
        orig_w, orig_h = get_svg_dimensions(svg_file)
    except ValueError as e:
        print(e)
        continue

    aspect = orig_w / orig_h

    for size in sizes:
        if orig_w < orig_h:
            width = size
            height = int(size / aspect)
        else:
            height = size
            width = int(size * aspect)

        output_name = f"{base_name}-{size}.png"

        cairosvg.svg2png(
            url=svg_file,
            write_to=output_name,
            output_width=width,
            output_height=height
        )

        print(f"Exported {output_name} ({width}x{height})")