# Before executing this Makefile, write the following lines in your terminal
#
# export CLASSPATH=$CLASSPATH:./lib/jfreechart-1.0.19.jar:./lib/jcommon-1.0.23.jar


SRC = Main.java Window.java ThreadServer.java FileManager.java

all: ${SRC}
	javac $^
	
exec:
	java Main
	
clean:
	rm *.class


