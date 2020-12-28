
# -
from functools import wraps

import flask
from flask import make_response, render_template

import sqlalchemy
# -

from app.configs.basic import BaseAppConfig as Config
from app.manager	   import JSLoader

from app.sql import session as Session
from app.sql.models import *

# -

ERRTOKEN = Token.light_new(token_type=Token.Types.Err)


blueprint: flask.Blueprint = flask.Blueprint('games_api', __name__, template_folder=Config.TEMPLATE_FOLDER)

def make_token_response(f):
	@wraps(f)
	def decorator(*args, **kwargs):
		token, message = f(*args, **kwargs)
		return make_response(
			{
				"body": {
					"token": token.get(),
					"token_type": token.token_type,
					"message": message.value
				}
			}
		)
	return decorator


@blueprint.route("/api/games/all", methods=["GET", 'POST'])
def get_game_list():
	session = Session.create_session()

	games = list([ i.get_json() for i in list(session.query(Game).all())])

	session.close()

	return  {
				"body": games
			}

