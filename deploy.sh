if [ $? -ne 0 ]; then
  exit -1
fi

echo "Installing..."
mvn install --settings target/CM/settings.xml
