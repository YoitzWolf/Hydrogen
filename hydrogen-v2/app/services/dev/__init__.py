version = "0.0.2"
__all__ = []


from . import dev_api as api

packages = []

blueprints = [api.blueprint]

def get_blueprints(app):
	for blueprint in blueprints: app.register_blueprint(blueprint)
	for package in packages: package.get_blueprints(app)


