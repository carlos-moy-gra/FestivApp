
# SCRIPT DE POBLADO DE BD

# curl -X POST -H "X-Parse-Application-Id: myAppId" -H "Content-Type: application/json" -d '{}' https://festivapp2020.herokuapp.com/parse/functions/hello [OK]
# curl -X POST -H "X-Parse-Application-Id: myAppId" -H "Content-Type: application/json" -d '{"score":1337,"playerName":"Sean Plott","cheatMode":false}' https://festivapp2020.herokuapp.com/parse/classes/GameScore [OK]
# curl -X GET  -H "X-Parse-Application-Id: myAppId" https://festivapp2020.herokuapp.com/parse/classes/GameScore/cSVhgEbOeT [OK]
#curl -X POST -H "X-Parse-Application-Id: myAppId" -H "Content-Type: application/json" \
# -d '{
#       "requests": [
#          {
#           "method": "POST",
#            "path": "/parse/classes/GameScore",
#            "body": {
#              "score": 1337,
#              "playerName": "Sean Plott"
#            }
#          },
#          {
#            "method": "POST",
#            "path": "/parse/classes/GameScore",
#            "body": {
#              "score": 1338,
#              "playerName": "ZeroCool"
#            }
#          }
#        ]
#      }' \
# https://festivapp2020.herokuapp.com/parse/batch [OK]

import csv, json, http.client, urllib.parse

# 1. Leer un csv
clase = "Artista"
csv_name = "artistas.csv"
reader = csv.DictReader(open(csv_name))

# 2. Query auxiliar
connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)

num_row = 0
result_genero = []
for row in reader:
	if num_row == 0:
		keys = list(row.keys())
		print('Nombre columnas: {}'.format(keys))
	else:
		params = urllib.parse.urlencode({"where":json.dumps({
	       	"nombre": row['genero']
	    })})
		connection.connect()
		connection.request('GET', '/parse/classes/Genero?%s' % params, '', {
	       "X-Parse-Application-Id": "myAppId"
	     })
		result = json.loads(connection.getresponse().read())
		print(num_row, row['nombre'], result['results'][0]['objectId'])
		result_genero.append(result['results'][0]['objectId'])
	num_row += 1
if (len(result_genero) == (num_row - 1)):
	print("\nConsulta auxiliar correcta\n")

# 3. construccion del JSON
request_list_list = []
request_list = []
num_row = 0
max_request = 50
reader = csv.DictReader(open(csv_name)) # si no, no funciona
for row in reader:
	if num_row != 0:
		if ((num_row % max_request) == 0):
			request_list_list.append(request_list)
			request_list = []
		new = {}
		for col in keys:
			new[col] = row[col]
		genero_id = result_genero[num_row - 1]
		new['genero'] = { "__type": "Pointer", "className": "Genero", "objectId": genero_id}
		request_list.append({"method": "POST", "path": "/parse/classes/" + clase, "body": new})
	num_row += 1
request_list_list.append(request_list)
print('Filas útiles procesadas: {}'.format(num_row - 1));
print('\nNúmero de operaciones batch a realizar: {}'.format(len(request_list_list)))

connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)

i = 1
for request_list in request_list_list:
	print('\nOperación batch: {}'.format(i))
	request_json = {}
	request_json["requests"] = request_list
	# 4. Inserción en mongoDB
	connection.connect()
	connection.request("POST", "/parse/batch", json.dumps(request_json), {
	       "X-Parse-Application-Id": "myAppId",
	       "Content-Type": "application/json"
	     })
	result = json.loads(connection.getresponse().read())
	print(result)
	i += 1
