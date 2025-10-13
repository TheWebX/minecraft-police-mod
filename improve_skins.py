#!/usr/bin/env python3

from PIL import Image, ImageDraw, ImageFilter
import numpy as np

def create_improved_police_skin():
    # Create a 64x64 texture (Minecraft skin size)
    size = 64
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Define colors
    SKIN_TONE = (255, 220, 177)  # Light skin tone
    DARK_SKIN = (198, 159, 115)  # Darker skin for shadows
    UNIFORM_BLUE = (0, 50, 150)  # Police blue
    DARK_BLUE = (0, 30, 100)     # Darker blue for details
    GOLD = (255, 215, 0)         # Gold for badge and buttons
    SILVER = (192, 192, 192)     # Silver for belt and holster
    BLACK = (0, 0, 0)            # Black for belt, shoes, etc.
    WHITE = (255, 255, 255)      # White for shirt
    GRAY = (128, 128, 128)       # Gray for pants

    # Helper function to draw a rectangle
    def draw_rect(x1, y1, x2, y2, color, outline=None):
        if outline:
            draw.rectangle([x1, y1, x2, y2], fill=color, outline=outline, width=1)
        else:
            draw.rectangle([x1, y1, x2, y2], fill=color)

    # Helper function to draw a circle
    def draw_circle(x, y, radius, color):
        draw.ellipse([x-radius, y-radius, x+radius, y+radius], fill=color)

    # Head (8x8 at top center)
    head_x, head_y = 20, 0
    draw_rect(head_x, head_y, head_x+8, head_y+8, SKIN_TONE)
    # Face details
    draw_rect(head_x+2, head_y+2, head_x+3, head_y+3, BLACK)  # Left eye
    draw_rect(head_x+5, head_y+2, head_x+6, head_y+3, BLACK)  # Right eye
    draw_rect(head_x+3, head_y+4, head_x+5, head_y+5, BLACK)  # Nose
    draw_rect(head_x+2, head_y+6, head_x+6, head_y+7, BLACK)  # Mouth
    # Police cap
    draw_rect(head_x-1, head_y-1, head_x+9, head_y+2, UNIFORM_BLUE)
    draw_rect(head_x, head_y, head_x+8, head_y+1, DARK_BLUE)  # Cap band
    # Police badge on cap
    draw_circle(head_x+4, head_y+1, 1, GOLD)

    # Body (8x12 below head)
    body_x, body_y = 20, 8
    # Shirt
    draw_rect(body_x, body_y, body_x+8, body_y+8, WHITE)
    # Police uniform jacket
    draw_rect(body_x, body_y+2, body_x+8, body_y+8, UNIFORM_BLUE)
    # Buttons
    for i in range(3):
        draw_circle(body_x+4, body_y+4+i*2, 1, GOLD)
    # Police badge on chest
    draw_rect(body_x+3, body_y+3, body_x+5, body_y+5, GOLD)
    draw_rect(body_x+4, body_y+4, body_x+4, body_y+4, BLACK)  # Badge center
    # Belt
    draw_rect(body_x, body_y+6, body_x+8, body_y+7, BLACK)
    # Holster
    draw_rect(body_x+1, body_y+5, body_x+3, body_y+6, SILVER)

    # Arms (4x12 on sides)
    # Left arm
    arm_left_x, arm_left_y = 12, 8
    draw_rect(arm_left_x, arm_left_y, arm_left_x+4, arm_left_y+12, SKIN_TONE)
    # Left arm uniform
    draw_rect(arm_left_x, arm_left_y+2, arm_left_x+4, arm_left_y+8, UNIFORM_BLUE)
    # Right arm
    arm_right_x, arm_right_y = 32, 8
    draw_rect(arm_right_x, arm_right_y, arm_right_x+4, arm_right_y+12, SKIN_TONE)
    # Right arm uniform
    draw_rect(arm_right_x, arm_right_y+2, arm_right_x+4, arm_right_y+8, UNIFORM_BLUE)

    # Legs (4x12 each)
    # Left leg
    leg_left_x, leg_left_y = 16, 20
    draw_rect(leg_left_x, leg_left_y, leg_left_x+4, leg_left_y+12, GRAY)
    # Right leg
    leg_right_x, leg_right_y = 24, 20
    draw_rect(leg_right_x, leg_right_y, leg_right_x+4, leg_right_y+12, GRAY)

    # Feet (4x4 each)
    # Left foot
    foot_left_x, foot_left_y = 16, 32
    draw_rect(foot_left_x, foot_left_y, foot_left_x+4, foot_left_y+4, BLACK)
    # Right foot
    foot_right_x, foot_right_y = 24, 32
    draw_rect(foot_right_x, foot_right_y, foot_right_x+4, foot_right_y+4, BLACK)

    # Add some shading and details
    # Head shading
    for i in range(8):
        for j in range(8):
            if (i + j) % 3 == 0 and i > 2 and j > 2:
                img.putpixel((head_x + i, head_y + j), DARK_SKIN)

    # Uniform details
    # Jacket lapels
    draw.line([body_x+2, body_y+2, body_x+2, body_y+6], DARK_BLUE, 1)
    draw.line([body_x+6, body_y+2, body_x+6, body_y+6], DARK_BLUE, 1)

    # Pants creases
    draw.line([leg_left_x+2, leg_left_y+2, leg_left_x+2, leg_left_y+10], (100, 100, 100), 1)
    draw.line([leg_right_x+2, leg_right_y+2, leg_right_x+2, leg_right_y+10], (100, 100, 100), 1)

    # Add some texture to the uniform
    for i in range(8):
        for j in range(6):
            if (i + j) % 4 == 0:
                img.putpixel((body_x + i, body_y + 2 + j), DARK_BLUE)

    # Save the image
    img.save('/workspace/src/main/resources/assets/policemod/textures/entity/police_mob.png')
    print("Improved police skin generated successfully!")

