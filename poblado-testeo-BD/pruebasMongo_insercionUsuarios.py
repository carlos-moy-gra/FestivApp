
# SCRIPT DE CONSULTA DE BD

import csv, json, http.client, urllib.parse

connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)

# INSERCIÓN DE USUARIOS

# connection.connect()
# connection.request('POST', '/parse/users', json.dumps({
# 	   "nombre_completo": "Carlos Moyano",
#        "username": "carlos_mg",
#        "email": "carlosmoygra@gmail.com",
#        "password": "password_prueba",
#        "sex": "Hombre",
#        "artistas_seguidos": [],
#        "generos_seguidos": [],
#        "festivales_seguidos": [],
#        "festivales_recomendados": []
#      }), {
#        "X-Parse-Application-Id": "myAppId",
#        "X-Parse-Revocable-Session": "1",
#        "Content-Type": "application/json"
#      })
# result = json.loads(connection.getresponse().read())
# print("El resultado es: {}".format(result))

# COMPROBACIÓN DE CAMPOS EN REGISTRO

username = "carlos_mg"
print("\nComprobando username: {}".format(username))
params = urllib.parse.urlencode({"where":json.dumps({
       "username": username
     })})
connection.connect()
connection.request('GET', '/parse/users?%s' % params, '', {
	 "X-Parse-Application-Id": "myAppId"
	})
result = json.loads(connection.getresponse().read())
if (len(result['results']) > 0):
	print("	Username no disponible")
else:
	print("	Username disponible")

username = "carlos_mgl"
print("\nComprobando username: {}".format(username))
params = urllib.parse.urlencode({"where":json.dumps({
       "username": username
     })})
connection.connect()
connection.request('GET', '/parse/users?%s' % params, '', {
	 "X-Parse-Application-Id": "myAppId"
	})
result = json.loads(connection.getresponse().read())
if (len(result['results']) > 0):
	print("	Username no disponible")
else:
	print("	Username disponible")

# LOGIN

username = "carlos_mg"
email = "carlosmoygra@gmail"
password = "password_prueba"
print("\nIntento de Login: {}".format(username))
params = urllib.parse.urlencode({
       "username": username,
       "password": password
     })
connection.connect()
connection.request('GET', '/parse/login?%s' % params, '', {
	 "X-Parse-Application-Id": "myAppId",
	 "X-Parse-Revocable-Session": "1"
	})
result = json.loads(connection.getresponse().read())
if ('objectId' in result):
	print("	Login OK")
else:
	print("	Login error")