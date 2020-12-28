import json

class Loader():
	"""docstring for ClassName"""
	def __init__(self, config):
		self.folder = config.TEMPLATE_FOLDER

	def load(self, path:str) -> dict:
		with open(self.folder+path, "r") as context:
			return json.loads(context.read(), encoding="utf-8")
			
		raise Exception(f"DATA ERROR AT {path}")


	def get_menubar(self, path: str, activated: str = "home") -> dict:
		menu = self.load(path)
		if activated not in menu["items"]:
			raise KeyError(f"No such item in menubar: {activated}")
		menu[activated]["style"] += " " + menu[activated]["active"]
		return menu