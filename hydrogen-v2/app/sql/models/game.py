import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as decl

from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from app.sql.session import SqlAlchemyBase


class Game(SqlAlchemyBase):

	#def defAv():
	#	return open("./static/databases/images/avatars/def.png", "rb").read()

	__tablename__ = "games"

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

	def __repr__(self): return f"<Game> {self.id} {self.name}"

	def to_dict(self):
		return {
			"type": "<Game>",
			"id": self.id,
			"name": self.name,
			"description": self.description,
			"owner": self.owner
		}

	def get_json(self):
		return {
			"id": self.id,
			"name": self.name,
			"description": self.description,
			"owner": self.owner.login
		}