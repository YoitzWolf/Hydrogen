import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec
from werkzeug.security import generate_password_hash, check_password_hash

from app.alchemist.session import SqlAlchemyBase

class Hub(SqlAlchemyBase):
    __tablename__ = 'hubs'

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )

    password_hash = sqlalchemy.Column(sqlalchemy.String)

    last_enter_date = sqlalchemy.Column(
        sqlalchemy.DateTime, default=datetime.datetime.now
    )

    name = sqlalchemy.Column(
        sqlalchemy.String,
        unique=True
    )

    # author
    author_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
    )
    author = orm.relation('User')

    # game
    game_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("games.id")
    )
    game = orm.relation('Game')

    max_user_count = sqlalchemy.Column(sqlalchemy.Integer, default=10)

    users = orm.relation("UTH", back_populates='hub')

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def __repr__(self):
        return f"<Hub> {self.id} {self.name} author:[{self.author.__repr__()}]"
    
    def to_dict(self):
        return {
            "type": "<Hub>",
            "repr": self.__repr__(),
            "id": self.id,
            "game": self.game.to_dict()
        }