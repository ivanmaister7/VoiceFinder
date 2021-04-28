f = open("C:/AudioFindProject/request.txt")
answer = f.read()
print(answer)
req = "the"
ff = open("C:/AudioFindProject/query.txt", 'w')
ff.write(req)
ff.close()
