# -*- coding: utf-8 -*-

# User Sign IN OUT UP Api Master

import flask                                        as flask
import os                                           as os
import flask_login                                  as flogin

# partly
from flask_login                                    import current_user, login_user

# my modules
from app.services.cfg.mkf		                    import Config, Database_Config

from app.alchemist                                  import session as Session
from app.alchemist.models.user                      import User

""" Service Android User"""

folder = "templates"

blueprint: flask.Blueprint = flask.Blueprint('user_api', __name__, template_folder=folder)    
login_manager: flogin.LoginManager = flogin.LoginManager()
# Session.global_init(Database_Config.URL)
session = Session.create_session()

def init_blueprint(folder: str=Config.TEMPLATES_FOLDER):
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

def is_auntethicated():
    return flogin.current_user.is_authenticated

def get_user():
    return flogin.current_user

# -----------API INTERFACE COURIER-----------


# -----------Routes-----------

@login_manager.user_loader
def load_user(id):
    return session.query(User).filter( User.id==int(id) ).first()

@blueprint.route('/usr/web/register', methods=['GET', 'POST'])
def register():
    if flogin.current_user.is_authenticated:
        return "ALREADY AUTHENTICATED" # flask.redirect('/')
    
    elif flask.request.method == 'GET':
        return "REGISTRATION IS OPEN"
    
    elif flask.request.method == 'POST':
        data = flask.request.form   
        flask.flash("POST :: ", data)
        
        user = User()
        user.login = data['login']
        user.email = data['email']
        
        if(data['password'] == data['password-rep']):
            user.set_password(data['password'])
        else:
            del user
            return "INVALID PASSWORD RECOVERY"
        
        session = Session.create_session()
        session.add(user)
        session.commit()

        return "OK" #flask.redirect("/login")



@blueprint.route('/usr/web/login', methods=['GET', 'POST'])
def login():
    if flogin.current_user.is_authenticated:
        return "ALREADY AUTHENTICATED" # flask.redirect('/')
    
    elif flask.request.method == 'GET':
        headers = {
                "main": "Test Page",
                "brand": SvgMaster.getFullLogo(),
                "menu": JsonMaster.htmlifyFile(
                    "./templates/json-templates/menu.json",
                    {
                        "activated": []
                    } 
                )        
        }
        return "LOGIN IS OPEN"

    elif flask.request.method == 'POST':
        data = flask.request.form
        user = session.query(User).filter( (User.login==data['login']) | ((User.email==data['login']))).first()
        print(user, "entered")
        if user is None or not user.check_password(data['password']):
            flask.flash(f"Invalid username or password: login: {data['login']}")
            return "INVALID PASSWORD OR LOGIN"
        
        login_user(user, remember=True)
        # if not flogin.is_safe_url(next):    return flask.abort(400)
            
        return "OK"


@blueprint.route('/usr/web/logout')
def logout():
    flogin.logout_user()
    return "OK"

# ------------------------user pages----------------------


