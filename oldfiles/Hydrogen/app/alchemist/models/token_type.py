import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec

from app.alchemist.session import SqlAlchemyBase

class TokenType(SqlAlchemyBase):
    __tablename__ = 'token_types'

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )

    size = sqlalchemy.Column(
        sqlalchemy.Integer
    )

    ttl = sqlalchemy.Column(
        sqlalchemy.DateTime,
        default=datetime.time(microsecond=0)
    )

    token_type = sqlalchemy.Column(
        sqlalchemy.String,
        unique=True
    )

    tokens = orm.relation('Token', back_populates="token_type")

    def __repr__(self):
        return f"<TokenType> {self.token_type} id: {self.id} size:{self.size}"
    
    def to_dict(self):
        return {
            "type": "<TokenType>",
            "size": self.size,
            "id": self.id,
            "token_type": self.token_type
        }

    def __hash__(self):
        return hash(self.token_type)

    def __eq__(self, another):
        return self.id == another.id or self.token_type == another.token_type