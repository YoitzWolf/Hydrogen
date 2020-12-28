import datetime
import secrets
import sqlalchemy
import sqlalchemy.orm as orm
import sqlalchemy.ext.declarative as decl

from enum import Enum

from werkzeug.security import generate_password_hash, check_password_hash

from app.sql.session import SqlAlchemyBase

class TokenType():

    ttl: datetime.timedelta
    name: str
    size: int

    def __init__(self, name, ttl, size=32):
        self.name = name
        self.ttl = ttl
        self.size = size

    def __len__(self): return self.size

    def __repr__(self):
        return f"<TokenType> {self.name}, TTL:{str(self.ttl)}"


class TokenTypes(Enum):
    """Token type enumeration"""
    Auth = TokenType("auth", datetime.timedelta(hours=5))
    Refr = TokenType("refr", datetime.timedelta(hours=5))
    Err  = TokenType("err",  datetime.timedelta(seconds=0))
    Ok   = TokenType("ok",   datetime.timedelta(seconds=0))


class Token(SqlAlchemyBase):

    Types = TokenTypes

    __tablename__ = "tokens"

    id = sqlalchemy.Column(
        sqlalchemy.Integer,
        primary_key=True,
        autoincrement=True
    )

    token = sqlalchemy.Column(
        sqlalchemy.String,
        unique = True
    )

    utc_birth = sqlalchemy.Column(
        sqlalchemy.DateTime
    )

    token_type = sqlalchemy.Column(
        sqlalchemy.String
    )
    
    owner_id = sqlalchemy.Column(
        sqlalchemy.Integer, sqlalchemy.ForeignKey("users.id")
    )

    owner = orm.relation("User", lazy='subquery')

    def __repr__(self): return f"<Token> {self.token} {self.owner.__repr__()}"

    def __str__(self): return f"<Token> {self.token} {self.owner.__repr__()}"

    def to_dict(self):
        return {
            "type": "<Token>",
            "token": self.token,
            "owner_repr": self.owner.__repr__()
        }

    def __hash__(self):
        return hash(self.token)

    def __eq__(self, another):
        return self.token == another.token

    def get(self): return self.token

    def is_valid(self):
        print(self.utc_birth + TokenTypes[self.token_type].value.ttl, ">", datetime.datetime.utcnow())
        return (self.utc_birth + TokenTypes[self.token_type].value.ttl > datetime.datetime.utcnow())

    def new(session, user, ttype):
        tokendata = None
        while 1:
            tokendata = secrets.token_hex(ttype.value.size//2)
            print(tokendata, len(tokendata), ttype.value.size, ttype.name)
            if len(session.query(Token).filter(Token.token==tokendata).all())==0: break

        token = Token(
            token=tokendata,
            owner=user,
            token_type=ttype.name,
            utc_birth=datetime.datetime.utcnow()
        )

        session.query(Token).filter(sqlalchemy.and_(Token.token_type==token.token_type, Token.owner_id==user.id)).delete()

        session.add(token)
        # print(list(session.query(Token).all()))
        session.commit()
        
        return token

    def light_new(token_type: TokenTypes):
        return Token(token=secrets.token_hex(token_type.value.size//2), token_type=token_type.name)

