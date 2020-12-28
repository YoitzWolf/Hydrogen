
# -
from functools import wraps

import flask
from flask import make_response, render_template

import sqlalchemy
# -

from app.configs.basic import BaseAppConfig as Config
from app.configs.answ_coder import MessageCodes as MS
from app.manager	   import JSLoader

from app.sql import session as Session
from app.sql.models import *
from app.sql.models.task import Task
from app.sql.models.token import TokenTypes

# -

ERRTOKEN = Token.light_new(token_type=Token.Types.Err)
OKTOKEN  = Token.light_new(token_type=Token.Types.Ok)


blueprint: flask.Blueprint = flask.Blueprint('dev_api', __name__, template_folder=Config.TEMPLATE_FOLDER)

def make_token_response(f):
	@wraps(f)
	def decorator(*args, **kwargs):
		token, message = f(*args, **kwargs)
		print("out:", token, message)
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


@blueprint.route("/api/dev/send", methods=['POST'])
@make_token_response
def save_solution():
	return (ERRTOKEN, MS.INVALID_TASK)


@blueprint.route("/api/dev/task")
def get_task():

	query = flask.request.args

	if query.get("id"): 
		session = Session.create_session()
		game = session.query(Game).filter(Game.id==query["id"]).first()

		task = Task(session, Config, game)
		task.load()

		session.close()

		return 	make_response(
				{
					"body": task.get_json()
				}
			) 
	
	else: return "None"


@blueprint.route("/api/dev/all")
def get_all():
	session = Session.create_session()
	notes = session.query(UcNote).all()
	session.close()

	return make_response(
				{
					"body": list([ i.get_json() for i in list(notes)])
				}
			)

'''

@blueprint.route("/api/hubs/connections")
def get_connections_by_token():

	query = flask.request.args
	if "token" not in query:
		return make_response({"body": []})

	session = Session.create_session()

	lst = session.query(Token).filter(Token.token==query.get("token"), Token.token_type==Token.Types.Auth.name)
	if len(lst.all()) == 0: 		 return make_response({"body": []})
	elif not lst.first().is_valid(): return make_response({"body": []})

	user = lst.first().owner_id

	connections = session.query(Connection).filter(Connection.owner_id==user).all()

	session.close()

	return make_response(
				{
					"body": list([ i.get_json() for i in list(connections)])
				}
			)


@blueprint.route("/api/hubs/filter")
def get_filtered():
	query = flask.request.args

	session = Session.create_session()
	hubs = session.query(Hub).filter()

	for attr, value in query.items():
		hubs = hubs.filter(getattr(Hub, attr).like( (value if attr not in Hub.INTVALUES else int(value)) ))

	session.close()

	return make_response(
				{
					"body": list([ i.get_json() for i in list(hubs.all())])
				}
			)

@blueprint.route("/api/hubs/clear")
@make_token_response
def kill_all():
	session = Session.create_session()
	hubs = session.query(Hub).delete()
	hubs = session.query(Connection).delete()
	session.commit()
	session.close()

	return (OKTOKEN, MS.Ok)


'''
