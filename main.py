import random
import playsound
import speech_recognition as sr
import os

from gtts import gTTS


def listen_command():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        print("Скажіть запит: ")
        audio = r.listen(source)
        try:
            sp = r.recognize_google(audio, language="uk")
            print("Запит: " + sp)
            return sp
        except sr.UnknownValueError:
            return "error"
        except sr.RequestError:
            return "error2"


def say_message(param):
    voice = gTTS("Результат пошуку за запитом " + param, lang="uk")
    file_name = "voice" + str(random.randint(100, 10000)) + ".mp3"
    voice.save(file_name)
    playsound.playsound(file_name)
    f = open("C:/AudioFindProject/request.txt")
    answer = f.read()
    os.startfile("C:/AudioFindProject/html/" + answer + ".html")
    # print(answer)


def find_answer(message):
    ff = open("C:/AudioFindProject/query.txt", 'wb')
    ff.write(message.encode('utf-8'))
    ff.close()
    os.startfile("C:/Users/ВАНЯ/PycharmProjects/pythonProject1/run.vbs")


def do_command(message):
    message = message.lower()
    find_answer(message)
    say_message(message)


step = "y"
while step == "y":
    command = listen_command()
    do_command(command)
    print("Продовжити пошук? y/n")
    step = input();
