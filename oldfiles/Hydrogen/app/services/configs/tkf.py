# -*- coding: utf-8 -*-

# token config

# libraries
import datetime

import flask 	as flask
import os    	as os
import socket	as socket
import enum 	as enum

from enum import Enum

# my modules

from app.alchemist.models.token_type import TokenType

class TokenTypeConfiguration(Enum):
	""" TOKEN TYPE ENUMERATION // CONFIG """
	refresh_token: TokenType = TokenType(
			id=0,
			size=64,
			ttl=datetime.time(minute=5),
			token_type="refresh_token",
		)
	auth_token: TokenType = TokenType(
			id=1,
			size=32,
			ttl=datetime.time(minute=2),
			token_type="auth_token"
		)
	

	

		
