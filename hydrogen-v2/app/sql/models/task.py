from . import *
import sqlalchemy


class Task():

	#def defAv():
	#	return open("./static/databases/images/avatars/def.png", "rb").read()

	def load(self):
		adress = f"{self.config.GAMES_FOLDER}/{self.game.id}/task/task.html"
		self.taskHtml = open(adress, "r", encoding="utf8").read()

	def __init__(self, session, config, game):
		self.game = game
		self.taskHtml = ""
		self.config = config


	def __repr__(self): return f"<Task>"

	def to_dict(self):
		return {
			"type": "<Task>",
			"game": self.game.to_dict(),
			"taskHtml": self.taskHtml
		}

	def get_json(self):
		import flask
		return {
			
			"game": self.game.get_json(),
			"taskHtml": flask.render_template_string(
				self.taskHtml,
				game=self.game.get_json()
			)
		}