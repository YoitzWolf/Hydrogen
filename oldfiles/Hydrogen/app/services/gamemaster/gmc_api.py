# -*- coding: utf-8 -*-

# Game creating Master

import flask                                        as flask
import os                                           as os
import sys                                          as sys

# my modules
from app.services.configs.mkf		                import Config, Database_Config
from app.services.usermaster.usr_api                import is_auntethicated, get_user

from app.alchemist                                  import session as Session
from app.alchemist.models.user                      import User
from app.alchemist.models.hub                       import Hub
from app.alchemist.models.game                      import Game

""" Service Android Hub viewer"""

folder = "templates"

blueprint: flask.Blueprint = flask.Blueprint('gmc_api', __name__, template_folder=folder)    
# Session.global_init(Database_Config.URL)
# session = Session.create_session()


# deafult get jsons

@blueprint.route("/games/all")
def games():
    return {"games": list(map(lambda x: x.to_dict(), session.query(Game).all()))}


@blueprint.route("/games/filter/<string:key>/<string:value>")
def games_filtered(key, value):
    return {"games": list(map(lambda x: x.to_dict(), session.query(Game).filter(Game.__dict__[key]==value).all()))}

@blueprint.route("/games/new")
def create_game(): pass


