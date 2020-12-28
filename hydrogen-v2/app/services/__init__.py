version = "0.0.2"
__all__ = ["users", "hubs", "games", "dev", "main"]

from . import users, hubs, games, dev, main
packages = [main, users, hubs, games, dev]

blueprints = []


def get_blueprints(app):
	for blueprint in blueprints: app.register_blueprint(blueprint)
	for package in packages: package.get_blueprints(app)


