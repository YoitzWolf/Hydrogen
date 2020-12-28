# -*- coding: utf-8 -*-

# Hub Api Master

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

blueprint: flask.Blueprint = flask.Blueprint('hub_api', __name__, template_folder=folder)    
# Session.global_init(Database_Config.URL)
session = Session.create_session()


@blueprint.route("/hubs/all")
def hubs():
    return {"hubs": list(map(lambda x: x.to_dict(), session.query(Hub).all()))}


@blueprint.route("/hubs/filter/<string:key>/<string:value>")
def hubs_filtered(key: str = "", value: str = ""):
    return {"hubs": list(map(lambda x: x.to_dict(), session.query(Hub).filter(Hub.__dict__[key]==value).all()))}


