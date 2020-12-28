
# -
import flask

from flask import make_response, render_template

# -

from app.configs.basic import BaseAppConfig as Config
from app.manager	   import JSLoader


from app.sql.models import *
# -


blueprint: flask.Blueprint = flask.Blueprint('main_web', __name__, template_folder=Config.TEMPLATE_FOLDER)

@blueprint.route("/")
def mainpage():
	bar = JSLoader.get_menubar("/json/bar-menu-list.json")
	print(bar)

	return render_template("/general/base.html", bar=bar)