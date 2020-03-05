
# SCRIPT DE POBLADO DE BD

import csv, json, http.client, urllib.parse

# 1. Leer un JSON
clase = "Festival"
file_name = "festivales.json"

data = json.load(open(file_name))

# 2. Query auxiliar #1 [ARTISTAS]
connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)

num_procesados = 0
no_encontrados_acum = 0
list_result_artista_fest = []
keys = data['festivales'][0].keys() # IMPORTANTE
print('Nombre columnas: {}'.format(keys))
for festival in data['festivales']:
	print(festival['nombre'])
	no_encontrados = 0
	result_artista_fest = []
	for i in range(len(festival['artistas'])):
		params = urllib.parse.urlencode({"where":json.dumps({
	       	"nombre": festival['artistas'][i]
	    })})
		connection.connect()
		connection.request('GET', '/parse/classes/Artista?%s' % params, '', {
       	 "X-Parse-Application-Id": "myAppId"
     	})
		result = json.loads(connection.getresponse().read())
		if (len(result['results']) > 0):
			print(festival['nombre'], festival['artistas'][i], result['results'][0]['objectId'])
			result_artista_fest.append(result['results'][0]['objectId'])
		else:
			print('Artista: [{}] no encontrado'.format(festival['artistas'][i]))
			no_encontrados += 1
	list_result_artista_fest.append(result_artista_fest)
	print('{} procesado con {} fallos\n'.format(festival['nombre'], no_encontrados))
	no_encontrados_acum += no_encontrados
	num_procesados += 1

if (len(list_result_artista_fest) == (num_procesados)):
	print("Consulta auxiliar #1 finalizada correctamente")
print('Fallos totales de la consulta auxiliar #1: {}\n'.format(no_encontrados_acum))

# 3. Query auxiliar #2 [GENEROS DEL FESTIVAL]
num_procesados = 0
no_encontrados_acum = 0
list_result_genero_fest = []
for festival in data['festivales']:
	print(festival['nombre'])
	no_encontrados = 0
	result_genero_fest = []
	for i in range(len(festival['generos'])):
		params = urllib.parse.urlencode({"where":json.dumps({
	       	"nombre": festival['generos'][i]
	    })})
		connection.connect()
		connection.request('GET', '/parse/classes/Genero?%s' % params, '', {
       	 "X-Parse-Application-Id": "myAppId"
     	})
		result = json.loads(connection.getresponse().read())
		if (len(result['results']) > 0):
			print(festival['nombre'], festival['generos'][i], result['results'][0]['objectId'])
			result_genero_fest.append(result['results'][0]['objectId'])
		else:
			print('Género: [{}] no encontrado'.format(festival['generos'][i]))
			no_encontrados += 1
			no_encontrados_acum += no_encontrados
	list_result_genero_fest.append(result_genero_fest)
	print('{} procesado con {} fallos\n'.format(festival['nombre'], no_encontrados))
	num_procesados += 1

if (len(list_result_genero_fest) == (num_procesados)):
	print("Consulta auxiliar #2 finalizada correctamente")
print('Fallos totales de la consulta auxiliar #2: {}\n'.format(no_encontrados_acum))

# 4. construccion del JSON
request_list_list = []
request_list = []
num_procesados = 0
max_request = 50
for festival in data['festivales']:
	if ((num_procesados != 0) and ((num_procesados % max_request) == 0)):
		request_list_list.append(request_list)
		request_list = []
	new = {}
	new['nombre'] = festival['nombre']

	# artistas del festival
	new['artistas'] = []
	for artista_id in list_result_artista_fest[num_procesados]:
		new['artistas'].append({"__type": "Pointer", "className": "Artista", "objectId": artista_id})

	# generos del festival
	new['generos'] = []
	for genero_id in list_result_genero_fest[num_procesados]:
		new['generos'].append({ "__type": "Pointer", "className": "Genero", "objectId": genero_id})

	request_list.append({"method": "POST", "path": "/parse/classes/" + clase, "body": new})
	num_procesados += 1

request_list_list.append(request_list)
print('Número de festivales procesados: {}'.format(num_procesados));
print('Número de operaciones batch a realizar: {}\n'.format(len(request_list_list)))
print(request_list_list)
with open('data_festivales.json', 'w') as outfile:
    json.dump(request_list_list, outfile)

# 5. inserción en MongoDB

i = 1
for request_list in request_list_list:
	print('\nOperación batch: {}'.format(i))
	request_json = {}
	request_json["requests"] = request_list 
	connection.connect()
	connection.request("POST", "/parse/batch", json.dumps(request_json), {
	       "X-Parse-Application-Id": "myAppId",
	       "Content-Type": "application/json"
	     })
	result = json.loads(connection.getresponse().read())
	print(result)
	i += 1
