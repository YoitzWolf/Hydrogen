import os

# -
from functools import wraps

import flask
from flask import make_response, render_template, redirect, url_for
from werkzeug.utils import secure_filename

import sqlalchemy
# -

from app.configs.basic import BaseAppConfig as Config
from app.manager	   import JSLoader

from app.sql import session as Session
from app.sql.models import *


blueprint: flask.Blueprint = flask.Blueprint('gms_webp', __name__, template_folder=Config.TEMPLATE_FOLDER)

@blueprint.route("/web/games/new", methods=["GET"])
def load_page():
	bar = JSLoader.get_menubar("/json/bar-menu-list.json", activated="add_games")

	return render_template("/general/game_loader.html", bar=bar)


@blueprint.route("/web/games/new", methods=["POST"])
def background_load_page():

	form = flask.request.form

	if 'file' in flask.request.files:
		file = flask.request.files["file"]
		if file:
			filename = secure_filename(file.filename)
			print(filename)
			file.save(Config.RAW_FOLDER + "/" + filename)

			session = Session.create_session()

			owner = session.query(User).filter(User.email==form["owner_email"]).first()

			game = Game(
				name=form["name"],
				description=form["description"],
				owner_id=owner.id
			)

			session.add(game)

			try:
				import zipfile

				ids = session.query(Game).filter(Game.owner_id==owner.id, Game.name==game.name).first().id

				fantasy_zip = zipfile.ZipFile(Config.RAW_FOLDER + "/" + filename)

				fantasy_zip.extractall(Config.GAMES_FOLDER + "/" + str(ids) )
				 
				fantasy_zip.close()

				session.commit()
			except Exception as e:
				print(e)

			session.close()

	return redirect(url_for('gms_webp.load_page'))


@blueprint.route("/web/games/all", methods=["GET"])
def get_game_list():

	bar = JSLoader.get_menubar("/json/bar-menu-list.json", activated="all_games")

	session = Session.create_session()

	games = list([ i.get_json() for i in list(session.query(Game).all())])

	session.close()

	return render_template("/general/game_viewer.html", games=games, bar=bar)