from PIL import Image, ImageDraw, ImageColor
from enum import Enum


def generate_tick() -> Image:
	size = (10, 10)
	image = Image.new(mode="RGBA", size=size, color="black")
	image.show()

generate_tick()