from PIL import Image, ImageDraw
from enum import Enum
#from typing import NewType, TypeVar, List, Tuple

from .namespace import *
from .glon import glon as glon
from .mapper import Actor

Glon = glon.GLOAD.pseudo()

class Map():
	dct: Enum   # Enum of Fields
	data: list  # list of lists
	width: int 	# x
	height: int # y
	layers: int # z
	textureformat: int

	players: dict # PLAYER:(X, Y, Z, VIZIBLE_DISTANCE)

	__document: Actor

	__hached: list

	__DEBUG = True

	# users are at the top level


	def size(self): return (self.width, self.height)

	def __init__(self, actor: Actor=None, width: int = 32, height: int = 32, layers: int = 5, data: list = None, textureformat: int = 1, dct: Enum = None):

		if width < 1 or height < 1 or layers <= 1: raise Exception("Invalid sizes. 1 is minimum. Min 2 layers")

		if dct is None or Enum is None:
			raise Exception("Wrong data input when map creating: data or dct is None")

		if layers != len(data) or any( (len(i) != height or any(len(j) != width for j in i)) for i in data):
			raise Exception("Wrong data input when map creating: data or dct is None")

		self.dct = dct
		self.data = data
		for i in data:
			for j in i:
				for k in range(len(j)):
					j[k] = j[k].value if j[k] is not None else dct.Air.value

		self.width = width
		self.height = height
		self.layers = layers

		self.players = {}

		self.textureformat = textureformat

		self.__document = actor

		self.__hached = list()

	@Glon.Set_field
	def set_field(self, x: int=0, y: int=0, layer: int=0, item: Field=None):
		if item is None: item = self.dct.raw
		if layer >= layers or layer < 1: raise Exception("Level Index Error")
		if x >= width or x < 1: 		 raise Exception("Width (X) Index Error")
		if y >= height or y < 1: 	     raise Exception("Height (Y) Index Error")

		self.data[layer][x][y] = item

	def move_field_to(self, x:int, y:int, layer:int, x1:int, y1:int, layer1:int, placeholder:Field=None):
		if placeholder is None: placeholder = self.dct.raw
		set_field(self, x=x1, y=y1, layer=layer1, item=self.data[layer][x][y])
		set_field(self, x=x, y=y, layer=layer, item=placeholder)

	def put_player(self, player, cortege):
		self.players[player] = cortege

	def move_field_by(self, x:int, y:int, layer:int, dx:int, dy:int, layer1:int, placeholder:Field=None):
		if placeholder is None: placeholder = self.dct.raw
		set_field(self, x=x+dx, y=y+dy, layer=layer1, item=self.data[layer][x][y])
		set_field(self, x=x, y=y, layer=layer, item=placeholder)

	def set_rect(self, x: int=0, y: int=0, x1:int=0, y1:int=0, layer: int=0, item: Field=None):
		if item is None: item = self.dct.raw
		if layer >= layers or layer < 1: raise Exception("Level Index Error")
		if x >= width or x < 1: raise Exception("Width (X) Index Error")
		if y >= height or y < 1: raise Exception("Height (Y) Index Error")
		if x1 >= width or x1 < 1: raise Exception("Width (X1) Index Error")
		if y1 >= height or y1 < 1: raise Exception("Height (Y1) Index Error")

		for i in range(min(x, x1), max(x, x1) + 1):
			for j in range(min(y, y1), max(y, y1) + 1):
				self.set_field(x=i, y=j, layer=layer, item=item)

	def get_size(self) -> tuple:
		return (self.width, self.height, self.layers)

	def get_sq_size(self) -> tuple:
		return (self.width, self.height)

	def generate_tick(self) -> Image:

		# image = Image.new(mode="RGBA", size=(self.width*self.textureformat, self.height*self.textureformat), color="rgb(1, 1, 1)")
		
		# document = ImageDraw.Draw(image)

		for z in range(self.layers):
			for y in range(self.height):
				for x in range(self.width):
					self.data[z][y][x].draw(x, y, self.__document, size=self.textureformat)

				for player in self.players:
					coords = self.players[player][0:3]
					if coords[1] == y and coords[2] == z:
						player.draw(coords[0], coords[1], self.__document, size=self.textureformat)
						
		#self.__hached.append(image)
		
		if Map.__DEBUG:
			print(self.width*self.textureformat, self.height*self.textureformat)
			#image.show()
		return self.__document


	# Overrideable
	def get_by_radius(self, player_info) -> tuple:
		return (
					player_info[0] - player_info[3],
					player_info[1] - player_info[3],
					player_info[0] + player_info[3],
					player_info[1] + player_info[3]
				)

	# Overrideable
	def get_player_view(self, player) -> Image:
		# gets a suare with center in players coords
		import copy
		image = copy.deepcopy(self.__hashed[-1])
		return image.crop(self.get_radius(self.players[player]))



class GameBase():
	__max_users: int
	__tick: int
	__max_commands_count: int
	__map: Map
	__actions: Enum

	__players: list

	ended: bool

	pre_events: dict
	post_events: dict

	def exit(self):
		self.ended = True

	def add_player(self, player: Player, coords, visibility=None):
		if visibility is None: visibility = self.__map.size()[0]
		self.__players.append(Player)
		self.__map.put_player(player, (*coords, visibility))

	def check_map(self):
		return self.__map.generate_tick()

	def eventloop(self, maxticks=1000):
		self.results = {}
		while 1:

			Glon.next(self.__tick)

			for event in self.pre_events: event(*self.pre_events[event]["args"], **self.pre_events[event]["kwargs"])

			# user_events

			for player in self.__players:
				player.controller.use_context(self.__map.get_player_view(player), self, self.__max_commands_count)
				command_line = player.controller.get_answer()
				for i in range(min(len(command_line)), self.__max_commands_count):
					if command_line[i] in self.__actions: command_line[i].action(self, player) # map is changing, but not visually
				
			# end user_events

			for event in self.post_events: event(*self.post_events[event]["args"], **self.post_events[event]["kwargs"])

			image = self.__map.generate_tick()
			self.__tick += 1

			if self.__tick > maxticks and not self.ended:
				self.ended = true
				self.results = {
					"verdict": Verdicts.TickLimit,
					"winner": None
				}

			if self.ended: return self.results

	def __init__(self, mapdata: dict={}, pre_events=[], post_events=[], max_commands_count=1, actions=None, fields=None, textures=None, entitys=None, vGLON=None):

		if actions is None: actions = HydrogenSTD.ExampleActionEnum
		self.__max_commands_count = max_commands_count
		self.__actions = actions
		self.pre_events = pre_events
		self.post_events = post_events
		self.ended = False
		self.__players = []
		self.results = {}

		self.__map = Map(**mapdata)



