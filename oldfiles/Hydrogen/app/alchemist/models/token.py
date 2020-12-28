import datetime
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec

from app.alchemist.session import SqlAlchemyBase

class Token(SqlAlchemyBase):
    __tablename__ = 'tokens'

    token = sqlalchemy.Column(
        sqlalchemy.String(),
        unique=True,
        primary_key=True
    )

    token_type_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("token_types.id")
    )

    token_type = orm.relation("TokenType")

    utc_birth = sqlalchemy.Column(
        sqlalchemy.DateTime,
        default=datetime.datetime.utcnow()
    )

    owner_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
    )

    owner = orm.relation("User")

    def __repr__(self):
        return f"<Token> {self.token} type: {self.token_type.__repr__()} [ UTC BIRTH: {self.utc_birth}; TTL: {self.token_type.ttl} ]"
    
    def to_dict(self):
        return {
            "type": "<Token>",
            "repr": self.__repr__(),
            "utc_birth": self.utc_birth,
            "token_type": self.token_type.__repr__(),
            "ttl": self.ttl         
        }


    def __hash__(self):
        return hash(self.token)

    def __eq__(self, another):
        return self.token == another.token