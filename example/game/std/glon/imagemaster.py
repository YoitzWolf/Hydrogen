
def convert_to_size(raw, scale):
	extracted = []
	for y in range(len(raw)):
		line = []
		for x in range(len(raw)):
			for i in range(scale): line += list(raw[y][x])
		for i in range(scale): extracted += line
	return bytes(extracted)

def show(image, size=1000):
	from PIL import Image
	b = list(image.tobytes())
	color_line = [ (b[i], b[i+1], b[i+2], b[i+3]) for i in range(0, len(b), 4) ]

	raw = []

	index = 0
	for y in range(image.height):
		line = []
		for j in range(image.width):
			line.append(color_line[index])
			index += 1
		raw.append(line)

	# print(*raw, sep="\n")
	im = Image.frombytes('RGBA', (size,size), convert_to_size(raw, size//len(raw)))
	im.show()


if __name__ == '__main__':
	from PIL import Image
	show(Image.open("../im.png"))