import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as decl

from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from app.sql.session import SqlAlchemyBase


class Connection(SqlAlchemyBase):

	#def defAv():
	#	return open("./static/databases/images/avatars/def.png", "rb").read()

	__tablename__ = "connections"

	id = sqlalchemy.Column(
		sqlalchemy.Integer,
		primary_key=True,
		autoincrement=True
	)


	owner_id = sqlalchemy.Column(
		sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
	)
	owner = orm.relation("User", lazy='subquery')

	hub_id = sqlalchemy.Column(
		sqlalchemy.Integer, sqlalchemy.ForeignKey("hubs.id")
	)
	hub = orm.relation("Hub", lazy='subquery')

	def __repr__(self): return f"<Connection> {self.id} {self.owner.login} {self.hub.name}"

	def to_dict(self):
		return {
			"type": "<Connection>",
			"id": self.id,
			"hub": self.hub.to_dict(),
			"owner": self.owner.to_dict()
		}

	def get_json(self):
		return {
			"id": self.id,
			"owner": self.owner_id,
			"hub": self.hub.get_json()
		}