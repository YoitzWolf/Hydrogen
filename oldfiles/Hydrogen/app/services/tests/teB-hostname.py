
# Python3 code to display hostname and 
# IP address 
  
# Importing socket library 
import socket 
  
# Function to display hostname and 
# IP address 

def get_host(version:int=4, port:int=8080) -> tuple: 
	if version == 6: return ("::", port)
	try: 
		host_name = socket.gethostname() 
		host_ip = socket.gethostbyname(host_name) 
		return (host_ip, port)
	except: 
		return ("ERR", "ERR")
  
# Driver code 
get_host()
