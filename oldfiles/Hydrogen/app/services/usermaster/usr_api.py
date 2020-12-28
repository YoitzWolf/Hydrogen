# -*- coding: utf-8 -*-

# User Sign IN OUT UP Api Master

import flask                                        as flask
import os                                           as os
import flask_login                                  as flogin

# partly
from flask_login                                    import current_user, login_user

# my modules
from app.services.configs.mkf                       import Config, Database_Config
from app.alchemist                                  import session as Session
from app.alchemist.models.user                      import User
from app.alchemist.models.token                     import Token
from app.alchemist.models.token_type                import TokenType

import app.services.tokenmaster.token_api           as token_api

""" Service Android User"""

folder = "templates"

blueprint: flask.Blueprint = flask.Blueprint('user_api', __name__, template_folder=folder)    
login_manager: flogin.LoginManager = flogin.LoginManager()
# Session.global_init(Database_Config.URL)
session = Session.create_session()

def init_blueprint(folder: str=Config.TEMPLATE_FOLDER):
    global blueprint 
    blueprint = flask.Blueprint('user_api', __name__, template_folder=folder)   

def init_login(app: flask.Flask=None):
    login_manager.setup_app(app)

def setTemplateFolder(self, folder="templates"):
    global blueprint
    blueprint = flask.Blueprint('user_api', __name__, template_folder=folder)    

def setApp(app):
    global login
    login_manager.setup_app(app)

def getBlueprint() -> flask.Blueprint:
    return blueprint

#@blueprint.route('/usr/api/is_auntethicated', methods=['GET'])
def is_auntethicated_api():
    return {"body": str(flogin.current_user.is_authenticated).upper()}



def is_auntethicated():
    return flogin.current_user.is_authenticated

def get_user():
    return flogin.current_user

# -----------API INTERFACE COURIER-----------


# -----------Routes-----------

@login_manager.user_loader
def load_user(id):
    return session.query(User).filter( User.id==int(id) ).first()

@blueprint.route('/usr/api/register', methods=['GET', 'POST'])
def register():
    if flogin.current_user.is_authenticated:
        return {"body": "ALREADY AUTHENTICATED"}
    
    elif flask.request.method == 'GET':
        return "POST ONLY"
    
    elif flask.request.method == 'POST':
        data = flask.request.get_json()   
        flask.flash("POST :: ", data)
        session = Session.create_session()
        if len(list(session.query(User).filter( (User.login==data["login"]) | (User.email==data["email"]) ).all() )) > 0:
            return {"body": "LOGIN"}
        
        user = User()
        user.login = data['login']
        user.email = data['email']
        user.set_password(data['password'])
        
        session.add(user)
        session.commit()

        return {"body": "OK"} #flask.redirect("/login")



@blueprint.route('/usr/api/login', methods=['GET', 'POST'])
def login():
    if flogin.current_user.is_authenticated:
        return {"body": "ALREADY AUTHENTICATED"} # flask.redirect('/')
    
    elif flask.request.method == 'GET':
         return "POST ONLY"

    elif flask.request.method == 'POST':
        data = flask.request.get_json()
        print("POST :: ", data)

        session = Session.create_session()
        user = session.query(User).filter( (User.login==data['login']) | ((User.email==data['login']))).first()
        print(user, "entered")
        if user is None or not user.check_password(data['password']):
            flask.flash(f"Invalid username or password: login: {data['login']}")
            return {"body": "INVALID"}
        
        login_user(user, remember=True)
        token_api.generate_valid_tocken(TokenType.auth_token, user)
        # if not flogin.is_safe_url(next):    return flask.abort(400)
        session = Session.create_session()
        return {"body": ["OK", session.query(User).filter( (User.login==data['login']) | ((User.email==data['login']))).first().token]}


@blueprint.route('/usr/api/logout')
def logout():
    flogin.logout_user()
    return {"body": "OK"}

# ------------------------user pages----------------------

@blueprint.route('/usr/api/is_auntethicated', methods=['GET'])
def token_validation():
    args = flask.request.args
    token = args["token"]
    user = token_api.get_currentuser(token, session)
    if user:
        return {"body": "OK"}
    else:
        return {"body": "FALSE"}


@blueprint.route('/usr/api/getall')
def getall():
    session = Session.create_session()
    return {"body": list(map(lambda x: (x.__repr__(), x.token), session.query(User).all()))}


