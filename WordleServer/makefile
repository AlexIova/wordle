JFLAGS = -cp
CLASSPATH = ../gson-2.10.1.jar:.
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(CLASSPATH)  $*.java


# Program name
PROGRAM_NAME = WordleServerMain

# What to compile
CLASSES = *.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

# Not standard
run:
	java $(JFLAGS) $(CLASSPATH) $(PROGRAM_NAME)
