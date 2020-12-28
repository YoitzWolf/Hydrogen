import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec
from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from app.alchemist.session import SqlAlchemyBase

class User(UserMixin, SqlAlchemyBase):
    __tablename__ = 'users'

    token = sqlalchemy.Column(
        sqlalchemy.String(32),
        unique=True
    )

    token_ext = sqlalchemy.Column(
        sqlalchemy.Integer,
        default=0
    )

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )

    login = sqlalchemy.Column(
        sqlalchemy.String,
        unique=True
    )

    email = sqlalchemy.Column(
        sqlalchemy.String,
        unique=True
    )

    password_hash = sqlalchemy.Column(sqlalchemy.String)
    last_enter_date = sqlalchemy.Column(
        sqlalchemy.DateTime, default=datetime.datetime.now)

    hubs = orm.relation("Hub", back_populates='author')
    games = orm.relation("Game", back_populates='author')
    hub_connections = orm.relation("UTH", back_populates='user')

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def __repr__(self):
        return f"<User> {self.id} {self.login} {self.email}"
    
    def to_dict(self):
        return {
            "type": "<User>",
            "repr": self.__repr__(),
            "id": self.id,
            "login": self.login,
            "email": self.email
        }