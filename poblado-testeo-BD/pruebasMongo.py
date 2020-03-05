
# SCRIPT DE CONSULTA DE BD

import csv, json, http.client, urllib.parse

connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)

# 1) TODOS LOS FESTIVALES QUE PERTENECEN A UN GÉNERO DETERMINADO

EDM = "dcjgd0HpOH" # Ejemplo1
Rock = "FD0MJkmnNQ" # Ejemplo2

generos_a_buscar = {"dcjgd0HpOH" : "EDM", "FD0MJkmnNQ" : "Rock"}
for festival_por_genero in generos_a_buscar.keys():
	params = urllib.parse.urlencode({"where":json.dumps({
	       "generos": {
	         "__type": "Pointer",
	         "className": "Genero",
	         "objectId": festival_por_genero
	       }
	     })})
	connection.connect()
	connection.request('GET', '/parse/classes/Festival?%s' % params, '', {
		 "X-Parse-Application-Id": "myAppId"
		})
	result = json.loads(connection.getresponse().read()) # nos devuelve los objetos Festival completos
	print("\nFestivales de género {} [id: {}]:".format(generos_a_buscar[festival_por_genero], festival_por_genero))
	for festival in result['results']:
		print(festival['nombre'])

# 2) TODOS LOS FESTIVALES QUE INCLUYEN A UN ARTISTA DETERMINADO

Bad_Gyal = "ULwSfjZfb8" # Ejemplo1
Lendakaris_Muertos = "ceLsGzgZxX" # Ejemplo2

artistas_a_buscar = {"ULwSfjZfb8" : "Bad Gyal", "ceLsGzgZxX" : "Lendakaris Muertos"}
for festival_por_artista in artistas_a_buscar.keys():
	params = urllib.parse.urlencode({"where":json.dumps({
	       "artistas": {
	         "__type": "Pointer",
	         "className": "Artista",
	         "objectId": festival_por_artista
	       }
	     })})
	connection.connect()
	connection.request('GET', '/parse/classes/Festival?%s' % params, '', {
		 "X-Parse-Application-Id": "myAppId"
		})
	result = json.loads(connection.getresponse().read()) # nos devuelve los objetos Festival completos
	print("\nFestivales en los que participa {} [id: {}]:".format(artistas_a_buscar[festival_por_artista], festival_por_artista))
	for festival in result['results']:
		print(festival['nombre'])

# 3) TODOS LOS ARTISTAS QUE PERTENECEN A UN GÉNERO DETERMINADO

Rap = "rLCfuEh5Az" # Ejemplo1
RyB = "pd59Hvz9T3" # Ejemplo2
EDM = "dcjgd0HpOH" # Ejemplo3

generos_a_buscar = {"rLCfuEh5Az" : "Rap", "pd59Hvz9T3" : "R&B", "dcjgd0HpOH" : "EDM"}
for artistas_por_genero in generos_a_buscar.keys():
	params = urllib.parse.urlencode({"where":json.dumps({
       "genero": {
         "__type": "Pointer",
         "className": "Genero",
         "objectId": artistas_por_genero
       }
     })})
	connection.connect()
	connection.request('GET', '/parse/classes/Artista?%s' % params, '', {
		 "X-Parse-Application-Id": "myAppId"
		})
	result = json.loads(connection.getresponse().read()) # nos devuelve los objetos Festival completos
	print("\nArtistas pertenecientes al género {} [id: {}]:".format(generos_a_buscar[artistas_por_genero], artistas_por_genero))
	if (len(result['results']) > 0):
		for artista in result['results']:
			print(artista['nombre'])
	else:
		print("NO SE HAN ENCONTRADO ARTISTAS")