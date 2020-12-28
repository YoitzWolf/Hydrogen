# Mapper Log Master

import numpy as np

class Actor():

	__changes: dict
	__map	 : list # this tick map
	__hached : list # last tick map

	def __init__(self, w:int, h:int):
		self.__map 		= list([ list([ (0, 0, 0, 0) for X in range(w) ]) for Y in range(h) ])
		self.__changes  = {}
		self.__hached   = list([ list([ (0, 0, 0, 0) for X in range(w) ]) for Y in range(h) ])
		
	def point(self, coords, color: tuple):
		x, y = coords
		self.__map[x][y] = color
		self.__changes[ (x, y) ] = color

	def get_pixel(self, x:int, y:int) -> tuple:
		return self.__map[x][y]

	def set_matrix(self, matrix, begin_coords):
		for y in range(len(matrix)):
			for x in range(len(matrix[y])):
				self.__map[y + begin_coords[1]][x + begin_coords[0]] = matrix[y][x]



	def clear(self):
		self.__hached = self.__map + []
		self.__changes = {}

	def log_changes(self, glon):
		glon.next()

		self.__changes = {}
		for Y in range(len(self.__map)):
			for X in range(len(self.__map[Y])):
				if (self.__map[Y][X] != self.__hached[Y][X]):
					self.__changes[(X, Y)] = self.__map[Y][X]
		glon.add_changes(self.__changes)
		return glon.getlog()