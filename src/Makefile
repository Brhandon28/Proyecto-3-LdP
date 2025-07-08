JAVAC=javac
JAVA=java
SRC_DIR=Proyecto3
OUT_DIR=out
SRCS=$(wildcard $(SRC_DIR)/*.java)
MAIN=Proyecto3.Proyecto3

all:
	$(JAVAC) -d $(OUT_DIR) $(SRCS)

run: all
	$(JAVA) -cp $(OUT_DIR) $(MAIN) $(FILE)

clean:
ifeq ($(OS),Windows_NT)
	del /Q $(OUT_DIR)\Proyecto3\*.class 2>nul
else 
	rm -f $(OUT_DIR)/Proyecto3/*.class
endif