this is a minimal working example of a message consumer.

the test consumes messages from the remote SingularLogic queue.

1 - go to the web interface at:
http://94.75.243.141:8161/admin/send.jsp

and send a message to the desired destination queue (foo.bar in the example) .

2 - launch the runner like this:
java -cp message-consumer-1.0-SNAPSHOT-jar-with-dependencies.jar com.collective.consumer.runner.Runner -configuration ../message-consumer-configuration.xml

syntax:
java -cp <runner class name> -configuration <xml configuration path>


3 - you should see the status of queues at
http://94.75.243.141:8161/admin/queues.jsp