VERSION=`./gradlew version -q`

./gradlew clean

if [ ! -f build/libs/sea-otter-${VERSION}.jar ]; then
	./gradlew build -x test
fi

java \
	-XX:MaxMetaspaceSize=100m \
	-Xmx1024m \
	-jar build/libs/sea-otter-${VERSION}.jar
