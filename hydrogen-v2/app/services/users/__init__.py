version = "0.0.2"
__all__ = []

packages = []

from .usr_api import blueprint as blueprintAPI
blueprints = [blueprintAPI]

def get_blueprints(app):
	for blueprint in blueprints: app.register_blueprint(blueprint)
	for package in packages: package.get_blueprints(app)


