
# -
import flask

from flask import make_response, render_template

# -

from app.configs.basic import BaseAppConfig as Config
from app.manager	   import JSLoader

# -


blueprint: flask.Blueprint = flask.Blueprint('main_web', __name__, template_folder=Config.TEMPLATE_FOLDER)

@blueprint.route("/usr")
def mainpage():
	bar = JSLoader.load("/json/bar-menu-list.json")
	print(bar['items'])
	return render_template("/general/base.html", bar=bar)