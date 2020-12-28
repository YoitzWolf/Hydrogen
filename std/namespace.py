from PIL import ImageColor
from enum import Enum
from typing import List

from .mapper import Actor

class Texture():
	size: int
	data: List[List[str]]
	name: str

	def __init__(self, size, texture=None):
		self.data = []
		self.size = size
		if texture is None: texture = [ [ "rgba(0, 0, 0, 0)" for j in range(size) ] for i in range(size) ]
		else:
			if(len(texture) < size): raise IndexError("Texture Hight is too short")
			for line in texture:
				if len(line) < size: raise IndexError("Texture Width is too short")
		self.data = texture

	def set_texture(self, texture=None):
		if texture is None: texture = [ [ "#111" for j in range(self.size) ] for i in range(self.size) ]
		else:
			if(len(texture) < self.size): raise IndexError("Texture Hight is too short")
			for line in texture:
				if len(line) < self.size: raise IndexError("Texture Width is too short")
		self.data = texture

	def clone(self): self.data = self.data + []

	def get_pixel(self, x: int, y: int, size: int = None) -> str:
		if size is not None and size != self.size: raise IndexError(f"self size{self.size} != {size}")
		return self.data[y][x]

	@staticmethod
	def filter(base, color, color_mode="RGBA", mode=0):
		color = ImageColor.getcolor(color, mode=color_mode)
		base = list(base)
		base[0] = min(int(base[0] + (color[0] - base[0]) * (color[3] / 255)), 255) 
		base[1] = min(int(base[1] + (color[1] - base[1]) * (color[3] / 255)), 255) 
		base[2] = min(int(base[2] + (color[2] - base[2]) * (color[3] / 255)), 255)
		base[3] = 255
		return tuple(base)


class Field():
	texture: Texture
	name: str
	__tags: list

	def has(tag) -> bool: return tag in self.__tags

	def get_texture() -> Texture: return self.texture

	def get_tags() -> list: return tuple(this.__tags)

	def __init__(self, texture=None, name:str="unnamed", *tags):
		self.name = name
		self.texture = texture
		self.__tags = tags

	def draw(self, X: int, Y: int, doc: Actor, size: int=None):
		# .draw(x, y, document, size=self.textureformat)
		if size is None: size=self.texture.size
		elif self.texture.size != size:
			self.texture.convert(size)
		# doc.point((x, y), color)
		for x in range(size):
			for y in range(size):
				doc.point((x+X, y+Y), Texture.filter(doc.get_pixel(x+X, y+Y), self.texture.get_pixel(x, y, size) ))


class Controller():
 
	actions: Enum

	"""docstring for Controller"""
	def __init__(self, actions):
		self.actions = actions
		self.context = None
		self.answer = None

	def use_context(self, view, game, maxsize):
		self.context = (view, game)

	def get_answer(self):
		if self.answer is None or self.context is None: return None
		return self.answer
		

class Entity(Field):
	"""docstring for Entity"""

	controller: Controller
	name: str

	def __init__(self, name="unnamed", controller=None, *args, **kwargs):
		super(Entity, self).__init__(name=name, *args, **kwargs)
		self.controller = controller
		self.name = name


class Player(Entity):
	"""docstring for Player"""
	def __init__(self, name="unnamed", controller=None, *args, **kwargs):
		super(Player, self).__init__(name=name, controller=controller, *args, **kwargs)
		

class Action():

	__docstring: str

	"""docstring for Action"""
	def __action(map, player):
		pass

	def __repr__(): return self.__docstring

	def __init__(self, action, docstring=""):
		self.__action = action
		self.__docstring = docstring
		

class ExampleActionEnum(Enum):
    """Token type enumeration"""
    Example = Action(lambda game, player: game.move_player(player, 1, 1), "Example HydrogenSTD Action")

class ExampleTextureEnum(Enum):
    """Token type enumeration"""
    raw = Texture(1) # Important texture - default placeholder for untyped 
		