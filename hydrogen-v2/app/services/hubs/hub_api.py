
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
from app.sql.models.token import TokenTypes

# -

ERRTOKEN = Token.light_new(token_type=Token.Types.Err)
OKTOKEN  = Token.light_new(token_type=Token.Types.Ok)


blueprint: flask.Blueprint = flask.Blueprint('hubs_api', __name__, template_folder=Config.TEMPLATE_FOLDER)

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


@blueprint.route("/api/hubs/new", methods=['POST'])
@make_token_response
def create_hub():
	data = flask.request.get_json()
	print(data)
	if data:
		session = Session.create_session()

		lst = session.query(Token).filter(Token.token==data["token"], Token.token_type==Token.Types.Auth.name)

		if len(lst.all()) == 0: 		 return (ERRTOKEN, MS.AUTH_Err)
		elif not lst.first().is_valid():
			print(list(lst))
			return (ERRTOKEN, MS.INVALID_TOKEN)

		if len(session.query(Hub).filter(Hub.name==data["name"]).all()) > 0:   return (ERRTOKEN, MS.NAME_IS_TAKEN)
		elif len(session.query(Game).filter(Game.id==data["game_id"]).all()) == 0: return (ERRTOKEN, MS.UNKNOWN_VALUE)

		else:
			hub = Hub(
					name=data['name'],
					owner_id=lst.first().owner.id,
					description=data['description'],
					public= (not data['is_public']),
					users_limit=min(256, max(2, (data["users_limit"] if "users_limit" in data else 0) )),
					game_id=data['game_id']
				)
			hub.set_password(data['password'])
			session.add(hub)
			session.commit()

			hub = session.query(Hub).filter(
				Hub.name==data['name'],
				Hub.owner_id==lst.first().owner.id
			).first()

			connection = Connection(
				owner_id=hub.owner_id,
				hub_id=hub.id
			)
			session.add(connection)
			session.commit()

			session.close()

			return (OKTOKEN, MS.HUB_CREARED)

		session.close()
	return (ERRTOKEN, MS.INVALID_TASK)


# sign into hub

@blueprint.route("/api/hubs/login", methods=['POST'])
@make_token_response
def sign_into():
	data = flask.request.get_json()
	if data:
		session = Session.create_session()

		lst = session.query(Token).filter(Token.token==data['token'], Token.token_type==Token.Types.Auth.name)
		if len(lst.all()) == 0: 		 return (ERRTOKEN, MS.AUTH_Err)
		elif not lst.first().is_valid(): return (ERRTOKEN, MS.INVALID_TOKEN)

		user = lst.first().owner

		lst = session.query(Hub).filter(Hub.id==data['hub_id'])

		if len(lst.all()) == 0: return (ERRTOKEN, MS.UNKNOWN_VALUE)

		hub = lst.first()
		if len(session.query(Connection).filter(Connection.hub_id==hub.id, Connection.owner_id==user.id).all()) > 0 : return (ERRTOKEN, "already connected!")
		elif not hub.check_password(data["password"]): return (ERRTOKEN, MS.WRONG_PASSW)
		elif len(hub.connections) >= hub.users_limit: return (ERRTOKEN, MS.HUB_LIMIT)
		else:

			connection = Connection(
					owner_id=user.id,
					hub_id=hub.id
				)
			session.add(connection)
			session.commit()
			session.close()

			return (OKTOKEN, MS.HUB_CONNECTED)

		session.close()

	return (ERRTOKEN, MS.INVALID_TASK)


@blueprint.route("/api/hubs/get_by_id")
def get_by_id():
	query = flask.request.args
	if query.get("id"): 
		session = Session.create_session()
		hubs = session.query(Hub).filter(Hub.id==query["id"])
		session.close()
		return make_response(
					{
						"body": (hubs.first().get_json() if len(hubs.all()) > 0 else list(hubs))
					}
			   )
	else: return make_response( {
			body: None
		} )


@blueprint.route("/api/hubs/all")
def get_all():
	session = Session.create_session()
	hubs = session.query(Hub).all()
	session.close()

	return make_response(
				{
					"body": list([ i.get_json() for i in list(hubs)])
				}
			)


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


@blueprint.route("/api/hubs/connections/all")
def get_connections():

	session = Session.create_session()

	connections = session.query(Connection).all()

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



