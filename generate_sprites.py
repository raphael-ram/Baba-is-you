#!/usr/bin/env python3
"""
Generate all sprites for Baba Is You game.
24x24 RGBA PNGs with transparent backgrounds, 3 animation frames each.
"""

import os
import math
import random
import shutil
from PIL import Image, ImageDraw

# -- Paths -------------------------------------------------------------------
BASE = "/home/cybernox/Baba-is-you/src/main/resources/images"
TARGET = "/home/cybernox/Baba-is-you/target/classes/images"
SIZE = 24

random.seed(42)

# -- Tiny bitmap font (5x7 uppercase) ----------------------------------------
FONT = {
    'A': [
        " XXX ",
        "X   X",
        "X   X",
        "XXXXX",
        "X   X",
        "X   X",
        "X   X",
    ],
    'B': [
        "XXXX ",
        "X   X",
        "X   X",
        "XXXX ",
        "X   X",
        "X   X",
        "XXXX ",
    ],
    'C': [
        " XXXX",
        "X    ",
        "X    ",
        "X    ",
        "X    ",
        "X    ",
        " XXXX",
    ],
    'D': [
        "XXXX ",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        "XXXX ",
    ],
    'E': [
        "XXXXX",
        "X    ",
        "X    ",
        "XXXX ",
        "X    ",
        "X    ",
        "XXXXX",
    ],
    'F': [
        "XXXXX",
        "X    ",
        "X    ",
        "XXXX ",
        "X    ",
        "X    ",
        "X    ",
    ],
    'G': [
        " XXXX",
        "X    ",
        "X    ",
        "X  XX",
        "X   X",
        "X   X",
        " XXXX",
    ],
    'H': [
        "X   X",
        "X   X",
        "X   X",
        "XXXXX",
        "X   X",
        "X   X",
        "X   X",
    ],
    'I': [
        "XXXXX",
        "  X  ",
        "  X  ",
        "  X  ",
        "  X  ",
        "  X  ",
        "XXXXX",
    ],
    'K': [
        "X   X",
        "X  X ",
        "X X  ",
        "XX   ",
        "X X  ",
        "X  X ",
        "X   X",
    ],
    'L': [
        "X    ",
        "X    ",
        "X    ",
        "X    ",
        "X    ",
        "X    ",
        "XXXXX",
    ],
    'M': [
        "X   X",
        "XX XX",
        "X X X",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
    ],
    'N': [
        "X   X",
        "XX  X",
        "X X X",
        "X  XX",
        "X   X",
        "X   X",
        "X   X",
    ],
    'O': [
        " XXX ",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        " XXX ",
    ],
    'P': [
        "XXXX ",
        "X   X",
        "X   X",
        "XXXX ",
        "X    ",
        "X    ",
        "X    ",
    ],
    'R': [
        "XXXX ",
        "X   X",
        "X   X",
        "XXXX ",
        "X  X ",
        "X   X",
        "X   X",
    ],
    'S': [
        " XXXX",
        "X    ",
        "X    ",
        " XXX ",
        "    X",
        "    X",
        "XXXX ",
    ],
    'T': [
        "XXXXX",
        "  X  ",
        "  X  ",
        "  X  ",
        "  X  ",
        "  X  ",
        "  X  ",
    ],
    'U': [
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        " XXX ",
    ],
    'V': [
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        " X X ",
        " X X ",
        "  X  ",
    ],
    'W': [
        "X   X",
        "X   X",
        "X   X",
        "X   X",
        "X X X",
        "XX XX",
        "X   X",
    ],
    'X': [
        "X   X",
        "X   X",
        " X X ",
        "  X  ",
        " X X ",
        "X   X",
        "X   X",
    ],
    'Y': [
        "X   X",
        "X   X",
        " X X ",
        "  X  ",
        "  X  ",
        "  X  ",
        "  X  ",
    ],
    'J': [
        "XXXXX",
        "    X",
        "    X",
        "    X",
        "    X",
        "X   X",
        " XXX ",
    ],
    'Q': [
        " XXX ",
        "X   X",
        "X   X",
        "X   X",
        "X X X",
        "X  X ",
        " XX X",
    ],
    'Z': [
        "XXXXX",
        "    X",
        "   X ",
        "  X  ",
        " X   ",
        "X    ",
        "XXXXX",
    ],
}


