import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as decl

from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from app.sql.session import SqlAlchemyBase


class User(UserMixin, SqlAlchemyBase):

	def defAv():
		return open("./static/databases/images/avatars/def.png", "rb").read()

	__tablename__ = "users"

	id = sqlalchemy.Column(
		sqlalchemy.Integer,
		primary_key=True,
		autoincrement=True
	)

	login = sqlalchemy.Column(
		sqlalchemy.String,
		unique = True
	)

	email = sqlalchemy.Column(
		sqlalchemy.String,
		unique=True
	)

	avatar = sqlalchemy.Column(
		sqlalchemy.Binary,
		default=defAv()
	)

	password_hash = sqlalchemy.Column(sqlalchemy.String)

	def __init__(self, *args, **kwargs):
		super(User, self).__init__(*args, **kwargs)

	def set_password(self, password):
		self.password_hash =generate_password_hash(password)

	def check_password(self, password):
		return check_password_hash(self.password_hash, password)

	def __repr__(self): return f"<User> {self.id} {self.login}"

	def to_dict(self):
		return {
			"type": "<User>",
			"id": self.id,
			"login": self.login,
			"email": self.email
		}

	def get_json(self):
		return {
			"login": self.login,
			"email": self.email,
			"avatar": str(self.avatar),
			"status": "USER"
		}