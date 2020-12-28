version = "0.0.1"
__all__ = []

from . import webp

packages = []
blueprints = [webp.blueprint]

def get_blueprints(app):
	for blueprint in blueprints: app.register_blueprint(blueprint)
	for package in packages: package.get_blueprints(app)


