
from enum import Enum

class MessageCodes(Enum):
    """MessageCodes enumeration"""

    Err = 0
    Ok  = 1

    AUTH_Err = 10
    AUTH_Ok  = 11

    REG_Err  = 20
    REG_Ok   = 21

    # GAMES



    # HUBS

    HUB_CREARED   = 101
    HUB_CONNECTED = 102

    HUB_LIMIT 	  = 110

    # ANOTHER


    NAME_IS_TAKEN = 400

    INVALID_TOKEN = 410	
    VALID_TOKEN	  = 411
    REFRESHED	  = 412

    WRONG_PASSW   = 420
    CORRECT_PASSW = 421

    INVALID_TASK  = 430
    UNKNOWN_VALUE = 440

    ACCESS_DENIED  = 500
    ACCESS_ALLWOED = 501