def draw_text_sprite(img, text, color, frame=0):
    """Draw centered pixel-art text on a 24x24 image."""
    n = len(text)

    if n <= 2:
        char_w = 5
        spacing = 2
    elif n <= 3:
        char_w = 5
        spacing = 2
    elif n <= 4:
        char_w = 5
        spacing = 1
    elif n == 5:
        char_w = 4
        spacing = 1
    else:  # 6 chars (DEFEAT)
        char_w = 3
        spacing = 1

    total_w = n * char_w + (n - 1) * spacing
    start_x = (SIZE - total_w) // 2
    start_y = (SIZE - 7) // 2

    dy = [0, -1, 1][frame]

    for ci, ch in enumerate(text):
        glyph = FONT.get(ch)
        if glyph is None:
            continue
        ox = start_x + ci * (char_w + spacing)
        for row_i, row in enumerate(glyph):
            for col_i, pixel in enumerate(row):
                if pixel == 'X':
                    if char_w == 5:
                        px = ox + col_i
                    elif char_w == 4:
                        px = ox + int(col_i * 4 / 5)
                    else:  # char_w == 3
                        px = ox + int(col_i * 3 / 5)
                    py = start_y + row_i + dy
                    if 0 <= px < SIZE and 0 <= py < SIZE:
                        img.putpixel((px, py), color)

    # Subtle glow on frame 1
    if frame == 1:
        glow = tuple(min(255, c + 40) for c in color[:3]) + (60,)
        pixels_to_glow = []
        for x in range(SIZE):
            for y in range(SIZE):
                if img.getpixel((x, y))[3] > 0:
                    for dx2, dy2 in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                        nx, ny = x + dx2, y + dy2
                        if 0 <= nx < SIZE and 0 <= ny < SIZE and img.getpixel((nx, ny))[3] == 0:
                            pixels_to_glow.append((nx, ny))
        for px, py in pixels_to_glow:
            img.putpixel((px, py), glow)


def make_text_sprite(folder, prefix, text, color_rgb, frame):
    """Create and save a text sprite."""
    img = Image.new('RGBA', (SIZE, SIZE), (0, 0, 0, 0))
    color = (*color_rgb, 255)
    draw_text_sprite(img, text, color, frame)
    path = os.path.join(folder, f"{prefix}{frame}.png")
    img.save(path)
    return path


# -- Material sprite drawing functions ----------------------------------------

def draw_baba(img, frame):
    """White rabbit-like character with dark eyes."""
    W = (255, 255, 255, 255)
    E = (50, 0, 0, 255)
    P = (255, 200, 200, 255)

    dy = [0, -1, 1][frame]

    # Body (center blob)
    for y in range(13, 21):
        for x in range(8, 17):
            yy = y + dy
            cx, cy_c = 12, 17
            if (x - cx) ** 2 / 16 + (y - cy_c) ** 2 / 16 <= 1:
                if 0 <= yy < SIZE:
                    img.putpixel((x, yy), W)

    # Head
    for y in range(7, 15):
        for x in range(7, 18):
            yy = y + dy
            cx, cy_c = 12, 11
            if (x - cx) ** 2 / 25 + (y - cy_c) ** 2 / 16 <= 1:
                if 0 <= yy < SIZE:
                    img.putpixel((x, yy), W)

    # Ears
    for y in range(2, 9):
        for x in range(8, 11):
            yy = y + dy
            if 0 <= yy < SIZE:
                img.putpixel((x, yy), W)
    for y in range(2, 9):
        for x in range(14, 17):
            yy = y + dy
            if 0 <= yy < SIZE:
                img.putpixel((x, yy), W)

    # Eyes
    for ex in [10, 14]:
        for ey_off in [10, 11]:
            yy = ey_off + dy
            if 0 <= yy < SIZE:
                img.putpixel((ex, yy), E)

    # Nose
    yy = 12 + dy
    if 0 <= yy < SIZE:
        img.putpixel((12, yy), P)

    # Feet
    for fx in [9, 10, 14, 15]:
        yy = 21 + dy
        if 0 <= yy < SIZE:
            img.putpixel((fx, yy), W)


