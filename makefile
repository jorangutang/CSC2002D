JAVAC = /usr/bin/javac
JFLAGS = -g
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) $(JFLAGS) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= WordApp.class WordPanel.class WordRecord.class WordDictionary.class Score.class
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

run:
	java -cp $(BINDIR) WordApp

docs:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java

clean:
	rm $(BINDIR)/*.class
