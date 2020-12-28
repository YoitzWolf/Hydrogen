
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

# -

ERRTOKEN = Token.light_new(token_type=Token.Types.Err)


blueprint: flask.Blueprint = flask.Blueprint('user_api', __name__, template_folder=Config.TEMPLATE_FOLDER)

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


@blueprint.route("/api/users/register", methods=["GET", 'POST'])
@make_token_response
def register_user():
	data = flask.request.get_json()
	if data:
		session = Session.create_session()

		lst = session.query(User).filter(User.login==data['login'], User.email==data['email']).all()
		if len(lst) != 0:
			return (ERRTOKEN, MS.NAME_IS_TAKEN)
		else:
			user = User(
				login=data["login"],
				email=data["email"],
			)
			user.set_password(data['password'])
			session.add(user)
			session.commit()
			session.query(User).filter(User.login==data["login"]).first()

			token = Token.new(session, user, Token.Types.Refr)

			return (token, MS.REG_Ok)

		session.close()

	return (ERRTOKEN, MS.INVALID_TASK)


# login by login + password

@blueprint.route("/api/users/login", methods=["GET", 'POST'])
@make_token_response
def login_user():
	data = flask.request.get_json()
	if data:
		session = Session.create_session()

		lst = session.query(User).filter(sqlalchemy.or_(User.login==data['login'], User.email==data['login']))
		if len(lst.all()) == 0: return (ERRTOKEN, MS.UNKNOWN_VALUE)
		else:
			user = lst.first()
			if not user.check_password(data['password']):
				return (ERRTOKEN, MS.WRONG_PASSW)

			token = Token.new(session, user, Token.Types.Refr)
			return (token, MS.AUTH_Ok)

		session.close()
	return (ERRTOKEN, MS.INVALID_TASK)


@blueprint.route("/api/users/refresh", methods=["GET", 'POST'])
@make_token_response
def refresh():
	query = flask.request.args

	data = flask.request.get_json()
	if data:
		session = Session.create_session()
		
		print("in", data)
		lst = session.query(Token).filter(sqlalchemy.and_(Token.token==data['token'], Token.token_type==data['token_type']))
		if len(lst.all()) == 0: return (ERRTOKEN, MS.INVALID_TOKEN)
		else:
			#user = lst.first()
			#if not user.check_password(data['password']):
			#	return (ERRTOKEN, "Wrong login or password")

			token = Token.new(session, lst.first().owner, Token.Types[query.get("out")])
			print("out: ", token)
			return (token, MS.REFRESHED)

		session.close()
	return (ERRTOKEN, MS.INVALID_TASK)


@blueprint.route("/api/users/getme")
def getme():
	query = flask.request.args
	if query.get("token"): 
		session = Session.create_session()
		token = user = session.query(Token).filter(Token.token==query.get("token"), Token.token_type==Token.Types.Auth.name).first()
		if token is None: return make_response(
									{
										"body": {
											"login": "unautorised",
											"email": "unautorised",
											"avatar": "ERROR",
											"status": "PSEUDO"
										}
									}
								)
		user = token.owner
		session.close()
		return make_response(
					{
						"body": user.get_json()
					}
			   )
