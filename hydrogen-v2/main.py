

#
import os
import sys
import flask

# from flask_migrate import Migrate

# my app modules
import app.configs.basic 	 as configs

import app.sql 				 as sql
sql.session.global_init(configs.BaseAppConfig)

import app.manager			 as manager
manager.init(configs.BaseAppConfig)

import app.services			 as services
# -


app = flask.Flask(__name__)
app.config.from_object(configs.BaseAppConfig)

# migrate = Migrate(app, sql.session.SqlAlchemyBase)


if __name__ == '__main__':
	services.get_blueprints(app)
	app.run(host=configs.BaseAppConfig.HOST, port=configs.BaseAppConfig.PORT)