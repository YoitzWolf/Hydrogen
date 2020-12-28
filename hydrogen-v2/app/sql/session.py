import os

import sqlalchemy as alchemy
import sqlalchemy.orm as orm
from sqlalchemy.orm import Session
import sqlalchemy.ext.declarative as dec

# from migrate.versioning import api

SqlAlchemyBase = dec.declarative_base()

from .models import *

fabric = None

def global_init(config, echo=False):

	global fabric

	if fabric:
		return "Some poblems with Factory"

	db_file = config.SQLALCHEMY_DATABASE_URI
	db_repo = config.SQLALCHEMY_MIGRATE_REPO

	if not db_file:
		raise Exception(f"Problems with file {db_file}")

	conn_str = f'{db_file.strip()}' # adress
	print(f"TRYING TO CONNECT TO {conn_str} DATABASE")

	engine = alchemy.create_engine(conn_str, echo=echo, poolclass=alchemy.pool.SingletonThreadPool, connect_args={"check_same_thread": False})
	fabric = orm.sessionmaker(expire_on_commit=False, bind=engine)

	SqlAlchemyBase.metadata.create_all(engine)

	return "OK"


def create_session() -> Session:
	global fabric
	return fabric()
