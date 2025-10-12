#!/usr/bin/env python3
"""
Create proper Minecraft skin textures that fit the 3D model correctly.
Based on the standard Minecraft skin format with proper UV mapping.
"""

from PIL import Image, ImageDraw
import os

def create_police_skin():
    """Create a proper police skin texture following Minecraft skin format."""
    # Create 64x64 RGBA image
    img = Image.new('RGBA', (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Define colors
    skin_color = (255, 220, 177)  # Light skin tone
    uniform_blue = (0, 50, 150)   # Dark blue uniform
    badge_gold = (255, 215, 0)    # Gold badge
    belt_black = (30, 30, 30)     # Black belt
    shoes_black = (20, 20, 20)    # Black shoes
    
    # Head (8x8 at top center)
    head_x, head_y = 8, 0
    draw.rectangle([head_x, head_y, head_x+7, head_y+7], fill=skin_color)
    
    # Head details
    # Eyes
    draw.rectangle([head_x+2, head_y+2, head_x+2, head_y+2], fill=(0, 0, 0))  # Left eye
    draw.rectangle([head_x+5, head_y+2, head_x+5, head_y+2], fill=(0, 0, 0))  # Right eye
    # Nose
    draw.rectangle([head_x+3, head_y+3, head_x+4, head_y+3], fill=(200, 180, 150))
    # Mouth
    draw.rectangle([head_x+2, head_y+5, head_x+5, head_y+5], fill=(150, 100, 100))
    
    # Hat (police cap)
    draw.rectangle([head_x, head_y, head_x+7, head_y+1], fill=uniform_blue)
    draw.rectangle([head_x+1, head_y-1, head_x+6, head_y], fill=uniform_blue)
    
    # Badge on hat
    draw.rectangle([head_x+3, head_y, head_x+4, head_y], fill=badge_gold)
    
    # Body (8x12 below head)
    body_x, body_y = 8, 8
    draw.rectangle([body_x, body_y, body_x+7, body_y+11], fill=uniform_blue)
    
    # Police badge on chest
    draw.rectangle([body_x+2, body_y+2, body_x+5, body_y+4], fill=badge_gold)
    draw.rectangle([body_x+3, body_y+3, body_x+4, body_y+3], fill=uniform_blue)
    
    # Belt
    draw.rectangle([body_x, body_y+8, body_x+7, body_y+9], fill=belt_black)
    
    # Arms (left and right of body)
    # Left arm
    arm_left_x, arm_left_y = 0, 8
    draw.rectangle([arm_left_x, arm_left_y, arm_left_x+3, arm_left_y+11], fill=uniform_blue)
    
    # Right arm
    arm_right_x, arm_right_y = 16, 8
    draw.rectangle([arm_right_x, arm_right_y, arm_right_x+3, arm_right_y+11], fill=uniform_blue)
    
    # Legs (below body)
    leg_left_x, leg_left_y = 4, 20
    draw.rectangle([leg_left_x, leg_left_y, leg_left_x+3, leg_left_y+11], fill=uniform_blue)
    
    leg_right_x, leg_right_y = 8, 20
    draw.rectangle([leg_right_x, leg_right_y, leg_right_x+3, leg_right_y+11], fill=uniform_blue)
    
    # Shoes
    draw.rectangle([leg_left_x, leg_left_y+11, leg_left_x+3, leg_left_y+11], fill=shoes_black)
    draw.rectangle([leg_right_x, leg_right_y+11, leg_right_x+3, leg_right_y+11], fill=shoes_black)
    
    # Back of head (for 3D model)
    back_head_x, back_head_y = 24, 0
    draw.rectangle([back_head_x, back_head_y, back_head_x+7, back_head_y+7], fill=skin_color)
    
    # Back of body
    back_body_x, back_body_y = 24, 8
    draw.rectangle([back_body_x, back_body_y, back_body_x+7, back_body_y+11], fill=uniform_blue)
    
    # Back of arms
    back_arm_left_x, back_arm_left_y = 20, 8
    draw.rectangle([back_arm_left_x, back_arm_left_y, back_arm_left_x+3, back_arm_left_y+11], fill=uniform_blue)
    
    back_arm_right_x, back_arm_right_y = 32, 8
    draw.rectangle([back_arm_right_x, back_arm_right_y, back_arm_right_x+3, back_arm_right_y+11], fill=uniform_blue)
    
    # Back of legs
    back_leg_left_x, back_leg_left_y = 20, 20
    draw.rectangle([back_leg_left_x, back_leg_left_y, back_leg_left_x+3, back_leg_left_y+11], fill=uniform_blue)
    
    back_leg_right_x, back_leg_right_y = 24, 20
    draw.rectangle([back_leg_right_x, back_leg_right_y, back_leg_right_x+3, back_leg_right_y+11], fill=uniform_blue)
    
    # Save the texture
    os.makedirs('src/main/resources/assets/policemod/textures/entity', exist_ok=True)
    img.save('src/main/resources/assets/policemod/textures/entity/police_mob.png')
    print("Police skin created with proper 3D mapping")

def create_soldier_skin():
    """Create a proper soldier skin texture following Minecraft skin format."""
    # Create 64x64 RGBA image
    img = Image.new('RGBA', (64, 64), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    # Define colors
    skin_color = (255, 220, 177)  # Light skin tone
    camo_green = (34, 139, 34)    # Forest green
    camo_brown = (139, 69, 19)    # Saddle brown
    camo_tan = (210, 180, 140)    # Tan
    belt_black = (30, 30, 30)     # Black belt
    shoes_black = (20, 20, 20)    # Black shoes
    
    # Head (8x8 at top center)
    head_x, head_y = 8, 0
    draw.rectangle([head_x, head_y, head_x+7, head_y+7], fill=skin_color)
    
    # Head details
    # Eyes
    draw.rectangle([head_x+2, head_y+2, head_x+2, head_y+2], fill=(0, 0, 0))  # Left eye
    draw.rectangle([head_x+5, head_y+2, head_x+5, head_y+2], fill=(0, 0, 0))  # Right eye
    # Nose
    draw.rectangle([head_x+3, head_y+3, head_x+4, head_y+3], fill=(200, 180, 150))
    # Mouth
    draw.rectangle([head_x+2, head_y+5, head_x+5, head_y+5], fill=(150, 100, 100))
    
    # Military helmet
    draw.rectangle([head_x, head_y, head_x+7, head_y+1], fill=camo_green)
    draw.rectangle([head_x+1, head_y-1, head_x+6, head_y], fill=camo_green)
    
    # Camouflage pattern on helmet
    draw.rectangle([head_x+2, head_y, head_x+3, head_y], fill=camo_brown)
    draw.rectangle([head_x+5, head_y, head_x+6, head_y], fill=camo_tan)
    
    # Body (8x12 below head) - camouflage pattern
    body_x, body_y = 8, 8
    draw.rectangle([body_x, body_y, body_x+7, body_y+11], fill=camo_green)
    
    # Camouflage patches
    draw.rectangle([body_x+1, body_y+2, body_x+3, body_y+4], fill=camo_brown)
    draw.rectangle([body_x+5, body_y+3, body_x+6, body_y+5], fill=camo_tan)
    draw.rectangle([body_x+2, body_y+6, body_x+4, body_y+8], fill=camo_tan)
    draw.rectangle([body_x+5, body_y+7, body_x+6, body_y+9], fill=camo_brown)
    
    # Belt
    draw.rectangle([body_x, body_y+8, body_x+7, body_y+9], fill=belt_black)
    
    # Arms (left and right of body) - camouflage
    # Left arm
    arm_left_x, arm_left_y = 0, 8
    draw.rectangle([arm_left_x, arm_left_y, arm_left_x+3, arm_left_y+11], fill=camo_green)
    draw.rectangle([arm_left_x+1, arm_left_y+2, arm_left_x+2, arm_left_y+4], fill=camo_brown)
    draw.rectangle([arm_left_x+2, arm_left_y+6, arm_left_x+3, arm_left_y+8], fill=camo_tan)
    
    # Right arm
    arm_right_x, arm_right_y = 16, 8
    draw.rectangle([arm_right_x, arm_right_y, arm_right_x+3, arm_right_y+11], fill=camo_green)
    draw.rectangle([arm_right_x+1, arm_right_y+3, arm_right_x+2, arm_right_y+5], fill=camo_tan)
    draw.rectangle([arm_right_x+2, arm_right_y+7, arm_right_x+3, arm_right_y+9], fill=camo_brown)
    
    # Legs (below body) - camouflage
    leg_left_x, leg_left_y = 4, 20
    draw.rectangle([leg_left_x, leg_left_y, leg_left_x+3, leg_left_y+11], fill=camo_green)
    draw.rectangle([leg_left_x+1, leg_left_y+2, leg_left_x+2, leg_left_y+4], fill=camo_brown)
    draw.rectangle([leg_left_x+2, leg_left_y+6, leg_left_x+3, leg_left_y+8], fill=camo_tan)
    
    leg_right_x, leg_right_y = 8, 20
    draw.rectangle([leg_right_x, leg_right_y, leg_right_x+3, leg_right_y+11], fill=camo_green)
    draw.rectangle([leg_right_x+1, leg_right_y+3, leg_right_x+2, leg_right_y+5], fill=camo_tan)
    draw.rectangle([leg_right_x+2, leg_right_y+7, leg_right_x+3, leg_right_y+9], fill=camo_brown)
    
    # Shoes
    draw.rectangle([leg_left_x, leg_left_y+11, leg_left_x+3, leg_left_y+11], fill=shoes_black)
    draw.rectangle([leg_right_x, leg_right_y+11, leg_right_x+3, leg_right_y+11], fill=shoes_black)
    
    # Back of head (for 3D model)
    back_head_x, back_head_y = 24, 0
    draw.rectangle([back_head_x, back_head_y, back_head_x+7, back_head_y+7], fill=skin_color)
    
    # Back of body
    back_body_x, back_body_y = 24, 8
    draw.rectangle([back_body_x, back_body_y, back_body_x+7, back_body_y+11], fill=camo_green)
    
    # Back camouflage patches
    draw.rectangle([back_body_x+2, back_body_y+2, back_body_x+4, back_body_y+4], fill=camo_brown)
    draw.rectangle([back_body_x+4, back_body_y+5, back_body_x+5, back_body_y+7], fill=camo_tan)
    draw.rectangle([back_body_x+1, back_body_y+6, back_body_x+3, back_body_y+8], fill=camo_tan)
    
    # Back of arms
    back_arm_left_x, back_arm_left_y = 20, 8
    draw.rectangle([back_arm_left_x, back_arm_left_y, back_arm_left_x+3, back_arm_left_y+11], fill=camo_green)
    
    back_arm_right_x, back_arm_right_y = 32, 8
    draw.rectangle([back_arm_right_x, back_arm_right_y, back_arm_right_x+3, back_arm_right_y+11], fill=camo_green)
    
    # Back of legs
    back_leg_left_x, back_leg_left_y = 20, 20
    draw.rectangle([back_leg_left_x, back_leg_left_y, back_leg_left_x+3, back_leg_left_y+11], fill=camo_green)
    
    back_leg_right_x, back_leg_right_y = 24, 20
    draw.rectangle([back_leg_right_x, back_leg_right_y, back_leg_right_x+3, back_leg_right_y+11], fill=camo_green)
    
    # Save the texture
    os.makedirs('src/main/resources/assets/policemod/textures/entity', exist_ok=True)
    img.save('src/main/resources/assets/policemod/textures/entity/soldier_mob.png')
    print("Soldier skin created with proper 3D mapping")

if __name__ == "__main__":
    create_police_skin()
    create_soldier_skin()
    print("Both skins created successfully!")