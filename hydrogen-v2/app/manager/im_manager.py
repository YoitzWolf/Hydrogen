import os
import sys


IMAGES = "./static/storage/"

def __getImage(name:str) -> str:
	try:
		with open(name) as file: return file.read()
	except OSError:
		raise OSError(f"NO SUCH FILE AS {name}")


def __load_image(f):
	def wrapper(name: str):
		return 


if __name__ == '__main__':
	print(__getImage("js_manager.py"))