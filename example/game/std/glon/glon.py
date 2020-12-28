#-*- GLON FORMAT -*-


class GLOAD():
	__STANDART = None
	__RESULT = dict()

	'''
	def action_decorator(name:str):
		def decorator(f):
			return f()
		return decorator
	'''
	
	def __init__(self, standart):
		self.__RESULT = dict()
		self.__STANDART = standart

	def pseudo(): return GLOAD(None)

	def load_standart(self, standart_file:str):
		import json

		global __STANDART

		with open(standart_file, "r") as file:
			__STANDART = json.loads(file.read())


	def next(self, *args, **kwargs): pass
	def set_field(self, *args, **kwargs): pass




def load_with_standart(standart_file:str="./std/glon/glon-l.json"):
	import json

	with open(standart_file, "r") as file:
		return GLOAD(json.loads(file.read()))


if __name__ == '__main__':
	load_with_standart("glon.alpha.v.0.0.1.json")