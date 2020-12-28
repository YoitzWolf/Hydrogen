import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as decl

from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from app.sql.session import SqlAlchemyBase


class Hub(SqlAlchemyBase):

	INTVALUES = ["id", "owner_id", "game_id", "users_limit"]

	#def defAv():
	#	return open("./static/databases/images/avatars/def.png", "rb").read()

	__tablename__ = "hubs"

	id = sqlalchemy.Column(
		sqlalchemy.Integer,
		primary_key=True,
		autoincrement=True
	)

	name = sqlalchemy.Column(
		sqlalchemy.String,
		unique = True
	)

	description = sqlalchemy.Column(
		sqlalchemy.String(),
		default="No description"
	)

	owner_id = sqlalchemy.Column(
		sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
	)
	owner = orm.relation("User", lazy='subquery')

	users_limit = sqlalchemy.Column(sqlalchemy.Integer)

	public = sqlalchemy.Column(sqlalchemy.Boolean, default=True)

	password_hash = sqlalchemy.Column(sqlalchemy.String)

	game_id = sqlalchemy.Column(
		sqlalchemy.Integer, sqlalchemy.ForeignKey("games.id")
	)
	game = orm.relation("Game", lazy='subquery')

	connections = orm.relation("Connection", back_populates="hub", lazy='subquery')

	def set_password(self, password):
		self.password_hash =generate_password_hash(password)

	def check_password(self, password):
		if self.public: return True
		return check_password_hash(self.password_hash, password)

	def __repr__(self): return f"<Hub> {self.id} {self.name}"

	def to_dict(self):
		return {
			"type": "<Hub>",
			"id": self.id,
			"name": self.name,
			"game": self.game.to_dict(),
			"owner": self.owner
		}

	def get_json(self):
		from app.sql import session as Session
		return {
			"id": self.id,
			"name": self.name,
			"owner": self.owner.login,
			"description": self.description,
			"game": self.game.get_json(),
			"is_public": self.public,
			"max_players": self.users_limit,
			"players": len(Session.create_session().query(Hub).filter(Hub.id==self.id).all()	)
		}

	def to_json(self): return self.get_json()