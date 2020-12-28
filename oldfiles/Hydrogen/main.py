# -*- coding: utf-8 -*-

# module import

import flask
import os

from redis import Redis
import rq

# classes import
from app.services.configs.mkf import *


import app.alchemist.session as session

session.global_init(Database_Config.URL)

import app.services.usermaster.usr_api as UserMaster
import app.services.gamemaster.hub_api as HubMaster
import app.services.gamemaster.gmc_api as GameCreator

import app.services.tokenmaster.token_api as tokenmaster

# app init

app = flask.Flask(__name__)

def init_app(config=Config):

    global app

    # CLASSIC FLASK INITS
    app = flask.Flask(__name__, template_folder=Config.TEMPLATE_FOLDER)
    app.config.from_object(Config)

    # REDIS + QUEUE
    #app.redis = Redis.from_url(app.config['REDIS_URL'])
    #app.task_queue = rq.Queue('microblog-tasks', connection=app.redis)


def get_app(): return app


# connection tests bp

test = flask.Blueprint('test', __name__, template_folder=Config.TEMPLATE_FOLDER)    

@test.route("/test")
def test_connection():
    return {"body": "TEST OK"}

@test.route("/")
def main():
    return flask.render_template("general-templates/main.html")

if __name__ == '__main__':
    init_app()

    session.global_init(Database_Config.URL)
    
    app.register_blueprint(test)

    UserMaster.init_login(app=app)
    app.register_blueprint(UserMaster.blueprint)

    # app.register_blueprint(HubMaster.blueprint)

    app.run(host=getAddres(), port=getPort())