import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec
from werkzeug.security import generate_password_hash, check_password_hash

from app.alchemist.session import SqlAlchemyBase

class Game(SqlAlchemyBase):
    __tablename__ = 'games'

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )

    name = sqlalchemy.Column(
        sqlalchemy.String,
        unique=True
    )

    description = sqlalchemy.Column(
        sqlalchemy.String
    )

    maxplayers = sqlalchemy.Column(sqlalchemy.Integer)

    # author
    author_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
    )
    author = orm.relation('User')

    # game
    hubs = orm.relation('Hub', back_populates="game")


    def __repr__(self):
        return f"<Game> {self.id} {self.name} author:[{self.author.__repr__()}]"
    
    def to_dict(self):
        return {
            "type": "<Game>",
            "repr": self.__repr__(),
            "id": self.id,
            "name": self.name
        }