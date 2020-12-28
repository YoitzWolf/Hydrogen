
# libraries
import flask
import os
import secrets
import socket

class BaseAppConfig():
	PORT = 80
	HOST = "192.168.1.101"
	SQLALCHEMY_DATABASE_URI = "sqlite:///./static/databases/base.db"
	SQLALCHEMY_MIGRATE_REPO = "./static/databases/migrate-repo"

	SECRET_KEY 			= secrets.token_hex(32)
	UPLOAD_FOLDER   	= './uploads'
	TEMPLATE_FOLDER		= './templates'
	FLASK_ENV       	= "development"
	FLASK_DEBUG     	=  1
	DEBUG           	=  True

	RAW_FOLDER = "./static/upl/buffer"
	GAMES_FOLDER = "./static/upl/games"
	SOL_FOLDER = "./static/upl/store"