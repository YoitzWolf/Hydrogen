version = "0.0.1"

from . import js_manager as JSM

JSLoader = None

def init(config):
	global JSLoader

	JSLoader = JSM.Loader(config)
	# print(JSLoader)