def draw_rock(img, frame):
    """Brown rounded stone."""
    base = (180, 130, 70, 255)
    dark = (140, 100, 50, 255)
    light = (210, 170, 100, 255)

    cx, cy = 12, 13
    rx, ry = 8, 7

    for y in range(SIZE):
        for x in range(SIZE):
            dx_f = (x - cx) / rx
            dy_f = (y - cy) / ry
            dist = dx_f * dx_f + dy_f * dy_f
            if dist <= 1.0:
                if dx_f + dy_f < -0.5:
                    c = light
                elif dx_f + dy_f > 0.7:
                    c = dark
                else:
                    c = base
                r, g, b, a = c
                r = max(0, min(255, r + frame * 5 - 5))
                img.putpixel((x, y), (r, g, b, a))

    for (sx, sy) in [(10, 11), (14, 14), (11, 15)]:
        if img.getpixel((sx, sy))[3] > 0:
            img.putpixel((sx, sy), dark)


def draw_wall(img, frame):
    """Dark gray brick wall filling the cell."""
    brick = (100, 100, 100, 255)
    mortar = (80, 80, 80, 255)
    highlight = (120, 120, 120, 255)

    for y in range(SIZE):
        for x in range(SIZE):
            img.putpixel((x, y), mortar)

    brick_h = 5
    brick_w = 10
    offset = frame * 2

    for row in range(SIZE // brick_h + 1):
        y0 = row * (brick_h + 1)
        x_off = (brick_w // 2 + offset) if row % 2 == 1 else offset
        for col in range(-1, SIZE // brick_w + 2):
            x0 = col * (brick_w + 1) + x_off
            for by in range(brick_h):
                for bx in range(brick_w):
                    px = x0 + bx
                    py = y0 + by
                    if 0 <= px < SIZE and 0 <= py < SIZE:
                        if by == 0 or bx == 0:
                            img.putpixel((px, py), highlight)
                        else:
                            img.putpixel((px, py), brick)


def draw_flag(img, frame):
    """Yellow flag on a brown pole."""
    pole = (139, 90, 43, 255)
    flag_c = (255, 220, 50, 255)
    flag_d = (230, 190, 30, 255)
    dy = [0, -1, 0][frame]

    for y in range(5, 22):
        img.putpixel((8, y), pole)
        img.putpixel((9, y), pole)

    for y in range(5, 13):
        yy = y + dy
        width = 11 - abs(y - 9) + (frame % 2)
        for x in range(10, 10 + width):
            if 0 <= x < SIZE and 0 <= yy < SIZE:
                c = flag_c if (x + y) % 3 != 0 else flag_d
                img.putpixel((x, yy), c)

    for dx2 in [-1, 0, 1]:
        for dy2 in [-1, 0, 1]:
            px, py = 8 + dx2, 4 + dy2
            if 0 <= px < SIZE and 0 <= py < SIZE and abs(dx2) + abs(dy2) <= 1:
                img.putpixel((px, py), (200, 170, 50, 255))


def draw_lava(img, frame):
    """Orange-red flowing lava filling the cell."""
    base = (255, 100, 20, 255)
    hot = (255, 60, 10, 255)
    bright = (255, 200, 50, 255)

    random.seed(42 + frame)
    for y in range(SIZE):
        for x in range(SIZE):
            wave = math.sin((x + frame * 3) * 0.5) * 2 + math.sin((y + frame * 2) * 0.7) * 2
            if wave > 1.5:
                img.putpixel((x, y), bright)
            elif wave > 0:
                img.putpixel((x, y), hot)
            else:
                r = base[0] + random.randint(-10, 10)
                g = base[1] + random.randint(-10, 10)
                img.putpixel((x, y), (max(0, min(255, r)), max(0, min(255, g)), base[2], 255))


def draw_skull(img, frame):
    """White skull with dark eye sockets."""
    skull_c = (220, 220, 220, 255)
    eye_c = (50, 0, 0, 255)
    dark = (180, 180, 180, 255)

    dy = [0, 0, 1][frame]
    cx, cy = 12, 10

    for y in range(4, 16):
        for x in range(5, 20):
            yy = y + dy
            dx_f = (x - cx) / 7.0
            dy_f = (y - cy) / 6.0
            if dx_f * dx_f + dy_f * dy_f <= 1.0 and 0 <= yy < SIZE:
                img.putpixel((x, yy), skull_c)

    for y in range(14, 19):
        for x in range(8, 17):
            yy = y + dy
            if 0 <= yy < SIZE:
                img.putpixel((x, yy), skull_c)

    for ey in range(8, 12):
        for ex in range(8, 11):
            yy = ey + dy
            if 0 <= yy < SIZE:
                img.putpixel((ex, yy), eye_c)
    for ey in range(8, 12):
        for ex in range(14, 17):
            yy = ey + dy
            if 0 <= yy < SIZE:
                img.putpixel((ex, yy), eye_c)

    for ny in [12, 13]:
        yy = ny + dy
        if 0 <= yy < SIZE:
            img.putpixel((12, yy), eye_c)

    yy = 16 + dy
    if 0 <= yy < SIZE:
        for tx in [9, 11, 13, 15]:
            img.putpixel((tx, yy), dark)


def draw_water(img, frame):
    """Blue wavy water filling the cell."""
    base = (50, 120, 220, 255)
    hi = (70, 150, 255, 255)
    dark = (30, 90, 180, 255)

    for y in range(SIZE):
        for x in range(SIZE):
            wave = math.sin((x + frame * 4) * 0.6) * 2 + math.cos((y + frame * 2) * 0.8) * 1.5
            if wave > 1.5:
                img.putpixel((x, y), hi)
            elif wave < -1:
                img.putpixel((x, y), dark)
            else:
                img.putpixel((x, y), base)


def draw_tile(img, frame):
    """Gray/tan flat tile with subtle border."""
    fill = (160, 150, 130, 255)
    border = (140, 130, 110, 255)
    light = (175, 165, 145, 255)

    for y in range(SIZE):
        for x in range(SIZE):
            if x < 1 or x >= SIZE - 1 or y < 1 or y >= SIZE - 1:
                img.putpixel((x, y), border)
            elif x < 2 or y < 2:
                img.putpixel((x, y), light)
            else:
                r = fill[0] + (frame - 1) * 3
                g = fill[1] + (frame - 1) * 3
                b = fill[2] + (frame - 1) * 3
                img.putpixel((x, y), (r, g, b, 255))


def draw_fan(img, frame):
    """Cyan spinning fan blades."""
    hub = (100, 240, 255, 255)
    blade = (80, 220, 240, 255)
    blade_d = (60, 200, 220, 255)

    cx, cy = 12, 12
    angle_offset = frame * (2 * math.pi / 3)

    for b in range(4):
        angle = angle_offset + b * math.pi / 2
        for t in range(2, 10):
            for w in range(-2, 3):
                x = int(cx + t * math.cos(angle) + w * math.cos(angle + math.pi / 2) * 0.3)
                y = int(cy + t * math.sin(angle) + w * math.sin(angle + math.pi / 2) * 0.3)
                if 0 <= x < SIZE and 0 <= y < SIZE:
                    c = blade if t < 7 else blade_d
                    img.putpixel((x, y), c)

    for dy2 in range(-2, 3):
        for dx2 in range(-2, 3):
            if dx2 * dx2 + dy2 * dy2 <= 4:
                px, py = cx + dx2, cy + dy2
                if 0 <= px < SIZE and 0 <= py < SIZE:
                    img.putpixel((px, py), hub)


def draw_box(img, frame):
    """Brown wooden crate with X pattern."""
    wood = (160, 100, 40, 255)
    dark = (130, 80, 30, 255)
    light = (190, 130, 60, 255)
    border_c = (100, 60, 20, 255)

    for y in range(5, 21):
        for x in range(5, 21):
            img.putpixel((x, y), wood)

    for i in range(5, 21):
        for b in [5, 20]:
            img.putpixel((i, b), border_c)
            img.putpixel((b, i), border_c)

    for i in range(16):
        x1 = 5 + i
        y1 = 5 + i
        x2 = 20 - i
        if 5 <= x1 <= 20 and 5 <= y1 <= 20:
            img.putpixel((x1, y1), dark)
            if x1 + 1 <= 20:
                img.putpixel((x1 + 1, y1), dark)
        if 5 <= x2 <= 20 and 5 <= y1 <= 20:
            img.putpixel((x2, y1), dark)
            if x2 - 1 >= 5:
                img.putpixel((x2 - 1, y1), dark)

    seed_points = [(8, 8), (15, 10), (10, 17)]
    sp = seed_points[frame]
    for dy2 in range(-1, 2):
        for dx2 in range(-1, 2):
            px, py = sp[0] + dx2, sp[1] + dy2
            if 5 < px < 20 and 5 < py < 20:
                img.putpixel((px, py), light)


# -- Sprite definitions -------------------------------------------------------

MATERIAL_SPRITES = {
    'baba':  {'files': ['baba_{}.png'], 'draw': draw_baba},
    'rock':  {'files': ['ROCK_0-{}.png'], 'draw': draw_rock},
    'wall':  {'files': ['WALL_0-{}.png'], 'draw': draw_wall},
    'flag':  {'files': ['FLAG_0-{}.png'], 'draw': draw_flag},
    'lava':  {'files': ['LAVA_0-{}.png'], 'draw': draw_lava},
    'skull': {'files': ['SKULL_0-{}.png'], 'draw': draw_skull},
    'water': {'files': ['WATER_0-{}.png'], 'draw': draw_water},
    'tile':  {'files': ['TILE_0-{}.png'], 'draw': draw_tile},
    'fan':   {'files': ['FAN_0-{}.png'], 'draw': draw_fan},
    'box':   {'files': ['BOX_0-{}.png'], 'draw': draw_box},
}

TEXT_SPRITES = {
    'baba_txt': {'prefix': 'Text_BABA_0-', 'text': 'BABA', 'color': (255, 200, 220)},
    'rock_txt': {'prefix': 'Text_ROCK_0-', 'text': 'ROCK', 'color': (180, 130, 70)},
    'flag_txt': {'prefix': 'Text_FLAG_0-', 'text': 'FLAG', 'color': (255, 220, 50)},
    'wall_txt': {'prefix': 'Text_WALL_0-', 'text': 'WALL', 'color': (140, 140, 140)},
    'lava_txt': {'prefix': 'Text_LAVA_0-', 'text': 'LAVA', 'color': (255, 120, 30)},
    'skull_txt': {'prefix': 'Text_SKULL_0-', 'text': 'SKULL', 'color': (180, 40, 40)},
    'water_txt': {'prefix': 'Text_WATER_0-', 'text': 'WATER', 'color': (50, 120, 220)},
    'fan_txt': {'prefix': 'Text_FAN_0-', 'text': 'FAN', 'color': (80, 220, 240)},
    'box_txt': {'prefix': 'Text_BOX_0-', 'text': 'BOX', 'color': (160, 100, 40)},
    'tile_txt': {'prefix': 'Text_TILE_0-', 'text': 'TILE', 'color': (160, 150, 130)},
    'you':     {'prefix': 'Text_YOU_0-', 'text': 'YOU', 'color': (255, 255, 255)},
    'push':    {'prefix': 'Text_PUSH_0-', 'text': 'PUSH', 'color': (255, 165, 30)},
    'win':     {'prefix': 'Text_WIN_0-', 'text': 'WIN', 'color': (255, 220, 50)},
    'stop':    {'prefix': 'Text_STOP_0-', 'text': 'STOP', 'color': (220, 50, 50)},
    'melt':    {'prefix': 'Text_MELT_0-', 'text': 'MELT', 'color': (130, 200, 255)},
    'defeat':  {'prefix': 'Text_DEFEAT_0-', 'text': 'DEFEAT', 'color': (160, 30, 30)},
    'sink':    {'prefix': 'Text_SINK_0-', 'text': 'SINK', 'color': (30, 60, 160)},
    'reverse': {'prefix': 'Text_REVERSE_0-', 'text': 'RVRSE', 'color': (160, 80, 200)},
    'pull':    {'prefix': 'Text_PULL_0-', 'text': 'PULL', 'color': (220, 80, 180)},
    'hot':     {'prefix': 'Text_HOT_0-', 'text': 'HOT', 'color': (255, 80, 20)},
}

IS_SPRITE = {
    'is': {
        'prefix': 'ab87768eb8b74076d27fdbc13d3c94d3rbzNe1s8RLxgKBhK-',
        'text': 'IS',
        'color': (255, 255, 255),
    }
}


def generate_all():
    generated = []

    # Generate material sprites
    for folder_name, spec in MATERIAL_SPRITES.items():
        folder = os.path.join(BASE, folder_name)
        os.makedirs(folder, exist_ok=True)

        file_template = spec['files'][0]
        draw_fn = spec['draw']

        for frame in range(3):
            img = Image.new('RGBA', (SIZE, SIZE), (0, 0, 0, 0))
            draw_fn(img, frame)
            filename = file_template.format(frame)
            path = os.path.join(folder, filename)
            img.save(path)
            generated.append(path)

    # Generate text sprites
    for folder_name, spec in TEXT_SPRITES.items():
        folder = os.path.join(BASE, folder_name)
        os.makedirs(folder, exist_ok=True)

        for frame in range(3):
            path = make_text_sprite(
                folder, spec['prefix'], spec['text'], spec['color'], frame
            )
            generated.append(path)

    # Generate IS sprite
    for folder_name, spec in IS_SPRITE.items():
        folder = os.path.join(BASE, folder_name)
        os.makedirs(folder, exist_ok=True)

        for frame in range(3):
            path = make_text_sprite(
                folder, spec['prefix'], spec['text'], spec['color'], frame
            )
            generated.append(path)

    # Copy to target/classes/images
    if os.path.exists(TARGET):
        shutil.rmtree(TARGET)
    shutil.copytree(BASE, TARGET)

    return generated


if __name__ == '__main__':
    files = generate_all()
    print(f"Generated {len(files)} sprite files in source.")
    print()

    # Summary by folder
    folders = {}
    for f in files:
        folder = os.path.basename(os.path.dirname(f))
        folders.setdefault(folder, []).append(os.path.basename(f))

    for folder in sorted(folders):
        fnames = sorted(folders[folder])
        sizes = []
        for fn in fnames:
            path = os.path.join(BASE, folder, fn)
            sz = os.path.getsize(path)
            sizes.append(sz)
            img = Image.open(path)
            assert img.size == (SIZE, SIZE), f"Bad size: {path} is {img.size}"
            assert img.mode == 'RGBA', f"Bad mode: {path} is {img.mode}"
        print(f"  {folder}/: {len(fnames)} files, sizes {min(sizes)}-{max(sizes)} bytes")
        for fn in fnames:
            print(f"    {fn}")

    # Verify target copy
    target_count = 0
    for root, dirs, fnames in os.walk(TARGET):
        for fn in fnames:
            if fn.endswith('.png'):
                target_count += 1
    print(f"\nTarget copy: {target_count} PNG files in {TARGET}")
    print("Done!")
