# Example Game File

from enum import Enum

# self-importing
from std.main import GameBase, Map
from std.namespace import *
from std.mapper import Actor
# endblock



class TheGame(GameBase):
	def __init__(self, controllers, **baseargs):
		super(TheGame, self).__init__(**baseargs) # VERY Important line
		for name in controllers:
			texture = None
			self.add_player(Player(controller=controllers[name]["controller"], texture=texture, name=name, *controllers[name]["tags"]))



class ActionEnum(Enum):
	Right = Action(lambda game, player: game.do_action(player, "right"), "Example Hydrogen Right Action")
	Left = Action(lambda game,  player: game.do_action(player, "left"),  "Example Hydrogen Left Action")


class TextureEnum(Enum):
	raw = Texture(1) # Important texture - default placeholder for untyped 
	wall   = Texture(1,	[["rgba( 1,   1,   1,   255 )"]])
	floor  = Texture(1, [["rgba( 255, 255, 255, 255 )"]])
	target = Texture(1, [["rgba( 255, 0,   0,   255 )"]])
	player = Texture(1, [["rgba( 0,   255, 0,   255 )"]])


# texture=None, name:str="unnamed", *tags
class FieldEnum(Enum):
	Air = Field(texture=TextureEnum.raw.value, name="Air")
	Wall = Field(texture=TextureEnum.wall.value, name="Wall")
	Floor = Field(texture=TextureEnum.floor.value, name="Floor")
	Target = Field(texture=TextureEnum.target.value, name="Target")
	Player = Field(texture=TextureEnum.player.value, name="Player")


class EnityConstructorsEnum(Enum):
	Player = lambda name, controller: Player(name, controller=controller, texture=TextureEnum.player.value)	
		

		

def RUN_GAME(controllers):


	# Map.__init__ is ( width: int = 32 #width of map, height: int = 32 #hight of map, layers: int = 5 #layers count, data: list = None, textureformat: int = 1 # textture quality(square) in pixels, dct: Enum = None # texture list)

	generated = [

		[
			[ FieldEnum.Wall, FieldEnum.Wall,  FieldEnum.Wall,  FieldEnum.Wall,   FieldEnum.Wall ],
			[ FieldEnum.Wall, FieldEnum.Floor, FieldEnum.Floor, FieldEnum.Floor,  FieldEnum.Wall ],
			[ FieldEnum.Wall, FieldEnum.Floor, FieldEnum.Floor, FieldEnum.Floor,  FieldEnum.Wall ],
			[ FieldEnum.Wall, FieldEnum.Floor, FieldEnum.Floor, FieldEnum.Target, FieldEnum.Wall ],
			[ FieldEnum.Wall, FieldEnum.Wall,  FieldEnum.Wall,  FieldEnum.Wall,   FieldEnum.Wall ]

		],
		[
			[None, None, None, None, None],
			[None, None, None, None, None],
			[None, None, None, None, None],
			[None, None, None, None, None],
			[None, None, None, None, None]

		]

	] # generated map/ list[list[list[Texture]]]
	# You can use None fields to write unknown textures

	from std.glon.glon import GLOAD, load_with_standart

	mapdata = {
		"actor": Actor(5, 5),
		"width": 5,
		"height": 5,
		"layers": 2,
		"data": generated,
		"textureformat": 1,
		"dct": FieldEnum
	}

	glon = load_with_standart()

	game = TheGame(controllers, mapdata=mapdata, actions=ActionEnum, fields=FieldEnum, textures=TextureEnum, entitys=EnityConstructorsEnum)
	game.add_player(EnityConstructorsEnum.Player("Player", Controller([]) ), (1, 1, 0))
	im = game.check_map()
	print(im.log_changes(glon))
	# from gmaster import imagemaster as imstr
	# imstr.show(im)


if __name__ == '__main__':
	RUN_GAME([])
	print(1)