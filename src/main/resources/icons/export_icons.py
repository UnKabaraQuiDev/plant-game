#!/usr/bin/python

import glob
import os
import cairosvg

# Define sizes to export
sizes = [16, 24, 32, 48, 64, 128, 256]

# Find all SVG files starting with "_"
svg_files = glob.glob("__*.svg")

for svg_file in svg_files:
    base_name = os.path.splitext(svg_file)[0].lstrip("_")  # remove "_" and extension

    for size in sizes:
        output_name = f"{base_name}-{size}.png"
        cairosvg.svg2png(
            url=svg_file,
            write_to=output_name,
            output_width=size,
            output_height=size
        )
        print(f"Exported {output_name}")
