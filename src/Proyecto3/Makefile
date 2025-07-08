JAVAC=javac
SRC=$(wildcard *.java)
CLASSES=$(SRC:.java=.class)
MAIN=Proyecto3

all:
	$(JAVAC) -d out $(SRC)

clean:
	del /Q out\Proyecto3\*.class 2>nul || rm -f out/Proyecto3/*.class