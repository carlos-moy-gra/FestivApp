
# SCRIPT DE ACTUALIZACIÓN DE BD

import csv, json, http.client, urllib.parse

# Obtenemos todos los objetos Artista almacenados en la BD
connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)
connection.connect()
connection.request('GET', '/parse/classes/Artista', '', {
       "X-Parse-Application-Id": "myAppId"
     })
result = json.loads(connection.getresponse().read())

numActualizados = 0
for artista in result['results']:
	connection.connect()
	connection.request('PUT', '/parse/classes/Artista/' + artista['objectId'], json.dumps({
	       "searchable_nombre": artista['nombre'].lower()
	     }), {
	       "X-Parse-Application-Id": "myAppId",
	       "Content-Type": "application/json"
	     })
	result = json.loads(connection.getresponse().read())

# Obtenemos todos los objetos Artista almacenados en la BD
connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)
connection.connect()
connection.request('GET', '/parse/classes/Artista', '', {
       "X-Parse-Application-Id": "myAppId"
     })
result = json.loads(connection.getresponse().read())
for artista in result['results']:
	print(artista['searchable_nombre'])