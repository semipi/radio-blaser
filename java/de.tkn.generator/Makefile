all: TicketMessage.class

TicketMessage.class: TicketMessage.java
	javac $<

TicketMessage.java: TicketMessage.h
	nescc-mig java -java-classname=TicketMessage $< TicketMessage -o $@