def create_improved_soldier_skin():
    # Create a 64x64 texture (Minecraft skin size)
    size = 64
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    # Define colors
    SKIN_TONE = (255, 220, 177)  # Light skin tone
    DARK_SKIN = (198, 159, 115)  # Darker skin for shadows
    MILITARY_GREEN = (34, 139, 34)  # Military green
    DARK_GREEN = (0, 100, 0)        # Darker green for details
    CAMO_BROWN = (139, 69, 19)      # Camouflage brown
    CAMO_TAN = (210, 180, 140)      # Camouflage tan
    GOLD = (255, 215, 0)            # Gold for insignia
    SILVER = (192, 192, 192)        # Silver for equipment
    BLACK = (0, 0, 0)               # Black for boots, belt, etc.
    WHITE = (255, 255, 255)         # White for details
    GRAY = (128, 128, 128)          # Gray for equipment

    # Helper function to draw a rectangle
    def draw_rect(x1, y1, x2, y2, color, outline=None):
        if outline:
            draw.rectangle([x1, y1, x2, y2], fill=color, outline=outline, width=1)
        else:
            draw.rectangle([x1, y1, x2, y2], fill=color)

    # Helper function to draw a circle
    def draw_circle(x, y, radius, color):
        draw.ellipse([x-radius, y-radius, x+radius, y+radius], fill=color)

    # Helper function to draw a line
    def draw_line(coords, color, width=1):
        draw.line(coords, fill=color, width=width)

    # Head (8x8 at top center)
    head_x, head_y = 20, 0
    draw_rect(head_x, head_y, head_x+8, head_y+8, SKIN_TONE)
    # Face details
    draw_rect(head_x+2, head_y+2, head_x+3, head_y+3, BLACK)  # Left eye
    draw_rect(head_x+5, head_y+2, head_x+6, head_y+3, BLACK)  # Right eye
    draw_rect(head_x+3, head_y+4, head_x+5, head_y+5, BLACK)  # Nose
    draw_rect(head_x+2, head_y+6, head_x+6, head_y+7, BLACK)  # Mouth
    # Military helmet
    draw_rect(head_x-1, head_y-1, head_x+9, head_y+2, MILITARY_GREEN)
    draw_rect(head_x, head_y, head_x+8, head_y+1, DARK_GREEN)  # Helmet band
    # Helmet strap
    draw_line([head_x+1, head_y+1, head_x+7, head_y+1], BLACK, 1)
    # Military insignia on helmet
    draw_rect(head_x+3, head_y, head_x+5, head_y+1, GOLD)

    # Body (8x12 below head)
    body_x, body_y = 20, 8
    # Military uniform
    draw_rect(body_x, body_y, body_x+8, body_y+8, MILITARY_GREEN)
    # Camouflage pattern
    for i in range(8):
        for j in range(8):
            if (i + j) % 3 == 0:
                img.putpixel((body_x + i, body_y + j), CAMO_BROWN)
            elif (i + j) % 5 == 0:
                img.putpixel((body_x + i, body_y + j), CAMO_TAN)
    # Military patches
    draw_rect(body_x+1, body_y+2, body_x+3, body_y+4, GOLD)  # Left patch
    draw_rect(body_x+5, body_y+2, body_x+7, body_y+4, GOLD)  # Right patch
    # Rank insignia
    draw_line([body_x+2, body_y+3, body_x+2, body_y+3], BLACK, 1)
    draw_line([body_x+6, body_y+3, body_x+6, body_y+3], BLACK, 1)
    # Military belt
    draw_rect(body_x, body_y+6, body_x+8, body_y+7, BLACK)
    # Equipment pouches
    draw_rect(body_x+1, body_y+5, body_x+2, body_y+6, GRAY)
    draw_rect(body_x+6, body_y+5, body_x+7, body_y+6, GRAY)

    # Arms (4x12 on sides)
    # Left arm
    arm_left_x, arm_left_y = 12, 8
    draw_rect(arm_left_x, arm_left_y, arm_left_x+4, arm_left_y+12, SKIN_TONE)
    # Left arm uniform
    draw_rect(arm_left_x, arm_left_y+2, arm_left_x+4, arm_left_y+8, MILITARY_GREEN)
    # Camouflage on arm
    for i in range(4):
        for j in range(6):
            if (i + j) % 4 == 0:
                img.putpixel((arm_left_x + i, arm_left_y + 2 + j), CAMO_BROWN)
    # Right arm
    arm_right_x, arm_right_y = 32, 8
    draw_rect(arm_right_x, arm_right_y, arm_right_x+4, arm_right_y+12, SKIN_TONE)
    # Right arm uniform
    draw_rect(arm_right_x, arm_right_y+2, arm_right_x+4, arm_right_y+8, MILITARY_GREEN)
    # Camouflage on arm
    for i in range(4):
        for j in range(6):
            if (i + j) % 4 == 0:
                img.putpixel((arm_right_x + i, arm_right_y + 2 + j), CAMO_BROWN)

    # Legs (4x12 each)
    # Left leg
    leg_left_x, leg_left_y = 16, 20
    draw_rect(leg_left_x, leg_left_y, leg_left_x+4, leg_left_y+12, MILITARY_GREEN)
    # Camouflage on leg
    for i in range(4):
        for j in range(12):
            if (i + j) % 3 == 0:
                img.putpixel((leg_left_x + i, leg_left_y + j), CAMO_BROWN)
            elif (i + j) % 5 == 0:
                img.putpixel((leg_left_x + i, leg_left_y + j), CAMO_TAN)
    # Right leg
    leg_right_x, leg_right_y = 24, 20
    draw_rect(leg_right_x, leg_right_y, leg_right_x+4, leg_right_y+12, MILITARY_GREEN)
    # Camouflage on leg
    for i in range(4):
        for j in range(12):
            if (i + j) % 3 == 0:
                img.putpixel((leg_right_x + i, leg_right_y + j), CAMO_BROWN)
            elif (i + j) % 5 == 0:
                img.putpixel((leg_right_x + i, leg_right_y + j), CAMO_TAN)

    # Feet (4x4 each)
    # Left foot
    foot_left_x, foot_left_y = 16, 32
    draw_rect(foot_left_x, foot_left_y, foot_left_x+4, foot_left_y+4, BLACK)
    # Right foot
    foot_right_x, foot_right_y = 24, 32
    draw_rect(foot_right_x, foot_right_y, foot_right_x+4, foot_right_y+4, BLACK)

    # Add some shading and details
    # Head shading
    for i in range(8):
        for j in range(8):
            if (i + j) % 3 == 0 and i > 2 and j > 2:
                img.putpixel((head_x + i, head_y + j), DARK_SKIN)

    # Uniform details
    # Jacket details
    draw_line([body_x+2, body_y+2, body_x+2, body_y+6], DARK_GREEN, 1)
    draw_line([body_x+6, body_y+2, body_x+6, body_y+6], DARK_GREEN, 1)

    # Pants creases
    draw_line([leg_left_x+2, leg_left_y+2, leg_left_x+2, leg_left_y+10], (0, 80, 0), 1)
    draw_line([leg_right_x+2, leg_right_y+2, leg_right_x+2, leg_right_y+10], (0, 80, 0), 1)

    # Add tactical gear details
    # Knee pads
    draw_rect(leg_left_x+1, leg_left_y+8, leg_left_x+3, leg_left_y+10, GRAY)
    draw_rect(leg_right_x+1, leg_right_y+8, leg_right_x+3, leg_right_y+10, GRAY)

    # Arm patches
    draw_rect(arm_left_x+1, arm_left_y+3, arm_left_x+3, arm_left_y+5, GOLD)
    draw_rect(arm_right_x+1, arm_right_y+3, arm_right_x+3, arm_right_y+5, GOLD)

    # Add some texture to the uniform
    for i in range(8):
        for j in range(6):
            if (i + j) % 4 == 0:
                img.putpixel((body_x + i, body_y + 2 + j), DARK_GREEN)

    # Save the image
    img.save('/workspace/src/main/resources/assets/policemod/textures/entity/soldier_mob.png')
    print("Improved soldier skin generated successfully!")

if __name__ == "__main__":
    create_improved_police_skin()
    create_improved_soldier_skin()
    print("All improved skins generated successfully!")