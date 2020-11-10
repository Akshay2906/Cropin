%~d1
cd "%~p1"
java -cp lib/*;C:/Users/kisho/.m2/repository/**/*;target/classes;target/test-classes com.automation.runner.TestNgRunner
mvn clean test -DsuiteXmlFile=TestSuite.xml
pause

