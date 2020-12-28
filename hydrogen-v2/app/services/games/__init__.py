version = "0.0.2"
__all__ = []

packages = []

from .gms_webp import blueprint as blueprintWB
from .gms_api import blueprint as blueprintAPI

blueprints = [blueprintWB, blueprintAPI]

def get_blueprints(app):
	for blueprint in blueprints: app.register_blueprint(blueprint)
	for package in packages: package.get_blueprints(app)


