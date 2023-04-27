JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java


# Program name
PROGRAM_NAME = WordleClientMain

# What to compile
CLASSES = *.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

# Not standard
run:
	java $(PROGRAM_NAME)
