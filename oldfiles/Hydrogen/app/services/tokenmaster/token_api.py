import time
import datetime
import secrets
import flask

from functools import wraps

from app.services.configs							import mkf
from app.services.configs							import tkf
from app.alchemist                                  import session as Session
from app.alchemist.models.user                      import User
from app.alchemist.models.token                     import Token
from app.alchemist.models.token_type                import TokenType


class CachedStorage(set):
	
	def __init__(self, model):
		self.model = model
		super().__init__(self)

	def add(self, unit):
		if not isinstance(unit, self.model): raise NotImplementedError(f"INVALID TYPE IN CachedStorage.add(unit) need: {self.model}, got: {unit.__class__}")
		super().add(unit)
		
cached_tokens: CachedStorage = CachedStorage(Token)


def get_time():
	return datetime.utcnow()

def get_token_ttl(hours:int = 0, minutes:int = 0, seconds:int = 0):
	return datetime.time(hours=hours, minutes=minutes, seconds=seconds)

def generate_valid_tocken(ttype: TokenType, owner: User) -> Token:
	
	token = Token(
		token_type_id = ttype.id,
		token_type = ttype,
		owner_id = owner.id
		)

	token.token = secrets.token_hex(ttype.size)

	while 1:
		if token.token in cached_tokens: #len(list(session.query(Token).filter(Token.token==token.token).all())) > 0	
			token.token = secrets.token_hex(ttype.size)		
		else:
			cached_tokens.add(token)
			return token


def set_token(user:User):
	session = Session.create_session()
	session.query(User).filter(User.id==user.id).update({"token": generatetocken(), "token_ext": get_token_ext(hours=4)})
	session.commit()

def get_currentuser(token: str, session) -> User:

	for u in cached_tokens:
		if token == u.token:
			token = u
			return session.query(User).filter(User.id==token.owner_id).first()
	return None


blueprint: flask.Blueprint = flask.Blueprint('token_api', __name__, template_folder=mkf.Config.TEMPLATE_FOLDER)

@blueprint.route("tokens/generate", methods=['POST'])
def generate_new():
	args = flask.request.body
	session = Session.create_session()
	u = session.query(User).filter(User.login==args["login"]).first()
	if u.check_password(args['password']):
		generate_valid_tocken(tkf.TokenTypeConfiguration.refresh_token, u)

@blueprint.route("tokens/list", methods=['POST', 'GET'])
def generate_new():
	return f""" { '<p>'+i.__repr__()+'</p>' for i in cached_tokens} """


