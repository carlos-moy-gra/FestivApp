
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

# {
#   "__type": "Pointer",
#   "className": "GameScore",
#   "objectId": "Ed1nuqPvc"
# }

import csv, json, http.client

# 1. Leer un csv
clase = "Genero"
csv_name = "generos.csv"
reader = csv.DictReader(open(csv_name))

# 2. construccion del JSON
request_list = []
num_row = 0
for row in reader:
	if num_row == 0:
		keys = list(row.keys())
		print('Nombre columnas: {}'.format(keys))
	num_row += 1
	new = {}
	for col in keys:
		new[col] = row[col]
	request_list.append({"method": "POST", "path": "/parse/classes/" + clase, "body": new})
request_json = {}
request_json["requests"] = request_list
print('Filas útiles procesadas: {}'.format(num_row - 1));

# 3. Inserción en mongoDB
connection = http.client.HTTPSConnection("festivapp2020.herokuapp.com", 443)
connection.connect()
connection.request("POST", "/parse/batch", json.dumps(request_json), {
       "X-Parse-Application-Id": "myAppId",
       "Content-Type": "application/json"
     })

result = json.loads(connection.getresponse().read())
print(result)
