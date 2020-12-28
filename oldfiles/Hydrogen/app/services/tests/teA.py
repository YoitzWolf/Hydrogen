import sys
import os
import importlib


def getClass(path: str) -> object:
	usermd = importlib.import_module(path)
	return usermd

def writeClass(path: str):
	with open(path, "w") as file:
		file.write(
				"class USER():\n"
				"	def print(self):\n"
				"		print(\"I'm a user\")\n"
			)
		file.close()

if __name__ == '__main__':
	writeClass("usr-code/usr1.py")
	getClass("usr-code.usr1").USER().print()
		