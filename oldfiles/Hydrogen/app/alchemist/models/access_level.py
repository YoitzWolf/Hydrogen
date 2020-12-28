import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as dec

from app.alchemist.session import SqlAlchemyBase

class AccessLevel(SqlAlchemyBase):
    __tablename__ = 'token_types'

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )



    def __repr__(self):
        return f"<TokenType> id: {self.id}"
    
    def to_dict(self):
        return {
            "type": "<TokenType>"
        }