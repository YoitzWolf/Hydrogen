import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec

from app.alchemist.session import SqlAlchemyBase

class UTH(SqlAlchemyBase):
    __tablename__ = 'uth'

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )
    # author
    user_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
    )
    user = orm.relation('User')

    # game
    hub_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("hubs.id")
    )
    hub = orm.relation('Hub')

    def __repr__(self):
        return f"<CONNECTION <User:Hub>> {self.id} {self.user.__repr__()} : {self.hub.__repr__()}"
    
    def to_dict(self):
        return {
            "type": "<CONNECTION <User:Hub>>",
            "repr": self.__repr__(),
            "id": self.id,
            "hub": self.hub.to_dict(),
            "user": self.user.to_dict()
        }