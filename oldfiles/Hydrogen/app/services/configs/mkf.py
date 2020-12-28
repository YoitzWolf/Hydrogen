# -*- coding: utf-8 -*-

# main config

# libraries
import flask 	as flask
import os    	as os
import socket	as socket

# my modules

class Config():
	SECRET_KEY      	=  os.environ.get('SECRET_KEY') or 'errorpasswordtakingsf9&31j123!2jq01Z'
	UPLOAD_FOLDER   	= '/uploads'
	TEMPLATE_FOLDER		= './templates'
	FLASK_ENV       	= "development"
	FLASK_DEBUG     	=  1
	DEBUG           	=  True

class Database_Config():
	URL 				= "sqlite:///./static/databases/mnp.sqlite3?check_same_thread=False"
	TYPE 				= "sqlite"

class Logging_Config():
	FILE 				= "l.log"
	LEVEL				= "DEBUG"
	TIMED				=  True
	
# custom
ADDRES            		= "::"	# '::'
PORT              		=  80

def getAddres():  return ADDRES  
def getPort():    return PORT

def get_host(version:int=4, port:int=8080) -> tuple: 
	if version == 6: return ("::", port)
	try: 
		host_name = socket.gethostname() 
		host_ip = socket.gethostbyname(host_name) 
		return (host_ip, port)
	except: 
		return ("ERR", 0)

def init_IP(version:int=4, port:int=8080):
	global ADDRES
	global PORT
	ADDRES, PORT = get_host(version=version, port=port)
